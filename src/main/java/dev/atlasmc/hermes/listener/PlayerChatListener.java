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
import dev.atlasmc.hermes.model.config.messageConfig.MessageFormats;
import dev.atlasmc.hermes.model.config.messageConfig.MiscellaneousMessages;
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

        //stop if the message is a command
        if (message.charAt(0) == '/')
            return;
        int messageStart = 0;
        final boolean toggleGlobalChat;
        //check for characters to be cut off
        if(message.charAt(messageStart) == '!') {
            toggleGlobalChat = true;
            messageStart++;
        } else {
            toggleGlobalChat = false;
        }
        if(message.length() > messageStart && message.charAt(messageStart) == '.') {
            messageStart += message.charAt(messageStart + 1) == '/' ||
                    message.charAt(messageStart + 1) == '!' ? 1 : 0;
        }
        message = message.substring(messageStart);

        final PlayerConfiguration playerConfiguration = playerConfigurations.get(player);
        if (playerConfiguration == null) {
            logger.warn(String.format("could not get playerConfiguration for player %s (%s)", player.getUsername(), player.getUniqueId()));
            return;
        }
        final Collection<Component> groupPrefixes = playerConfiguration.getLpGroupPrefixes();
        final Component primaryGroupPrefix = playerConfiguration.getPrimaryGroupPrefix();
        final boolean useGlobalChat;
        final MiscellaneousMessages miscellaneousMessages = configuration.getMessageConfig().getMiscellaneousMessages();

        if (toggleGlobalChat) {
            if (message.isEmpty()) {
                final MiniMessage miniMessage = MiniMessage.miniMessage();
                //toggle between global and server chat
                if (playerConfiguration.isServerGlobalChannelSender()) {
                    playerConfiguration.setServerGlobalChannelSender(false);
                    player.sendMessage(miniMessage.deserialize(miscellaneousMessages.getSwitchedToServerChatMessage()));
                } else {
                    playerConfiguration.setServerGlobalChannelSender(true);
                    player.sendMessage(miniMessage.deserialize(miscellaneousMessages.getSwitchedToGlobalChatMessage()));
                }
                return;
            }
            //write in global chat if the player is currently in local chat and vice versa
            useGlobalChat = !playerConfiguration.isServerGlobalChannelSender();
        } else {
            useGlobalChat = playerConfiguration.isServerGlobalChannelSender();
        }
        sendServerMessage(useGlobalChat, serverName, player, message);
    }

    /**
     * Sends a given message and replaces custom minimessage tags.
     * Replaces the following tags
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
        final MiniMessage miniMessage = MiniMessage.miniMessage();
        final Optional<Collection<Component>> senderLpGroupsPrefix = AudienceHelper.getAudienceLpGroupPrefix(sender, playerConfigurations);
        final Optional<Component> senderLpPrimaryGroupPrefix = AudienceHelper.getAudienceLpPrimaryGroupPrefix(sender, playerConfigurations);
        final Component serverPrefix = configuration.getServerPrefixComponent(serverName);
        final MessageFormats messageFormats = configuration.getMessageConfig().getMessageFormats();
        final Component messageComponent;

        final Channel targetChannel;
        final String miniMessageString;

        if (globalChat) {
            //check permissions
            if (!sender.hasPermission(PermissionConstants.chatSendServerGlobalMessage))
                return;
            messageComponent = sender.hasPermission(PermissionConstants.formatLegacyChatCodesServerGlobal) ?
                    LegacyComponentSerializer.legacyAmpersand().deserialize(message) : Component.text(message);

            //use global channel
            miniMessageString = messageFormats.getPlayerServerGlobalChat();
            targetChannel = channelManager.getServerGlobalChannel();
        } else {
            //check permissions
            if (!sender.hasPermission(PermissionConstants.getChatSendServerMessage(serverName)))
                return;
            messageComponent = sender.hasPermission(PermissionConstants.formatLegacyChatCodesServer) ?
                    LegacyComponentSerializer.legacyAmpersand().deserialize(message) : Component.text(message);

            //use server channel
            miniMessageString = messageFormats.getPlayerServerChat();
            targetChannel = channelManager.getServerChannel(serverName);
        }

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

        final Component chatMiniMessageComponent = miniMessage.deserialize(miniMessageString, customTagResolvers);
        targetChannel.sendMessage(chatMiniMessageComponent);
    }
}
