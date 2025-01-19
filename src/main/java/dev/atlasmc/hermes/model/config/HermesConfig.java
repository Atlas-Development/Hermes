package dev.atlasmc.hermes.model.config;

import dev.atlasmc.hermes.constant.MiniMessageCustomTagConstants;
import dev.atlasmc.hermes.model.config.messageConfig.MessageConfig;
import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.util.HashMap;
import java.util.Map;

/**
 * configuration class which is used for serialization/deserialization of the config.yml file.
 */
@Data
@ConfigSerializable
public class HermesConfig {
    /**
     * The config file version.
     */
    public final static String configurationVersion = "0.0.0";

    /**
     * The config file version for serialization. This is a copy of {@link #configurationVersion}.
     */
    @Comment("The version of the config file.")
    private final String configVersion = configurationVersion;

    /**
     * Configuration object containing command feedback and message formats.
     */
    @Comment("Configurations for all types of messages sent by Hermes.")
    private final MessageConfig messageConfig = new MessageConfig();

    /**
     * Configuration object containing aliases for all commands.
     */
    @Comment("Aliases that can be used instead of the default command name.")
    private final CommandAliases commandAliases = new CommandAliases();

    /**
     * Map for translating server names to server prefix MiniMessage strings.
     */
    @Comment("""
            Prefixes to be used in place of the <server_prefix> custom tag.
            The key is the name of the server as specified in the "velocity.toml"
            The value is the prefix in MiniMessage format to use for the respective server.
            e.g. If the velocity.toml contains the servers "survival" and "lobby":
                survival: "<green>sv"
                lobby: "<gray>lb"
            """)
    private final Map<String, String> serverPrefixes = new HashMap<>();

    /**
     * The MiniMessage string to be used when a server could not be found within {@link #serverPrefixes}.<br/>
     * This can still use the {@value MiniMessageCustomTagConstants#serverName} custom tag.
     */
    @Comment("the prefix in MiniMessage format to be used for the <server_prefix> custom tag when no entry was found in the serverPrefixes config.\n" +
            "supported custom MiniMessage tags: <" + MiniMessageCustomTagConstants.serverName + ">.")
    private String fallbackServerPrefix = String.format("<hover:show_text:'<%s> <red>(not configured)'><gray>?</hover>", MiniMessageCustomTagConstants.serverName);

    /**
     * MiniMessage to be used when referring to the console in place of tags like {@value MiniMessageCustomTagConstants#sender},
     * {@value MiniMessageCustomTagConstants#receiver} or {@value MiniMessageCustomTagConstants#player}.
     */
    @Comment("The name to use for the console in place of tags like <sender>, <receiver> or <player>.")
    private String consoleName = "<red>Console";

    /**
     * MiniMessage to be used when referring to an unknown audience.
     */
    @Comment("The name to use for chat audiences that have no resolvable name.")
    private String unknownAudienceFallbackName = "<gray>unknown";

    /**
     * Parsed server prefix components which are generated from {@link #serverPrefixes} using {@link #getServerPrefixComponent(String)} when needed.
     * This Property is not serialized and deserialized.
     */
    private transient final Map<String, Component> serverPrefixComponents = new HashMap<>();

    /**
     * Get a configured server prefix component by server name, returns {@link #fallbackServerPrefix} if no configuration was found.
     *
     * @param serverName the server name to get a prefix for
     * @return the parsed miniMessage component
     */
    public Component getServerPrefixComponent(final String serverName) {
        Component prefixComponent = serverPrefixComponents.get(serverName);
        if (prefixComponent != null)
            return prefixComponent;

        //if the prefix component is not in the map, generate and add it
        String prefixMiniMessageString = serverPrefixes.get(serverName);
        if (prefixMiniMessageString == null)
            prefixMiniMessageString = fallbackServerPrefix;
        final MiniMessage miniMessage = MiniMessage.miniMessage();
        prefixComponent = miniMessage.deserialize(prefixMiniMessageString,
                Placeholder.unparsed(MiniMessageCustomTagConstants.serverName, serverName));

        serverPrefixComponents.put(serverName, prefixComponent);
        return prefixComponent;
    }

    public HermesConfig() {
        this.setDefaults();
    }

    /**
     * Set default values that are not in the properties declaration for generating an initial configuration.
     */
    public void setDefaults() {
        serverPrefixes.put("lobby", String.format("<hover:show_text:'<%s>'><gray>lb</hover>", MiniMessageCustomTagConstants.serverName));
        serverPrefixes.put("dev", String.format("<hover:show_text:'<%s>'><gray>d</hover>", MiniMessageCustomTagConstants.serverName));
    }
}
