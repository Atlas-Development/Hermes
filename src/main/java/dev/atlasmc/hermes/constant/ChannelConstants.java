package dev.atlasmc.hermes.constant;

import dev.atlasmc.hermes.model.channel.NamedChannel;

public interface ChannelConstants {
    /**
     * Name of the {@link NamedChannel} that gets the messages of all server channels.
     */
    String ServerGlobalChannelName = "serverGlobalChannel";
    /**
     * Name of the {@link NamedChannel} that gets the messages of all private message channels.
     */
    String PrivateMessageGlobalChannelName = "privateMessageGlobalChannel";
}
