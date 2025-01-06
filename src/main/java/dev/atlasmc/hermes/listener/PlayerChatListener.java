package dev.atlasmc.hermes.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.atlasmc.hermes.constant.MiniMessageCustomTagConstants;
import dev.atlasmc.hermes.constant.PermissionConstants;
import dev.atlasmc.hermes.helper.AudienceHelper;
import dev.atlasmc.hermes.model.channel.Channel;
import dev.atlasmc.hermes.model.channel.ChannelManager;
import dev.atlasmc.hermes.model.config.HermesConfig;
import dev.atlasmc.hermes.model.config.MessageFormats;
import dev.atlasmc.hermes.model.config.runtime.PlayerConfiguration;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import java.util.*;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.slf4j.Logger;

public class PlayerChatListener {
    private static final String fallbackServerName = "?";

    @Getter
    private final ProxyServer proxy;

    @Getter
    private final Logger logger;

    @Getter
    private final HermesConfig configuration;

    @Getter
    private final ChannelManager channelManager;

    @Getter
    private final Map<Audience, PlayerConfiguration> playerConfigurations;

    public PlayerChatListener(final ProxyServer proxy, final Logger logger, final HermesConfig configuration,
                              final ChannelManager channelManager, final Map<Audience, PlayerConfiguration> playerConfigurations) {
        this.proxy = proxy;
        this.logger = logger;
        this.configuration = configuration;
        this.channelManager = channelManager;
        this.playerConfigurations = playerConfigurations;
    }

    @Subscribe
    public void onPlayerChat(final PlayerChatEvent event) {
        final Player player = event.getPlayer();
        String message = event.getMessage();
        final String serverName = player.getCurrentServer()
                .map(server -> server.getServerInfo().getName())
                .orElse(fallbackServerName);

        //cancel player message
        event.setResult(PlayerChatEvent.ChatResult.denied());

        //check if message is a command
        if (message.charAt(0) == '/')
            return;
        else if (message.startsWith("./"))
            message = message.substring(1);

        final PlayerConfiguration playerConfiguration = playerConfigurations.get(player);
        if (playerConfiguration == null) {
            logger.warn(String.format("could not get playerConfiguration for player %s (%s)", player.getUsername(), player.getUniqueId()));
            return;
        }
        final Collection<Component> groupPrefixes = playerConfiguration.getLpGroupPrefixes();
        final Component primaryGroupPrefix = playerConfiguration.getPrimaryGroupPrefix();
        final boolean useGlobalChat;

        if (message.charAt(0) == '!') {
            if (message.length() == 1) {
                final MiniMessage miniMessage = MiniMessage.miniMessage();
                //toggle between global and server chat
                if (playerConfiguration.isServerGlobalChannelSender()) {
                    playerConfiguration.setServerGlobalChannelSender(false);
                    player.sendMessage(miniMessage.deserialize(configuration.getCommandFeedbackMessages().getSwitchedToServerChatMessage()));
                } else {
                    playerConfiguration.setServerGlobalChannelSender(true);
                    player.sendMessage(miniMessage.deserialize(configuration.getCommandFeedbackMessages().getSwitchedToGlobalChatMessage()));
                }
                return;
            }
            //remove '!' from message
            message = message.substring(1);

            //write in global chat if the player is currently in local chat and vice versa
            useGlobalChat = !playerConfiguration.isServerGlobalChannelSender();
        } else {
            useGlobalChat = playerConfiguration.isServerGlobalChannelSender();
        }
        sendServerMessage(useGlobalChat, serverName, player, message);
    }

    /**
     * sends a given message and replaces custom minimessage tags.
     * {@link MessageFormats#getPrivateMessageSenderFormat()}
     * replaces the following tags
     * <ul>
     *     <li>{@value MiniMessageCustomTagConstants#serverPrefix}</li>
     *     <li>{@value MiniMessageCustomTagConstants#sender}</li>
     *     <li>{@value MiniMessageCustomTagConstants#message}</li>
     *     <li>{@value MiniMessageCustomTagConstants#senderHasLpGroupPrefix}</li>
     *     <li>{@value MiniMessageCustomTagConstants#senderLpPrimaryGroupPrefix}</li>
     *     <li>{@value MiniMessageCustomTagConstants#senderLpGroupsPrefix}</li>
     *     <li>{@value MiniMessageCustomTagConstants#}</li>
     *     <li>{@value MiniMessageCustomTagConstants#}</li>
     *     <li>{@value MiniMessageCustomTagConstants#}</li>
     *     <li>{@value MiniMessageCustomTagConstants#}</li>
     * </ul>
     *
     * @param globalChat whether the message should be sent in global chat.
     * @param serverName the servers name which is used to get custom tag replacements.
     * @param sender     the sender of the message to use for tag replacements.
     * @param message    the message to send.
     */
    public void sendServerMessage(final boolean globalChat, final String serverName, final Player sender, final String message) {
        final Optional<RegisteredServer> server = proxy.getServer(serverName);
        if (server.isEmpty()) {
            logger.error("tried to send message in non existent server!");
            return;
        }
        final Component messageComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(message);
        final MiniMessage miniMessage = MiniMessage.miniMessage();
        final Optional<Collection<Component>> senderLpGroupsPrefix = AudienceHelper.getAudienceLpGroupPrefix(sender, playerConfigurations);
        final Optional<Component> senderLpPrimaryGroupPrefix = AudienceHelper.getAudienceLpPrimaryGroupPrefix(sender, playerConfigurations);
        final Component serverPrefix = configuration.getServerPrefixComponent(serverName);

        //define custom tag replacements
        final TagResolver[] customTagResolvers = new TagResolver[]{
                Placeholder.component(MiniMessageCustomTagConstants.serverPrefix, serverPrefix),
                Placeholder.unparsed(MiniMessageCustomTagConstants.serverName, serverName),
                Placeholder.component(MiniMessageCustomTagConstants.sender, AudienceHelper.resolveAudienceNameComponent(sender, configuration)),
                Placeholder.component(MiniMessageCustomTagConstants.message, messageComponent),
                Placeholder.component(MiniMessageCustomTagConstants.senderLpPrimaryGroupPrefix, senderLpPrimaryGroupPrefix.orElse(Component.empty())),
                Formatter.joining(MiniMessageCustomTagConstants.senderLpGroupsPrefix, senderLpGroupsPrefix.orElse(new ArrayList<>())),
                Formatter.booleanChoice(MiniMessageCustomTagConstants.senderHasLpGroupPrefix, senderLpGroupsPrefix.isPresent())
        };

        final Channel targetChannel;
        final String miniMessageString;
        if (globalChat) {
            //check permission
            if (!sender.hasPermission(PermissionConstants.chatSendServerGlobalMessage))
                return;
            //use global channel
            miniMessageString = configuration.getMessageFormats().getPlayerServerGlobalChatMessageFormat();
            targetChannel = channelManager.getServerGlobalChannel();
        } else {
            //check permission
            if (!sender.hasPermission(PermissionConstants.getChatSendServerMessage(serverName)))
                return;
            //use server channel
            miniMessageString = configuration.getMessageFormats().getPlayerServerChatMessageFormat();
            targetChannel = channelManager.getServerChannel(serverName);
        }

        final Component chatMiniMessageComponent = miniMessage.deserialize(miniMessageString, customTagResolvers);
        targetChannel.sendMessage(chatMiniMessageComponent);
    }
}
