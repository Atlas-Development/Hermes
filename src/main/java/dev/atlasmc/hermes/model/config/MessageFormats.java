package dev.atlasmc.hermes.model.config;

import dev.atlasmc.hermes.constant.MiniMessageCustomTagConstants;
import lombok.Data;

import java.text.MessageFormat;

@Data
public class MessageFormats {
    /**
     * MiniMessage for global server chat messages sent by a player.<br/>
     * may use the following custom tags:
     * <ul>
     *     <li>{@value MiniMessageCustomTagConstants#serverPrefix}</li>
     *     <li>{@value MiniMessageCustomTagConstants#serverName}</li>
     *     <li>{@value MiniMessageCustomTagConstants#sender}</li>
     *     <li>{@value MiniMessageCustomTagConstants#message}</li>
     *     <li>{@value MiniMessageCustomTagConstants#senderHasLpGroupPrefix}</li>
     *     <li>{@value MiniMessageCustomTagConstants#senderLpPrimaryGroupPrefix}</li>
     *     <li>{@value MiniMessageCustomTagConstants#senderLpGroupsPrefix}</li>
     * </ul>
     */
    private String playerServerGlobalChatMessageFormat = MessageFormat.format("<color:#555555>[<color:#aaaaaa><hover:show_text:''global message sent from server {3}''>g</hover><color:#555555>] [<color:#ffffff><{0}><color:#555555>] <color:#ffffff><{1}>: <{2}>",
            MiniMessageCustomTagConstants.senderLpPrimaryGroupPrefix,
            MiniMessageCustomTagConstants.sender,
            MiniMessageCustomTagConstants.message,
            MiniMessageCustomTagConstants.serverName);

    /**
     * MiniMessage for server chat messages sent by a player.<br/>
     * may use the following custom tags:
     * <ul>
     *     <li>{@value MiniMessageCustomTagConstants#serverPrefix}</li>
     *     <li>{@value MiniMessageCustomTagConstants#serverName}</li>
     *     <li>{@value MiniMessageCustomTagConstants#sender}</li>
     *     <li>{@value MiniMessageCustomTagConstants#message}</li>
     *     <li>{@value MiniMessageCustomTagConstants#senderHasLpGroupPrefix}</li>
     *     <li>{@value MiniMessageCustomTagConstants#senderLpPrimaryGroupPrefix}</li>
     *     <li>{@value MiniMessageCustomTagConstants#senderLpGroupsPrefix}</li>
     * </ul>
     */
    private String playerServerChatMessageFormat = MessageFormat.format("<color:#555555>[<color:#aaaaaa><hover:show_text:''server name: <{0}>''><{1}></hover><color:#555555>] [<color:#ffffff><{2}><color:#555555>] <color:#ffffff><{3}>: <{4}>",
            MiniMessageCustomTagConstants.serverName,
            MiniMessageCustomTagConstants.serverPrefix,
            MiniMessageCustomTagConstants.senderLpPrimaryGroupPrefix,
            MiniMessageCustomTagConstants.sender,
            MiniMessageCustomTagConstants.message);

    /**
     * MiniMessage for private messages to third parties e.g. console or social spy.<br/>
     * may use the following custom tags:
     * <ul>
     *     <li>{@value MiniMessageCustomTagConstants#sender}</li>
     *     <li>{@value MiniMessageCustomTagConstants#receiver}</li>
     *     <li>{@value MiniMessageCustomTagConstants#message}</li>
     *     <li>{@value MiniMessageCustomTagConstants#senderHasLpGroupPrefix}</li>
     *     <li>{@value MiniMessageCustomTagConstants#receiverHasLpGroupPrefix}</li>
     *     <li>{@value MiniMessageCustomTagConstants#senderLpPrimaryGroupPrefix}</li>
     *     <li>{@value MiniMessageCustomTagConstants#receiverLpPrimaryGroupPrefix}</li>
     *     <li>{@value MiniMessageCustomTagConstants#senderLpGroupsPrefix}</li>
     *     <li>{@value MiniMessageCustomTagConstants#receiverLpGroupsPrefix}</li>
     *     <li>{@value MiniMessageCustomTagConstants#senderIsPlayer}</li>
     *     <li>{@value MiniMessageCustomTagConstants#receiverIsPlayer}</li>
     *     <li>{@value MiniMessageCustomTagConstants#senderIsConsole}</li>
     *     <li>{@value MiniMessageCustomTagConstants#receiverIsConsole}</li>
     * </ul>
     */
    private String privateMessageThirdPartyFormat = MessageFormat.format("<dark_gray>[<gray>msg<dark_gray>] <{0}:''[<gray><{1}><dark_gray>]'':''''> <white><{4}> <gray>-> <{2}:''<dark_gray>[<gray><{3}><dark_gray>]'':''''> <white><{5}>: <{6}>",
            MiniMessageCustomTagConstants.senderHasLpGroupPrefix,
            MiniMessageCustomTagConstants.senderLpPrimaryGroupPrefix,
            MiniMessageCustomTagConstants.receiverHasLpGroupPrefix,
            MiniMessageCustomTagConstants.receiverLpPrimaryGroupPrefix,
            MiniMessageCustomTagConstants.sender,
            MiniMessageCustomTagConstants.receiver,
            MiniMessageCustomTagConstants.message);

