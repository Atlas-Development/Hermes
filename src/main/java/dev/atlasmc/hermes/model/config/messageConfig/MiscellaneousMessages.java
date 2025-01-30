package dev.atlasmc.hermes.model.config.messageConfig;

import dev.atlasmc.hermes.constant.MiniMessageCustomTagConstants;
import lombok.Data;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.text.MessageFormat;

@Data
@ConfigSerializable
public class MiscellaneousMessages {
    /// =======================================================
    /// Global channel switch feedback messages
    /**
     * MiniMessage to be sent when switching from server to global chat
     */
    private String switchedToGlobalChatMessage = "<dark_gray>[<dark_aqua>hermes<dark_gray>] <gray>Switched to global chat.";

    /**
     * MiniMessage to be sent when switching from server to global chat
     */
    private String switchedToServerChatMessage = "<dark_gray>[<dark_aqua>hermes<dark_gray>] <gray>Switched to server chat.";

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
}
