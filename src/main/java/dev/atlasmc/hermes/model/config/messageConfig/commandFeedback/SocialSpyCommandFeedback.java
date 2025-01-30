package dev.atlasmc.hermes.model.config.messageConfig.commandFeedback;

import dev.atlasmc.hermes.command.SocialSpyCommand;
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
public class SocialSpyCommandFeedback extends CommandFeedbackItem {
    /// =======================================================
    /// /{@value SocialSpyCommand#commandName} command feedback messages.
    /**
     * MiniMessage {@value CommandFeedback#commandUsageTagName} tag for /{@value SocialSpyCommand#commandName}.
     */
    @Comment("The MiniMessage to be used in place of the <"+ MiniMessageCustomTagConstants.commandUsage + "> custom tag.")
    public static String usagePlaceholderStatic = "/socialSpy \\<state>";
    private String usagePlaceholder = usagePlaceholderStatic;

    /**
     * MiniMessage feedback for command /{@value SocialSpyCommand#commandName} missing parameter {@value SocialSpyCommand#stateArgumentName}.
     */
    public Component getMissingStateArgumentComponent() {
        return deserializeWithUsageTag(missingStateArgument, usagePlaceholderStatic);
    }

    private String missingStateArgument = "<dark_gray>[<dark_aqua>hermes<dark_gray>] <gray>No argument specified for <dark_gray>\\<state><gray><br>usage: <dark_gray><usage>";

    /**
     * MiniMessage feedback for command /{@value SocialSpyCommand#commandName} invalid parameter {@value SocialSpyCommand#stateArgumentName}.
     */
    public Component getInvalidStateArgumentComponent() {
        return deserializeWithUsageTag(invalidStateArgument, usagePlaceholderStatic);
    }

    private String invalidStateArgument = "<dark_gray>[<dark_aqua>hermes<dark_gray>] <gray>Invalid argument specified for <dark_gray>\\<state><gray><br>The state must be either \"true\" or \"false\"<br>usage: <dark_gray><usage>";

    /**
     * MiniMessage feedback for command /{@value SocialSpyCommand#commandName} socialSpy activated.
     */
    public Component getActivatedMessageComponent() {
        return deserializeWithUsageTag(activatedMessage, usagePlaceholderStatic);
    }

    private String activatedMessage = "<dark_gray>[<dark_aqua>hermes<dark_gray>] <gray>Activated SocialSpy.";

    /**
     * MiniMessage feedback for command /{@value SocialSpyCommand#commandName} socialSpy activated.
     */
    public Component getDeactivatedMessageComponent() {
        return deserializeWithUsageTag(deactivatedMessage, usagePlaceholderStatic);
    }

    private String deactivatedMessage = "<dark_gray>[<dark_aqua>hermes<dark_gray>] <gray>Deactivated SocialSpy.";

    /**
     * MiniMessage feedback for command /{@value SocialSpyCommand#commandName} missing permission node {@value PermissionConstants#commandPermissionPrivateMessageReply}.
     */
    public Component getMissingPermissionComponent() {
        return deserializeWithUsageTag(missingPermission, usagePlaceholderStatic);
    }

    private String missingPermission = MessageFormat.format("<dark_gray>[<dark_aqua>hermes<dark_gray>] <red> You are missing the permission node \"{0}\"",
            PermissionConstants.commandPermissionSocialSpy);
}
