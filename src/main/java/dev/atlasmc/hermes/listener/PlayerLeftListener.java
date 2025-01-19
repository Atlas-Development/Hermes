package dev.atlasmc.hermes.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import dev.atlasmc.hermes.constant.MiniMessageCustomTagConstants;
import dev.atlasmc.hermes.constant.PermissionConstants;
import dev.atlasmc.hermes.helper.AudienceHelper;
import dev.atlasmc.hermes.model.channel.ChannelManager;
import dev.atlasmc.hermes.model.config.HermesConfig;
import dev.atlasmc.hermes.model.config.messageConfig.EventFeedback;
import dev.atlasmc.hermes.model.config.runtime.PlayerConfiguration;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class PlayerLeftListener {

    @Getter
    private final ChannelManager channelManager;

    @Getter
    private final HermesConfig configuration;

    @Getter
    private final Map<Audience, PlayerConfiguration> playerConfigurations;

    public PlayerLeftListener(final ChannelManager channelManager, final HermesConfig configuration, final Map<Audience, PlayerConfiguration> playerConfigurations) {
        this.channelManager = channelManager;
        this.configuration = configuration;
        this.playerConfigurations = playerConfigurations;
    }

    @Subscribe
    public void onPlayerLeft(final DisconnectEvent event) {
        final Player player = event.getPlayer();
        channelManager.unregisterAudienceNamedChannels(player);
        //do not unregister private channels as they can only be re-registered if they persist
        if (player.hasPermission(PermissionConstants.playerLeaveServerMessage)) {
            final Optional<ServerConnection> currentServerConnection = player.getCurrentServer();
            final String serverName = currentServerConnection.map(c -> c.getServer().getServerInfo().getName()).orElse("?");
            this.sendPlayerLeftMessage(serverName, player);
        }
    }

    /**
     * Sends a player leave message to {@link ChannelManager#getServerGlobalChannel()}.<br/>
     * Uses the format specified in {@link EventFeedback#getPlayerLeftServer()}.
     *
     * @param serverName the name of the server the player was on.
     * @param player     the player that left.
     */
    public void sendPlayerLeftMessage(final String serverName, final Player player) {
        final MiniMessage miniMessage = MiniMessage.miniMessage();
        final Component serverPrefix = configuration.getServerPrefixComponent(serverName);
        final Optional<Collection<Component>> senderLpGroupsPrefix = AudienceHelper.getAudienceLpGroupPrefix(player, playerConfigurations);
        final Optional<Component> senderLpPrimaryGroupPrefix = AudienceHelper.getAudienceLpPrimaryGroupPrefix(player, playerConfigurations);

        //define custom tag replacements
        final TagResolver[] customTagResolvers = new TagResolver[]{
                Placeholder.component(MiniMessageCustomTagConstants.currentServerPrefix, serverPrefix),
                Placeholder.unparsed(MiniMessageCustomTagConstants.currentServerName, serverName),
                Placeholder.component(MiniMessageCustomTagConstants.player, AudienceHelper.resolveAudienceNameComponent(player, configuration)),
                Placeholder.component(MiniMessageCustomTagConstants.senderLpPrimaryGroupPrefix, senderLpPrimaryGroupPrefix.orElse(Component.empty())),
                Formatter.joining(MiniMessageCustomTagConstants.senderLpGroupsPrefix, senderLpGroupsPrefix.orElse(new ArrayList<>())),
                Formatter.booleanChoice(MiniMessageCustomTagConstants.senderHasLpGroupPrefix, senderLpGroupsPrefix.isPresent())
        };

        final Component playerJoinedMessageComponent = miniMessage.deserialize(configuration.getMessageConfig().getEventFeedback().getPlayerLeftServer(), customTagResolvers);
        this.channelManager.getServerGlobalChannel().sendMessage(playerJoinedMessageComponent);
    }
}
