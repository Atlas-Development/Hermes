package dev.atlasmc.hermes.model.config.messageConfig;

import dev.atlasmc.hermes.constant.MiniMessageCustomTagConstants;
import lombok.Data;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.text.MessageFormat;

@Data
@ConfigSerializable
public class EventFeedback {
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
    @Comment("""
            Message to send when a player joins the server.
            """)
    private String playerJoinedServer = MessageFormat.format("<green>+ <dark_gray>[<{3}:''|''><dark_gray>] [<gray><hover:show_text:''<{2}>''><{1}></hover><dark_gray>] <gray><{0}>",
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
    @Comment("""
            Message to send when a player leaves the server.
            """)
    private String playerLeftServer = MessageFormat.format("<red>- <dark_gray>[<{3}:''|''><dark_gray>] [<gray><hover:show_text:''<{2}>''><{1}></hover><dark_gray>] <gray><{0}>",
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
    @Comment("""
            Message to send when a player switches servers.
            """)
    private String playerSwitchedServer = MessageFormat.format("<gray>-> <{0}> switched from <hover:show_text:''<{2}>''><{1}></hover> to <hover:show_text:''<{4}>''><{3}></hover>",
            MiniMessageCustomTagConstants.player,
            MiniMessageCustomTagConstants.currentServerPrefix,
            MiniMessageCustomTagConstants.currentServerName,
            MiniMessageCustomTagConstants.previousServerPrefix,
            MiniMessageCustomTagConstants.previousServerName);
}