    /**
     * MiniMessage for private messages as seen by the sender of the message.<br/>
     * may use the following custom tags:
     * <ul>
     *     <li>{@value MiniMessageCustomTagConstants#sender}</li>
     *     <li>{@value MiniMessageCustomTagConstants#receiver}</li>
     *     <li>{@value MiniMessageCustomTagConstants#message}</li>
     *     <li>{@value MiniMessageCustomTagConstants#senderHasLpGroupPrefix}</li>
     *     <li>{@value MiniMessageCustomTagConstants#receiverHasLpGroupPrefix}</li>
     *     <li>{@value MiniMessageCustomTagConstants#senderLpPrimaryGroupPrefix}</li>
     *     <li>{@value MiniMessageCustomTagConstants#receiverLpPrimaryGroupPrefix}</li>
     *     <li>{@value MiniMessageCustomTagConstants#senderLpGroupsPrefix}</li>
     *     <li>{@value MiniMessageCustomTagConstants#receiverLpGroupsPrefix}</li>
     *     <li>{@value MiniMessageCustomTagConstants#senderIsPlayer}</li>
     *     <li>{@value MiniMessageCustomTagConstants#receiverIsPlayer}</li>
     *     <li>{@value MiniMessageCustomTagConstants#senderIsConsole}</li>
     *     <li>{@value MiniMessageCustomTagConstants#receiverIsConsole}</li>
     * </ul>
     */
    private String privateMessageSenderFormat = MessageFormat.format("<dark_gray>[<gray>msg<dark_gray>] <white>you <gray>-> <{0}:''<dark_gray>[<{1}:''|''><dark_gray>]'':''''> <white><{2}>: <{3}>",
            MiniMessageCustomTagConstants.receiverHasLpGroupPrefix,
            MiniMessageCustomTagConstants.receiverLpGroupsPrefix,
            MiniMessageCustomTagConstants.receiver,
            MiniMessageCustomTagConstants.message);

    /**
     * MiniMessage for private messages as seen by the receiver of the message.<br/>
     * may use the following custom tags:
     * <ul>
     *     <li>{@value MiniMessageCustomTagConstants#sender}</li>
     *     <li>{@value MiniMessageCustomTagConstants#receiver}</li>
     *     <li>{@value MiniMessageCustomTagConstants#message}</li>
     *     <li>{@value MiniMessageCustomTagConstants#senderHasLpGroupPrefix}</li>
     *     <li>{@value MiniMessageCustomTagConstants#receiverHasLpGroupPrefix}</li>
     *     <li>{@value MiniMessageCustomTagConstants#senderLpPrimaryGroupPrefix}</li>
     *     <li>{@value MiniMessageCustomTagConstants#receiverLpPrimaryGroupPrefix}</li>
     *     <li>{@value MiniMessageCustomTagConstants#senderLpGroupsPrefix}</li>
     *     <li>{@value MiniMessageCustomTagConstants#receiverLpGroupsPrefix}</li>
     *     <li>{@value MiniMessageCustomTagConstants#senderIsPlayer}</li>
     *     <li>{@value MiniMessageCustomTagConstants#receiverIsPlayer}</li>
     *     <li>{@value MiniMessageCustomTagConstants#senderIsConsole}</li>
     *     <li>{@value MiniMessageCustomTagConstants#receiverIsConsole}</li>
     * </ul>
     */
    private String privateMessageReceiverFormat = MessageFormat.format("<dark_gray>[<gray>msg<dark_gray>] <{0}:''<dark_gray>[<{1}:''|''><dark_gray>]'':''''> <white><{2}> <gray>-> <white>you: <{3}>",
            MiniMessageCustomTagConstants.senderHasLpGroupPrefix,
            MiniMessageCustomTagConstants.senderLpGroupsPrefix,
            MiniMessageCustomTagConstants.sender,
            MiniMessageCustomTagConstants.message);

    /**
     * MiniMessage to be used in place of the elements in the custom tag {@value MiniMessageCustomTagConstants#senderLpGroupsPrefix} and {@value MiniMessageCustomTagConstants#receiverLpGroupsPrefix}.<br/>
     * this is evaluated per group.<br/>
     * may use the following custom tags:
     * <ul>
     *     <li>{@value MiniMessageCustomTagConstants#lpGroupConfigDisplayName}</li>
     *     <li>{@value MiniMessageCustomTagConstants#lpGroupConfigPrefix}</li>
     * </ul>
     */
    private String luckPermsGroupTagFormat = MessageFormat.format("<hover:show_text:''<{1}>''><{0}></hover>",
            MiniMessageCustomTagConstants.lpGroupConfigPrefix,
            MiniMessageCustomTagConstants.lpGroupConfigDisplayName);

