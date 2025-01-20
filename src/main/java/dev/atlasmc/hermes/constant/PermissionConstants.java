package dev.atlasmc.hermes.constant;

public interface PermissionConstants {
    /// ===================================================================
    /// Permissions for sending messages to different types of private message related commands.
    /**
     * Permission for the "/message" command.
     */
    String commandPermissionPrivateMessageInitiate = "hermes.command.privatemsg.initiate";

    /**
     * Permission for the "/reply" command.
     */
    String commandPermissionPrivateMessageReply = "hermes.command.privatemsg.reply";

    /**
     * Permission for the "/socialspy" command.
     */
    String commandPermissionSocialSpy = "hermes.command.moderation.spy.privatemsg";

    /// ===================================================================
    /// Permissions for sending messages to different types of channels.
    /**
     * Permission for setting whether the user can send messages to a specific server.<br/>
     * Use hermes.chat.message.send.server.* for all servers.
     * @param serverName the proxy config name of the server to get the permission name for.
     * @return the permission name for the specified server.
     */
    static String getChatSendServerMessage(String serverName){
        return "hermes.channel.server.send." + serverName;
    }

    /**
     * Permission for setting whether the user can send messages to the global server channel.
     */
    String chatSendServerGlobalMessage = "hermes.channel.proxy.send";


    /// ===================================================================
    /// Permissions for receiving messages from different types of channels.
    /**
     * Permission for setting whether the user can receive messages from a specific server.<br/>
     * use hermes.chat.message.receive.server.* for all servers.
     * @param serverName the proxy config name of the server to get the permission name for.
     * @return the permission name for the specified server.
     */
    static String getChatReceiveServerMessage(String serverName){
        return "hermes.channel.server.receive." + serverName;
    }

    /**
     * Permission for setting whether the user can receive messages from the global server channel.
     */
    String chatReceiveServerGlobalMessage = "hermes.channel.proxy.receive";


    /// ===================================================================
    /// Permissions for triggering messages when a player joins/leaves/switches servers.
    /**
     * Permission for setting whether the player should cause a server join message.
     */
    String playerJoinServerMessage = "hermes.event.player.join";

    /**
     * Permission for setting whether the player should cause a server leave message.
     */
    String playerLeaveServerMessage = "hermes.event.player.leave";

    /**
     * Permission for setting whether the player should cause a server switch message.
     */
    String playerSwitchServerMessage = "hermes.event.player.switch";


    /// ===================================================================
    /// Permissions for chat message formatting.
    /**
     * Permission for setting whether MiniMessage legacy chat-codes in a player's private messages or private reply messages should be applied.<br/>
     * Legacy chat-codes are in the form of [ampersand][format-char][text].<br/>
     * For more info, see <a href="https://docs.advntr.dev/serializer/legacy.html">MiniMessage legacy serializer</a>.
     */
    String formatLegacyChatCodesPrivateMessage = "hermes.format.privatemsg.chatcodes";

    /**
     * Permission for setting whether MiniMessage legacy chat-codes in a player's server messages should be applied.<br/>
     * Legacy chat-codes are in the form of [ampersand][format-char][text].<br/>
     * For more info, see <a href="https://docs.advntr.dev/serializer/legacy.html">MiniMessage legacy serializer</a>.
     */
    String formatLegacyChatCodesServer = "hermes.format.server.chatcodes";

    /**
     * Permission for setting whether MiniMessage legacy chat-codes in a player's global server messages should be applied.<br/>
     * Legacy chat-codes are in the form of [ampersand][format-char][text].<br/>
     * For more info, see <a href="https://docs.advntr.dev/serializer/legacy.html">MiniMessage legacy serializer</a>.
     */
    String formatLegacyChatCodesServerGlobal = "hermes.format.proxy.chatcodes";
}
