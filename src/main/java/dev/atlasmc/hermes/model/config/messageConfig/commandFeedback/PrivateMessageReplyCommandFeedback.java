package dev.atlasmc.hermes.model.config.messageConfig.commandFeedback;

import dev.atlasmc.hermes.command.PrivateMessageReplyCommand;
import dev.atlasmc.hermes.constant.MiniMessageCustomTagConstants;
import dev.atlasmc.hermes.constant.PermissionConstants;
import dev.atlasmc.hermes.model.config.messageConfig.CommandFeedback;
import lombok.Data;
import net.kyori.adventure.text.Component;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.text.MessageFormat;

@Data
@ConfigSerializable
public class PrivateMessageReplyCommandFeedback extends CommandFeedbackItem {
    /// =======================================================
    /// /{@value PrivateMessageReplyCommand#commandName} command feedback messages.
    /**
     * MiniMessage {@value CommandFeedback#commandUsageTagName} tag for /{@value PrivateMessageReplyCommand#commandName}.
     */
    @Comment("The MiniMessage to be used in place of the <"+ MiniMessageCustomTagConstants.commandUsage + "> custom tag.")
    public static String usagePlaceholderStatic = "/reply \\<message>";
    private String usagePlaceholder = usagePlaceholderStatic;

    /**
     * MiniMessage to be sent when the reply command is executed but there is nobody to reply to.
     * MiniMessage feedback for command /{@value PrivateMessageReplyCommand#commandName} missing parameter {@value PrivateMessageReplyCommand#messageArgumentName}.
     */
    public Component getMissingMessageArgumentComponent() {
        return deserializeWithUsageTag(missingMessageArgument, usagePlaceholderStatic);
    }

    private String missingMessageArgument = "<gray>No argument specified for <dark_gray>\\<message><gray><br>usage: <dark_gray><usage>";

    /**
     * MiniMessage feedback for command /{@value PrivateMessageReplyCommand#commandName} no reply partner.
     */
    public Component getNoPartnerComponent() {
        return deserializeWithUsageTag(noPartner, usagePlaceholderStatic);
    }

    private String noPartner = "<gray>there is nobody to reply to.";

    /**
     * MiniMessage feedback for command /{@value PrivateMessageReplyCommand#commandName} recipient unavailable.<br/>
     * For offline players {@link #getRecipientPlayerOfflineMessageComponent()} is used instead.
     */
    public Component getRecipientUnavailableMessageComponent() {
        return deserializeWithUsageTag(recipientUnavailableMessage, usagePlaceholderStatic);
    }

    private String recipientUnavailableMessage = "<gray>The recipient is not available.";

    /**
     * MiniMessage feedback for command /{@value PrivateMessageReplyCommand#commandName} recipient player offline.
     */
    public Component getRecipientPlayerOfflineMessageComponent() {
        return deserializeWithUsageTag(recipientPlayerOfflineMessage, usagePlaceholderStatic);
    }

    private String recipientPlayerOfflineMessage = "<gray>The receiving player is offline.";

    /**
     * MiniMessage feedback for command /{@value PrivateMessageReplyCommand#commandName} missing permission node {@value PermissionConstants#commandPermissionPrivateMessageReply}.
     */
    public Component getMissingPermissionComponent() {
        return deserializeWithUsageTag(missingPermission, usagePlaceholderStatic);
    }

    private String missingPermission = MessageFormat.format("<red> You are missing the permission node \"{0}\"",
            PermissionConstants.commandPermissionPrivateMessageReply);
}