    /**
     * MiniMessage to be used in place of the custom tag {@value MiniMessageCustomTagConstants#lpPrimaryGroup}.<br/>
     * may use the following custom tags:
     * <ul>
     *     <li>{@value MiniMessageCustomTagConstants#lpGroupConfigDisplayName}</li>
     *     <li>{@value MiniMessageCustomTagConstants#lpGroupConfigPrefix}</li>
     * </ul>
     */
    private String luckPermsPrimaryGroupTagFormat = MessageFormat.format("<hover:show_text:''<{1}> (primary group)''><{0}></hover>",
            MiniMessageCustomTagConstants.lpGroupConfigPrefix,
            MiniMessageCustomTagConstants.lpGroupConfigDisplayName);

    /**
     * MiniMessage used for player join message.<br/>
     * may use the following tags:
     * <ul>
     *     <li>{@value  MiniMessageCustomTagConstants#currentServerPrefix}</li>>
     *     <li>{@value  MiniMessageCustomTagConstants#currentServerName}</li>>
     *     <li>{@value  MiniMessageCustomTagConstants#player}</li>>
     *     <li>{@value  MiniMessageCustomTagConstants#senderLpPrimaryGroupPrefix}</li>>
     *     <li>{@value  MiniMessageCustomTagConstants#senderLpGroupsPrefix}</li>>
     *     <li>{@value  MiniMessageCustomTagConstants#senderHasLpGroupPrefix}</li>>
     * </ul>
     */
    private String playerJoinedServerMessageFormat = MessageFormat.format("<green>+ <dark_gray>[<{3}:''|''><dark_gray>] [<gray><hover:show_text:''<{2}>''><{1}></hover><dark_gray>] <gray><{0}>",
            MiniMessageCustomTagConstants.player,
            MiniMessageCustomTagConstants.currentServerPrefix,
            MiniMessageCustomTagConstants.currentServerName,
            MiniMessageCustomTagConstants.senderLpGroupsPrefix);

    /**
     * MiniMessage used for player leave message.<br/>
     * may use the following tags:
     * <ul>
     *     <li>{@value  MiniMessageCustomTagConstants#currentServerPrefix}</li>>
     *     <li>{@value  MiniMessageCustomTagConstants#currentServerName}</li>>
     *     <li>{@value  MiniMessageCustomTagConstants#player}</li>>
     *     <li>{@value  MiniMessageCustomTagConstants#senderLpPrimaryGroupPrefix}</li>>
     *     <li>{@value  MiniMessageCustomTagConstants#senderLpGroupsPrefix}</li>>
     *     <li>{@value  MiniMessageCustomTagConstants#senderHasLpGroupPrefix}</li>>
     * </ul>
     */
    private String playerLeftServerMessageFormat = MessageFormat.format("<red>- <dark_gray>[<{3}:''|''><dark_gray>] [<gray><hover:show_text:''<{2}>''><{1}></hover><dark_gray>] <gray><{0}>",
            MiniMessageCustomTagConstants.player,
            MiniMessageCustomTagConstants.currentServerPrefix,
            MiniMessageCustomTagConstants.currentServerName,
            MiniMessageCustomTagConstants.senderLpGroupsPrefix);

    /**
     * MiniMessage used for player server switching message<br/>
     * may use the following tags:
     * <ul>
     *     <li>{@value  MiniMessageCustomTagConstants#previousServerPrefix}</li>>
     *     <li>{@value  MiniMessageCustomTagConstants#previousServerName}</li>>
     *     <li>{@value  MiniMessageCustomTagConstants#currentServerPrefix}</li>>
     *     <li>{@value  MiniMessageCustomTagConstants#currentServerName}</li>>
     *     <li>{@value  MiniMessageCustomTagConstants#player}</li>>
     *     <li>{@value  MiniMessageCustomTagConstants#senderLpPrimaryGroupPrefix}</li>>
     *     <li>{@value  MiniMessageCustomTagConstants#senderLpGroupsPrefix}</li>>
     *     <li>{@value  MiniMessageCustomTagConstants#senderHasLpGroupPrefix}</li>>
     * </ul>
     */
    private String playerSwitchedServerMessageFormat = MessageFormat.format("<gray>-> <{0}> switched from <hover:show_text:''<{2}>''><{1}></hover> to <hover:show_text:''<{4}>''><{3}></hover>",
            MiniMessageCustomTagConstants.player,
            MiniMessageCustomTagConstants.currentServerPrefix,
            MiniMessageCustomTagConstants.currentServerName,
            MiniMessageCustomTagConstants.previousServerPrefix,
            MiniMessageCustomTagConstants.previousServerName);
}
