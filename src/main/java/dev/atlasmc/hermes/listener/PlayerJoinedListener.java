package dev.atlasmc.hermes.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.atlasmc.hermes.constant.MiniMessageCustomTagConstants;
import dev.atlasmc.hermes.constant.PermissionConstants;
import dev.atlasmc.hermes.helper.AudienceHelper;
import dev.atlasmc.hermes.model.channel.ChannelManager;
import dev.atlasmc.hermes.model.config.HermesConfig;
import dev.atlasmc.hermes.model.config.messageConfig.EventFeedback;
import dev.atlasmc.hermes.model.config.messageConfig.MiscellaneousMessages;
import dev.atlasmc.hermes.model.config.runtime.PlayerConfiguration;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.luckperms.api.LuckPerms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class PlayerJoinedListener {

    @Getter
    private final Map<Audience, PlayerConfiguration> playerConfigurations;

    @Getter
    private final ChannelManager channelManager;

    @Getter
    private final LuckPerms luckPerms;

    @Getter
    private final ProxyServer proxy;

    @Getter
    private final HermesConfig configuration;

    public PlayerJoinedListener(final Map<Audience, PlayerConfiguration> playerConfigurations, final ChannelManager channelManager,
                                final LuckPerms luckPerms, final ProxyServer proxy, final HermesConfig configuration) {
        this.playerConfigurations = playerConfigurations;
        this.channelManager = channelManager;
        this.luckPerms = luckPerms;
        this.proxy = proxy;
        this.configuration = configuration;
    }

    @Subscribe
    public void onPlayerJoined(final ServerConnectedEvent event) {
        final Player player = event.getPlayer();
        final Optional<RegisteredServer> previousServer = event.getPreviousServer();
        final RegisteredServer currentServer = event.getServer();
        final MiscellaneousMessages miscellaneousMessages = configuration.getMessageConfig().getMiscellaneousMessages();

        //try to find an existing playerConfiguration for the joined player and create one if none was found
        //player configuration must be gotten by uuid because the audience changed
        PlayerConfiguration playerConfiguration = null;
        for (PlayerConfiguration currentPlayerConfig : playerConfigurations.values()) {
            if (!currentPlayerConfig.getPlayerUUID().equals(player.getUniqueId()))
                continue;
            playerConfiguration = currentPlayerConfig;
        }
        if (playerConfiguration == null) {
            playerConfiguration = new PlayerConfiguration(player, luckPerms, proxy, miscellaneousMessages);
            playerConfigurations.put(player, playerConfiguration);
        } else {
            //re-assign playerConfiguration to the new audience
            playerConfigurations.remove(playerConfiguration.getCurrentAudience());
            playerConfigurations.put(player, playerConfiguration);
        }
        final Audience oldAudience = playerConfiguration.getCurrentAudience();
        //update audience in player config
        playerConfiguration.setCurrentAudience(player);
        //re-register player channels
        channelManager.reRegisterAudiencePrivateMessageChannel(oldAudience, player);
        channelManager.registerAudienceToConfiguredChannels(player, playerConfiguration);
        channelManager.registerAudienceToServerChannel(player, currentServer.getServerInfo().getName(), playerConfiguration);

        if (previousServer.isEmpty()) {
            if (player.hasPermission(PermissionConstants.playerJoinServerMessage))
                sendPlayerJoinedMessage(currentServer, player);
        } else if (player.hasPermission(PermissionConstants.playerSwitchServerMessage))
            sendPlayerSwitchedServerMessage(previousServer.get(), currentServer, player);
    }

    /**
     * Sends a player join message to {@link ChannelManager#getServerGlobalChannel()}.<br/>
     * Uses the format specified in {@link EventFeedback#getPlayerJoinedServer()}.
     *
     * @param server the server that the player joined to.
     * @param player the player that joined.
     */
    public void sendPlayerJoinedMessage(final RegisteredServer server, final Player player) {
        final MiniMessage miniMessage = MiniMessage.miniMessage();
        final String serverName = server.getServerInfo().getName();
        final Component serverPrefix = configuration.getServerPrefixComponent(serverName);
        final Optional<Collection<Component>> senderLpGroupsPrefix = AudienceHelper.getAudienceLpGroupPrefix(player, playerConfigurations);
        final Optional<Component> senderLpPrimaryGroupPrefix = AudienceHelper.getAudienceLpPrimaryGroupPrefix(player, playerConfigurations);
        final EventFeedback eventFeedback = configuration.getMessageConfig().getEventFeedback();

        //define custom tag replacements
        final TagResolver[] customTagResolvers = new TagResolver[]{
                Placeholder.component(MiniMessageCustomTagConstants.currentServerPrefix, serverPrefix),
                Placeholder.unparsed(MiniMessageCustomTagConstants.currentServerName, serverName),
                Placeholder.component(MiniMessageCustomTagConstants.player, AudienceHelper.resolveAudienceNameComponent(player, configuration)),
                Placeholder.component(MiniMessageCustomTagConstants.senderLpPrimaryGroupPrefix, senderLpPrimaryGroupPrefix.orElse(Component.empty())),
                Formatter.joining(MiniMessageCustomTagConstants.senderLpGroupsPrefix, senderLpGroupsPrefix.orElse(new ArrayList<>())),
                Formatter.booleanChoice(MiniMessageCustomTagConstants.senderHasLpGroupPrefix, senderLpGroupsPrefix.isPresent())
        };

        final Component playerJoinedMessageComponent = miniMessage.deserialize(eventFeedback.getPlayerJoinedServer(), customTagResolvers);
        this.channelManager.getServerGlobalChannel().sendMessage(playerJoinedMessageComponent);
    }

    /**
     * Sends a player change server message to {@link ChannelManager#getServerGlobalChannel()}.<br/>
     * Uses the format specified in {@link EventFeedback#getPlayerSwitchedServer()}.
     *
     * @param previousServer the server that the player switched from.
     * @param currentServer  the server that the player switched to.
     * @param player         the player that switched servers.
     */
    public void sendPlayerSwitchedServerMessage(final RegisteredServer previousServer, final RegisteredServer currentServer,
                                                final Player player) {
        final MiniMessage miniMessage = MiniMessage.miniMessage();
        final String previousServerName = previousServer.getServerInfo().getName();
        final String currentServerName = currentServer.getServerInfo().getName();
        final Component previousServerPrefix = configuration.getServerPrefixComponent(previousServerName);
        final Component currentServerPrefix = configuration.getServerPrefixComponent(currentServerName);
        final Optional<Collection<Component>> senderLpGroupsPrefix = AudienceHelper.getAudienceLpGroupPrefix(player, playerConfigurations);
        final Optional<Component> senderLpPrimaryGroupPrefix = AudienceHelper.getAudienceLpPrimaryGroupPrefix(player, playerConfigurations);
        final EventFeedback eventFeedback = configuration.getMessageConfig().getEventFeedback();

        //define custom tag replacements
        final TagResolver[] customTagResolvers = new TagResolver[]{
                Placeholder.component(MiniMessageCustomTagConstants.previousServerPrefix, previousServerPrefix),
                Placeholder.unparsed(MiniMessageCustomTagConstants.previousServerName, previousServerName),
                Placeholder.component(MiniMessageCustomTagConstants.currentServerPrefix, currentServerPrefix),
                Placeholder.unparsed(MiniMessageCustomTagConstants.currentServerName, currentServerName),
                Placeholder.component(MiniMessageCustomTagConstants.player, AudienceHelper.resolveAudienceNameComponent(player, configuration)),
                Placeholder.component(MiniMessageCustomTagConstants.senderLpPrimaryGroupPrefix, senderLpPrimaryGroupPrefix.orElse(Component.empty())),
                Formatter.joining(MiniMessageCustomTagConstants.senderLpGroupsPrefix, senderLpGroupsPrefix.orElse(new ArrayList<>())),
                Formatter.booleanChoice(MiniMessageCustomTagConstants.senderHasLpGroupPrefix, senderLpGroupsPrefix.isPresent())
        };

        final Component playerJoinedMessageComponent = miniMessage.deserialize(eventFeedback.getPlayerSwitchedServer(), customTagResolvers);
        this.channelManager.getServerGlobalChannel().sendMessage(playerJoinedMessageComponent);
    }
}
