package dev.atlasmc.hermes.constant;

public interface PermissionConstants {
    /// ===================================================================
    /// Permissions for sending messages to different types of private message related commands.
    /// For all private message related commands, use "hermes.command.message.private.*".
    /**
     * Permission for the "/message" command.
     */
    String commandPermissionPrivateMessageInitiate = "hermes.command.message.private.initiate";

    /**
     * Permission for the "/reply" command.
     */
    String commandPermissionPrivateMessageReply = "hermes.command.message.private.reply";

    /**
     * Permission for the "/socialspy" command.
     */
    String commandPermissionSocialSpy = "hermes.command.moderation.socialspy";

    /// ===================================================================
    /// permissions for sending messages to different types of channels.
    /// for all channels, use "hermes.chat.message.send.*".
    /**
     * Permission for setting whether the user can send messages to a specific server.<br/>
     * Use hermes.chat.message.send.server.* for all servers.
     * @param serverName the proxy config name of the server to get the permission name for.
     * @return the permission name for the specified server.
     */
    static String getChatSendServerMessage(String serverName){
        return "hermes.chat.message.send." + serverName;
    }

    /**
     * Permission for setting whether the user can send messages to the global server channel.
     */
    String chatSendServerGlobalMessage = "hermes.chat.message.send.serverglobal";


    /// ===================================================================
    /// Permissions for receiving messages from different types of channels.
    /// For all channels, use "hermes.chat.message.receive.*".
    /**
     * Permission for setting whether the user can receive messages from a specific server.<br/>
     * use hermes.chat.message.receive.server.* for all servers.
     * @param serverName the proxy config name of the server to get the permission name for.
     * @return the permission name for the specified server.
     */
    static String getChatReceiveServerMessage(String serverName){
        return "hermes.chat.message.receive.server." + serverName;
    }

    /**
     * Permission for setting whether the user can receive messages from the global server channel.
     */
    String chatReceiveServerGlobalMessage = "hermes.chat.message.receive.serverglobal";


    /// ===================================================================
    /// Permissions for triggering messages when a player joins/leaves/switches servers.
    /// For all server change triggers, use "hermes.serverchange.*".
    /**
     * Permission for setting whether the player should cause a server join message.
     */
    String playerJoinServerMessage = "hermes.serverchange.join";

    /**
     * Permission for setting whether the player should cause a server leave message.
     */
    String playerLeaveServerMessage = "hermes.serverchange.leave";

    /**
     * Permission for setting whether the player should cause a server switch message.
     */
    String playerSwitchServerMessage = "hermes.serverchange.change";


    /// ===================================================================
    /// Permissions for chat message formatting.
    /// For all message formatting options in all types of chat messages, use "hermes.chat.message.format.*".
    /**
     * Permission for setting whether MiniMessage legacy chat-codes in a player's private messages or private reply messages should be applied.<br/>
     * Legacy chat-codes are in the form of [ampersand][format-char][text].<br/>
     * For more info, see <a href="https://docs.advntr.dev/serializer/legacy.html">MiniMessage legacy serializer</a>.
     */
    String formatLegacyChatCodesPrivateMessage = "hermes.chat.message.format.private.legacy";

    /**
     * Permission for setting whether MiniMessage legacy chat-codes in a player's server messages should be applied.<br/>
     * Legacy chat-codes are in the form of [ampersand][format-char][text].<br/>
     * For more info, see <a href="https://docs.advntr.dev/serializer/legacy.html">MiniMessage legacy serializer</a>.
     */
    String formatLegacyChatCodesServer = "hermes.chat.message.format.server.legacy";

    /**
     * Permission for setting whether MiniMessage legacy chat-codes in a player's global server messages should be applied.<br/>
     * Legacy chat-codes are in the form of [ampersand][format-char][text].<br/>
     * For more info, see <a href="https://docs.advntr.dev/serializer/legacy.html">MiniMessage legacy serializer</a>.
     */
    String formatLegacyChatCodesServerGlobal = "hermes.chat.message.format.serverglobal.legacy";
}
