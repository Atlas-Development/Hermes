package dev.atlasmc.hermes.constant;

import dev.atlasmc.hermes.model.config.HermesConfig;
import net.kyori.adventure.audience.Audience;

public interface MiniMessageCustomTagConstants {
    /**
     * The tag used for inserting an explanation of how to use the given command.
     */
    String commandUsage = "usage";

    /**
     * The message component in any kind of message e.g. server chat or private messages.
     */
    String message = "message";

    /**
     * The server prefix of a server that is defined in {@link HermesConfig#getServerPrefixes()}.
     */
    String serverPrefix = "server_prefix";

    /**
     * The name of the server that is configured in the proxy config.
     */
    String serverName = "server_name";

    /**
     * The name of the sender e.g. player name if it is a player or {@link HermesConfig#getConsoleName()} if it is the console.<br/>
     * Typically uses {@link dev.atlasmc.hermes.helper.AudienceHelper#resolveAudienceNameComponent(Audience, HermesConfig)} to get the name.
     */
    String sender = "sender";

    /**
     * The name of the receiver e.g. player name if it is a player or {@link HermesConfig#getConsoleName()} if it is the console.<br/>
     * Typically uses {@link dev.atlasmc.hermes.helper.AudienceHelper#resolveAudienceNameComponent(Audience, HermesConfig)} to get the name.
     */
    String receiver = "receiver";

    /**
     * A list of all parsed LuckPerms groups of the sender formatted by {@link MessageFormats#getLuckPermsGroupTagFormat()}, to be used for MiniMessage "joining" tags.
     */
    String senderLpGroupsPrefix = "sender_lp_groups_prefix";

    /**
     * A list of all parsed LuckPerms groups of the receiver formatted by {@link MessageFormats#getLuckPermsGroupTagFormat()}, to be used for MiniMessage "joining" tags.
     */
    String receiverLpGroupsPrefix = "receiver_lp_groups_prefix";

    /**
     * The primary LuckPerms group of the sender.
     */
    String senderLpPrimaryGroupPrefix = "sender_lp_primary_group_prefix";

    /**
     * The primary LuckPerms group of the receiver.
     */
    String receiverLpPrimaryGroupPrefix = "receiver_lp_primary_group_prefix";

    /**
     * Whether the sender has LuckPerms groups, useful for MiniMessage "boolean choice" tags.
     */
    String senderHasLpGroupPrefix = "sender_has_lp_group_prefix";

    /**
     * Whether the receiver has LuckPerms groups, useful for MiniMessage "boolean choice" tags.
     */
    String receiverHasLpGroupPrefix = "receiver_has_lp_group_prefix";

    /**
     * Whether the sender is a player, useful for MiniMessage "boolean choice" tags.
     */
    String senderIsPlayer = "sender_is_player";

    /**
     * Whether the receiver is a player, useful for MiniMessage "boolean choice" tags.
     */
    String receiverIsPlayer = "receiver_is_player";

    /**
     * Whether the sender is the console, useful for MiniMessage "boolean choice" tags.
     */
    String senderIsConsole = "sender_is_console";

    /**
     * Whether the receiver is the console, useful for MiniMessage "boolean choice" tags.
     */
    String receiverIsConsole = "receiver_is_console";

    /// Tags for player joined/left and switched server messages
    String previousServerPrefix = "prev_server_prefix";
    String currentServerPrefix = "curr_server_prefix";
    String previousServerName = "prev_server_name";
    String currentServerName = "curr_server_name";
    String player = "player";
    
    /// LuckPerms tags that are used per group.
    /**
     * The display name defined in the LuckPerms group configuration.
     */
    String lpGroupConfigDisplayName = "lp_group_config_display_name";

    /**
     * A list of prefixes defined in the LuckPerms group configuration.
     */
    String lpGroupPrefixList = "lp_group_prefix_list";
    String lpGroupConfigPrefix = "lp_group_config_prefix";

    /**
     * List of parsed MiniMessage components which are already formatted using {@link MessageFormats#getLuckPermsGroupTagFormat()}.
     */
    String lpGroups = "lp_groups_components";
    String lpPrimaryGroup = "lp_primary_group_component";
}
