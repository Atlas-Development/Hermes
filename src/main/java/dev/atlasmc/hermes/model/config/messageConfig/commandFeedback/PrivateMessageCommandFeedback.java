package dev.atlasmc.hermes.model.config.messageConfig.commandFeedback;

import dev.atlasmc.hermes.command.PrivateMessageCommand;
import dev.atlasmc.hermes.constant.MiniMessageCustomTagConstants;
import dev.atlasmc.hermes.constant.PermissionConstants;
import dev.atlasmc.hermes.model.config.messageConfig.CommandFeedback;
import lombok.Data;
import net.kyori.adventure.text.Component;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.text.MessageFormat;

@ConfigSerializable
@Data
public class PrivateMessageCommandFeedback extends CommandFeedbackItem {
    /// =======================================================
    /// {@value PrivateMessageCommand#commandName} command feedback messages.
    /**
     * MiniMessage to be inserted in place of the custom {@value CommandFeedback#commandUsageTagName} tag.
     */
    @Comment("The MiniMessage to be used in place of the <"+ MiniMessageCustomTagConstants.commandUsage + "> custom tag.")
    public static String usagePlaceholderStatic = "/message \\<receiver> \\<message>";
    private String usagePlaceholder = usagePlaceholderStatic;

    /**
     * MiniMessage feedback for command /{@value PrivateMessageCommand#commandName} missing parameter {@value PrivateMessageCommand#receiverArgumentName}.
     */
    public Component getMissingReceiverArgumentComponent() {
        return deserializeWithUsageTag(missingReceiverArgument, usagePlaceholderStatic);
    }

    private final String missingReceiverArgument = "<dark_gray>[<dark_aqua>hermes<dark_gray>] <gray>No argument specified for <dark_gray>\\<receiver><gray><br>usage: <dark_gray><usage>";

    /**
     * MiniMessage feedback for command /{@value PrivateMessageCommand#commandName} missing parameter {@value PrivateMessageCommand#messageArgumentName}.
     */
    public Component getMissingMessageArgumentComponent() {
        return deserializeWithUsageTag(missingMessageArgument, usagePlaceholderStatic);
    }

    private final String missingMessageArgument = "<dark_gray>[<dark_aqua>hermes<dark_gray>] <gray>No argument specified for <dark_gray>\\<message><gray><br>usage: <dark_gray><usage>";

    /**
     * MiniMessage feedback for command /{@value PrivateMessageCommand#commandName} recipient could not be found.
     */
    public Component getRecipientUnavailableMessageComponent() {
        return deserializeWithUsageTag(recipientUnavailableMessage, usagePlaceholderStatic);
    }

    private final String recipientUnavailableMessage = "<dark_gray>[<dark_aqua>hermes<dark_gray>] <gray>The specified recipient is not available.";

    /**
     * MiniMessage feedback for command /{@value PrivateMessageCommand#commandName} missing permission node {@value PermissionConstants#commandPermissionPrivateMessageReply}.
     */
    public Component getMissingPermissionComponent() {
        return deserializeWithUsageTag(missingPermission, usagePlaceholderStatic);
    }

    private final String missingPermission = MessageFormat.format("<dark_gray>[<dark_aqua>hermes<dark_gray>] <red> You are missing the permission node \"{0}\"",
            PermissionConstants.commandPermissionPrivateMessageInitiate);
}
