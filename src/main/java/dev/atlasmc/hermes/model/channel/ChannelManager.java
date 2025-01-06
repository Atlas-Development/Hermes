package dev.atlasmc.hermes.model.channel;

import com.velocitypowered.api.proxy.ProxyServer;
import dev.atlasmc.hermes.constant.ChannelConstants;
import dev.atlasmc.hermes.constant.NamedChannelType;
import dev.atlasmc.hermes.helper.AudienceHelper;
import dev.atlasmc.hermes.model.config.HermesConfig;
import dev.atlasmc.hermes.model.config.runtime.ChannelConfiguration;
import dev.atlasmc.hermes.model.config.runtime.PlayerConfiguration;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ChannelManager {
    private final Map<Audience, PrivateMessageChannel> privateMessageChannels;
    private final Map<String, NamedChannel> serverChannels;

    @Getter
    private final NamedChannel serverGlobalChannel;

    @Getter
    private final NamedChannel privateMessageGlobalChannel;

    private final HermesConfig configuration;
    private final Logger logger;
    private final Map<Audience, PlayerConfiguration> playerConfigurations;
    private final ProxyServer proxy;

    public ChannelManager(final HermesConfig configuration, final ProxyServer proxy, final Logger logger, final Map<Audience, PlayerConfiguration> playerConfigurations) {
        this.proxy = proxy;
        this.logger = logger;
        this.playerConfigurations = playerConfigurations;
        this.serverGlobalChannel = new NamedChannel(NamedChannelType.SERVER_GLOBAL, ChannelConstants.ServerGlobalChannelName);
        serverGlobalChannel.addAudience(proxy.getConsoleCommandSource());
        this.privateMessageGlobalChannel = new NamedChannel(NamedChannelType.OTHER, ChannelConstants.PrivateMessageGlobalChannelName);
        privateMessageGlobalChannel.addAudience(proxy.getConsoleCommandSource());
        this.configuration = configuration;
        this.serverChannels = new HashMap<>();
        privateMessageChannels = new HashMap<>();
    }

    public Channel getPrivateMessageChannel(final Audience audience) {
        return this.privateMessageChannels.get(audience);
    }

    /**
     * method for registering servers to {@link #serverChannels}.
     *
     * @param serverName the name of the server to add as a channel.
     */
    public void registerServerChannel(final String serverName) {
        NamedChannel serverChannel = this.serverChannels.get(serverName);
        if (serverChannel != null)
            return;
        serverChannel = new NamedChannel(NamedChannelType.SERVER, serverName);
        serverChannel.addAudience(this.proxy.getConsoleCommandSource());
        this.serverChannels.put(serverName, serverChannel);
    }

    /**
     * adds or removes an audience from the {@link #serverGlobalChannel} audience.
     *
     * @param newState whether the given audience should be contained in the channel.
     * @param audience the audience to perform the action with.
     */
    public void setServerGlobalChannelRegistration(final boolean newState, final Audience audience) {
        final boolean isGlobalAudienceMember = AudienceHelper.CheckAudienceContainsAudience(serverGlobalChannel.getReceiverAudience(), audience);
        if (newState && !isGlobalAudienceMember)
            serverGlobalChannel.addAudience(audience);
        else if (!newState && isGlobalAudienceMember)
            serverGlobalChannel.removeAudience(audience);
    }

    public NamedChannel getServerChannel(final String serverName) {
        return serverChannels.get(serverName);
    }

    /**
     * set a {@link PrivateMessageChannel} from owner to receiver and if successful, a {@link PrivateMessageChannel} channel from receiver to owner
     * and pass the {@link #privateMessageGlobalChannel} to all created channels.
     *
     * @param sender   audience that should own the channel.
     * @param receiver audience to set as receiver of the channel and for which to try to open a reply channel.
     * @return the owner channel that was created or already existed
     */
    public PrivateMessageChannel setPrivateMessageChannel(final Audience sender, final Audience receiver) {
        //set the owner private message channel if it does not exist yet
        PrivateMessageChannel ownerChannel = privateMessageChannels.get(sender);
        if (ownerChannel == null || ownerChannel.getReceiverAudience() != receiver) {
            ownerChannel = new PrivateMessageChannel(sender, receiver, privateMessageGlobalChannel, configuration, playerConfigurations);
            privateMessageChannels.put(sender, ownerChannel);
        }

        //set the receiver private message channel if it does not exist yet
        PrivateMessageChannel receiverChannel = privateMessageChannels.get(receiver);
        if (receiverChannel == null || receiverChannel.getReceiverAudience() != sender) {
            receiverChannel = new PrivateMessageChannel(receiver, sender, privateMessageGlobalChannel, configuration, playerConfigurations);
            privateMessageChannels.put(receiver, receiverChannel);
        }

        //if sender and receiver exist, set respective reply channels
        receiverChannel.addReplyChannel(ownerChannel);
        ownerChannel.addReplyChannel(receiverChannel);
        return ownerChannel;
    }

    /**
     * find an audiences sender channel and if it exists, assign it to a new audience, updating reply channel receiver audiences in the process.
     *
     * @param oldAudience the former audience to update the channel of.
     * @param newAudience the new audience to set the channel to.
     */
    public void reRegisterAudiencePrivateMessageChannel(Audience oldAudience, Audience newAudience) {
        if (oldAudience == null)
            return;
        final PrivateMessageChannel channel = privateMessageChannels.get(oldAudience);
        privateMessageChannels.remove(oldAudience);
        if (channel == null)
            return;
        channel.updateAudience(newAudience);
        privateMessageChannels.put(newAudience, channel);
    }

    public void registerAudienceToConfiguredChannels(Audience audience, ChannelConfiguration channelConfiguration) {
        for (String name : channelConfiguration.getReceivingNamedChannelNames()) {
            if (name.equals(ChannelConstants.ServerGlobalChannelName)) {
                serverGlobalChannel.addAudience(audience);
                continue;
            }
            NamedChannel namedChannel = serverChannels.get(name);
            if (namedChannel == null) {
                logger.warn(String.format("tried to register the named channel '%s', which does not exist.", name));
                continue;
            }
            namedChannel.addAudience(audience);
        }
    }

    public void registerAudienceToServerChannel(Audience audience, String serverName, ChannelConfiguration channelConfiguration) {
        serverGlobalChannel.addAudience(audience);
        channelConfiguration.addReceivingNamedChannelName(serverName);
        NamedChannel serverChannel = serverChannels.get(serverName);
        if (serverChannel != null)
            serverChannel.addAudience(audience);
    }

    /**
     * unregisters the given audience from all named channels.
     *
     * @param audience the audience to unregister.
     */
    public void unregisterAudienceNamedChannels(Audience audience) {
        //remove audience from other channels
        privateMessageGlobalChannel.removeAudience(audience);
        serverGlobalChannel.removeAudience(audience);
        for (NamedChannel serverChannel : serverChannels.values()) {
            serverChannel.removeAudience(audience);
        }
    }
}
