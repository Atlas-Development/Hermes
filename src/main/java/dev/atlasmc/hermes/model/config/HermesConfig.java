package dev.atlasmc.hermes.model.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

import java.util.HashMap;
import java.util.Map;

/**
 * configuration class which is used for serialization/deserialization of the config.yml file.
 */
@Data
public class HermesConfig {

    /**
     * config file version.
     */
    private String version;

    /**
     * configuration object for message formatting.
     */
    private final MessageFormats messageFormats;

    /**
     * configuration object for command feedback messages.
     */
    private final CommandFeedbackMessages commandFeedbackMessages;

    /**
     * tag name used for the server name custom MiniMessage tag.
     */
    @JsonIgnore
    private final static String serverNameMiniMessageTag = "servername";

    /**
     * Map for translating server names to server prefix MiniMessage strings.
     */
    private final Map<String, String> serverPrefixes;

    /**
     * The MiniMessage string to be used when a server could not be found within {@link #serverPrefixes}.<br/>
     * this can still use the {@value #serverNameMiniMessageTag} custom tag.
     */
    private String fallbackServerPrefix;

    /**
     * MiniMessage to be used when referring to the console.
     */
    private String consoleName;

    /**
     * MiniMessage to be used when referring to an unknown audience.
     */
    private String unknownAudienceFallbackName;

    /**
     * parsed server prefix components which are generated from {@link #serverPrefixes} using {@link #getServerPrefixComponent(String)} when needed.
     */
    @JsonIgnore
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final Map<String, Component> serverPrefixComponents;

    /**
     * get a configured server prefix component by server name, returns {@link #fallbackServerPrefix} if no configuration was found.
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
                Placeholder.unparsed(serverNameMiniMessageTag, serverName));

        serverPrefixComponents.put(serverName, prefixComponent);
        return prefixComponent;
    }

    public HermesConfig(final String version) {
        this.version = version;
        this.messageFormats = new MessageFormats();
        this.commandFeedbackMessages = new CommandFeedbackMessages();
        this.serverPrefixes = new HashMap<>();
        this.serverPrefixComponents = new HashMap<>();
        this.fallbackServerPrefix = "?";
    }

    public HermesConfig() {
        this.version = "";
        this.messageFormats = new MessageFormats();
        this.commandFeedbackMessages = new CommandFeedbackMessages();
        this.serverPrefixes = new HashMap<>();
        this.serverPrefixComponents = new HashMap<>();
        this.fallbackServerPrefix = "?";
    }

    /**
     * set default values for generating an initial configuration
     */
    public void setDefaults() {
        serverPrefixes.put("lobby", String.format("<hover:show_text:'<%s>'><gray>lb</hover>", serverNameMiniMessageTag));
        serverPrefixes.put("dev", String.format("<hover:show_text:'<%s>'><gray>d</hover>", serverNameMiniMessageTag));
        fallbackServerPrefix = String.format("<hover:show_text:'<%s> <red>(not configured)'><gray>?</hover>", serverNameMiniMessageTag);
        consoleName = "<red>Console</red>";
        unknownAudienceFallbackName = "<gray>unknown</gray>";
    }
}
