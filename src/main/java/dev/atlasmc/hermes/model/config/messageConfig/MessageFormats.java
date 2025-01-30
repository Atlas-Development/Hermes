package dev.atlasmc.hermes.model.config.messageConfig;

import dev.atlasmc.hermes.constant.MiniMessageCustomTagConstants;
import lombok.Data;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.text.MessageFormat;

@Data
@ConfigSerializable
public class MessageFormats {
    /**
     * MiniMessage for global server chat messages sent by a player.<br/>
     * May use the following custom tags:
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
    @Comment("MiniMessage format for global server chat messages." +
            "Supports the following MiniMessage tags:" +
            "<" + MiniMessageCustomTagConstants.serverPrefix + ">,\n" +
            "<" + MiniMessageCustomTagConstants.serverName + ">,\n" +
            "<" + MiniMessageCustomTagConstants.sender + ">,\n" +
            "<" + MiniMessageCustomTagConstants.message + ">,\n" +
            "<" + MiniMessageCustomTagConstants.senderHasLpGroupPrefix + ">,\n" +
            "<" + MiniMessageCustomTagConstants.senderLpPrimaryGroupPrefix + ">,\n" +
            "<" + MiniMessageCustomTagConstants.senderLpGroupsPrefix + ">")
    private String playerServerGlobalChat = MessageFormat.format("<dark_gray>[<gray><hover:show_text:''Global message sent from server <{3}>''>g</hover><dark_gray>] <dark_gray>[<{0}><dark_gray>] <white><{1}>: <{2}>",
            MiniMessageCustomTagConstants.senderLpGroupsPrefix,
            MiniMessageCustomTagConstants.sender,
            MiniMessageCustomTagConstants.message,
            MiniMessageCustomTagConstants.serverName);

    /**
     * MiniMessage for server chat messages sent by a player.<br/>
     * May use the following custom tags:
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
    @Comment("MiniMessage format for global server chat messages." +
            "Supports the following MiniMessage tags:" +
            "<" + MiniMessageCustomTagConstants.serverPrefix + ">,\n" +
            "<" + MiniMessageCustomTagConstants.serverName + ">,\n" +
            "<" + MiniMessageCustomTagConstants.sender + ">,\n" +
            "<" + MiniMessageCustomTagConstants.message + ">,\n" +
            "<" + MiniMessageCustomTagConstants.senderHasLpGroupPrefix + ">,\n" +
            "<" + MiniMessageCustomTagConstants.senderLpPrimaryGroupPrefix + ">,\n" +
            "<" + MiniMessageCustomTagConstants.senderLpGroupsPrefix + ">")
    private String playerServerChat = MessageFormat.format("<dark_gray>[<gray><hover:show_text:''Server name: <{0}>''><{1}></hover><dark_gray>] [<{2}><dark_gray>] <white><{3}>: <{4}>",
            MiniMessageCustomTagConstants.serverName,
            MiniMessageCustomTagConstants.serverPrefix,
            MiniMessageCustomTagConstants.senderLpGroupsPrefix,
            MiniMessageCustomTagConstants.sender,
            MiniMessageCustomTagConstants.message);

    /**
     * MiniMessage for private messages as seen by the sender of the message.<br/>
     * May use the following custom tags:
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
    @Comment("MiniMessage format for global server chat messages." +
            "Supports the following MiniMessage tags:" +
            "<" + MiniMessageCustomTagConstants.sender + ">,\n" +
            "<" + MiniMessageCustomTagConstants.receiver + ">,\n" +
            "<" + MiniMessageCustomTagConstants.message + ">,\n" +
            "<" + MiniMessageCustomTagConstants.senderHasLpGroupPrefix + ">,\n" +
            "<" + MiniMessageCustomTagConstants.receiverHasLpGroupPrefix + ">,\n" +
            "<" + MiniMessageCustomTagConstants.senderLpPrimaryGroupPrefix + ">,\n" +
            "<" + MiniMessageCustomTagConstants.receiverLpPrimaryGroupPrefix + ">,\n" +
            "<" + MiniMessageCustomTagConstants.senderLpGroupsPrefix + ">,\n" +
            "<" + MiniMessageCustomTagConstants.receiverLpGroupsPrefix + ">,\n" +
            "<" + MiniMessageCustomTagConstants.senderIsPlayer + ">,\n" +
            "<" + MiniMessageCustomTagConstants.receiverIsPlayer + ">,\n" +
            "<" + MiniMessageCustomTagConstants.senderIsConsole + ">,\n" +
            "<" + MiniMessageCustomTagConstants.receiverIsConsole + ">")
    private String privateMessageSender = MessageFormat.format("<dark_gray>[<color:#40a0a0>msg<dark_gray>] <gray>you → <{0}:''<dark_gray>[<{1}><dark_gray>]'':''''> <white><{2}>: <{3}>",
            MiniMessageCustomTagConstants.receiverHasLpGroupPrefix,
            MiniMessageCustomTagConstants.receiverLpGroupsPrefix,
            MiniMessageCustomTagConstants.receiver,
            MiniMessageCustomTagConstants.message);

    /**
     * MiniMessage for private messages as seen by the receiver of the message.<br/>
     * May use the following custom tags:
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
    @Comment("MiniMessage format for global server chat messages." +
            "Supports the following MiniMessage tags:" +
            "<" + MiniMessageCustomTagConstants.sender + ">,\n" +
            "<" + MiniMessageCustomTagConstants.receiver + ">,\n" +
            "<" + MiniMessageCustomTagConstants.message + ">,\n" +
            "<" + MiniMessageCustomTagConstants.senderHasLpGroupPrefix + ">,\n" +
            "<" + MiniMessageCustomTagConstants.receiverHasLpGroupPrefix + ">,\n" +
            "<" + MiniMessageCustomTagConstants.senderLpPrimaryGroupPrefix + ">,\n" +
            "<" + MiniMessageCustomTagConstants.receiverLpPrimaryGroupPrefix + ">,\n" +
            "<" + MiniMessageCustomTagConstants.senderLpGroupsPrefix + ">,\n" +
            "<" + MiniMessageCustomTagConstants.receiverLpGroupsPrefix + ">,\n" +
            "<" + MiniMessageCustomTagConstants.senderIsPlayer + ">,\n" +
            "<" + MiniMessageCustomTagConstants.receiverIsPlayer + ">,\n" +
            "<" + MiniMessageCustomTagConstants.senderIsConsole + ">,\n" +
            "<" + MiniMessageCustomTagConstants.receiverIsConsole + ">")
    private String privateMessageReceiver = MessageFormat.format("<dark_gray>[<color:#40a0a0>msg<dark_gray>] <{0}:''[<{1}><dark_gray>]'':''''> <white><{2}> <gray>→ you<white>: <{3}>",
            MiniMessageCustomTagConstants.senderHasLpGroupPrefix,
            MiniMessageCustomTagConstants.senderLpGroupsPrefix,
            MiniMessageCustomTagConstants.sender,
            MiniMessageCustomTagConstants.message);

    /**
     * MiniMessage for private messages to third parties e.g. console or social spy.<br/>
     * May use the following custom tags:
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
    @Comment("MiniMessage format for global server chat messages." +
            "Supports the following MiniMessage tags:" +
            "<" + MiniMessageCustomTagConstants.sender + ">,\n" +
            "<" + MiniMessageCustomTagConstants.receiver + ">,\n" +
            "<" + MiniMessageCustomTagConstants.message + ">,\n" +
            "<" + MiniMessageCustomTagConstants.senderHasLpGroupPrefix + ">,\n" +
            "<" + MiniMessageCustomTagConstants.receiverHasLpGroupPrefix + ">,\n" +
            "<" + MiniMessageCustomTagConstants.senderLpPrimaryGroupPrefix + ">,\n" +
            "<" + MiniMessageCustomTagConstants.receiverLpPrimaryGroupPrefix + ">,\n" +
            "<" + MiniMessageCustomTagConstants.senderLpGroupsPrefix + ">,\n" +
            "<" + MiniMessageCustomTagConstants.receiverLpGroupsPrefix + ">,\n" +
            "<" + MiniMessageCustomTagConstants.senderIsPlayer + ">,\n" +
            "<" + MiniMessageCustomTagConstants.receiverIsPlayer + ">,\n" +
            "<" + MiniMessageCustomTagConstants.senderIsConsole + ">,\n" +
            "<" + MiniMessageCustomTagConstants.receiverIsConsole + ">")
    private String privateMessageThirdParty = MessageFormat.format("<dark_gray>[<color:#40a0a0>msg<dark_gray>] <{0}:''[<{1}><dark_gray>]'':''''> <white><{4}> <gray>→ <{2}:''<dark_gray>[<{3}><dark_gray>]'':''''> <white><{5}>: <{6}>",
            MiniMessageCustomTagConstants.senderHasLpGroupPrefix,
            MiniMessageCustomTagConstants.senderLpGroupsPrefix,
            MiniMessageCustomTagConstants.receiverHasLpGroupPrefix,
            MiniMessageCustomTagConstants.receiverLpGroupsPrefix,
            MiniMessageCustomTagConstants.sender,
            MiniMessageCustomTagConstants.receiver,
            MiniMessageCustomTagConstants.message);
}
