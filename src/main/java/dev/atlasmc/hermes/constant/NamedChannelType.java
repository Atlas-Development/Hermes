package dev.atlasmc.hermes.constant;

import dev.atlasmc.hermes.model.channel.NamedChannel;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

/**
 * Type of named channel used for differentiating between different permission checks in {@link NamedChannel#sendMessage(Component, Audience)}.
 */
public enum NamedChannelType {
    /**
     * the global server channel type which has its own permissions
     */
    SERVER_GLOBAL,

    /**
     * any server channel which has dynamic permissions
     */
    SERVER,

    /**
     * This does not cause an additional permission check!
     */
    OTHER
}
