package dev.atlasmc.hermes.model.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.atlasmc.hermes.command.HermesCommand;
import dev.atlasmc.hermes.command.PrivateMessageCommand;
import dev.atlasmc.hermes.command.PrivateMessageReplyCommand;
import dev.atlasmc.hermes.command.SocialSpyCommand;
import dev.atlasmc.hermes.constant.PermissionConstants;
import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

import java.text.MessageFormat;

/**
 * configured messages to send for command feedback.
 */
@Data
public class CommandFeedbackMessages {
    /// =======================================================
    /// Constant tags that can be used in other feedback messages.
    /**
     * Custom MiniMessage tag name to be replaced with {@link #privateMessageUsagePlaceholder}.
     */
    private static final String commandUsageTag = "usage";


    /// =======================================================
    /// {@value PrivateMessageCommand#commandName} command feedback messages.
    /**
     * MiniMessage to be inserted in place of the custom {@value #commandUsageTag} tag.
     */
    private String privateMessageUsagePlaceholder = "/message \\<receiver> \\<message>";

    /**
     * MiniMessage feedback for command /{@value PrivateMessageCommand#commandName} missing parameter {@value PrivateMessageCommand#receiverArgumentName}.
     */
    @JsonIgnore
    public Component getPrivateMessageMissingReceiverArgumentComponent() {
        return deserializeWithUsageTag(privateMessageMissingReceiverArgument, privateMessageUsagePlaceholder);
    }

    private String privateMessageMissingReceiverArgument = "<gray>No argument specified for <dark_gray>\\<receiver><gray><br>usage: <dark_gray><usage>";

    /**
     * MiniMessage feedback for command /{@value PrivateMessageCommand#commandName} missing parameter {@value PrivateMessageCommand#messageArgumentName}.
     */
    @JsonIgnore
    public Component getPrivateMessageMissingMessageArgumentComponent() {
        return deserializeWithUsageTag(privateMessageMissingMessageArgument, privateMessageUsagePlaceholder);
    }

    private String privateMessageMissingMessageArgument = "<gray>No argument specified for <dark_gray>\\<message><gray><br>usage: <dark_gray><usage>";

    /**
     * MiniMessage feedback for command /{@value PrivateMessageCommand#commandName} recipient could not be found.
     */
    @JsonIgnore
    public Component getPrivateMessageUnavailableRecipientMessageComponent() {
        return deserializeWithUsageTag(privateMessageUnavailableRecipientMessage, privateMessageUsagePlaceholder);
    }

    private String privateMessageUnavailableRecipientMessage = "<gray>The specified recipient is not available.";

    /**
     * MiniMessage feedback for command /{@value PrivateMessageReplyCommand#commandName} missing permission node {@value PermissionConstants#commandPermissionPrivateMessageReply}.
     */
    @JsonIgnore
    public Component getPrivateMessageMissingPermissionComponent() {
        return deserializeWithUsageTag(privateMessageMissingPermission, privateMessageUsagePlaceholder);
    }

    private String privateMessageMissingPermission = MessageFormat.format("<red> You are missing the permission node \"{0}\"",
            PermissionConstants.commandPermissionPrivateMessageInitiate);


    /// =======================================================
    /// /{@value PrivateMessageReplyCommand#commandName} command feedback messages.
    /**
     * MiniMessage {@value #commandUsageTag} tag for /{@value PrivateMessageReplyCommand#commandName}.
     */
    private String privateMessageReplyUsagePlaceholder = "/reply \\<message>";

    /**
     * MiniMessage to be sent when the reply command is executed but there is nobody to reply to.
     * MiniMessage feedback for command /{@value PrivateMessageReplyCommand#commandName} missing parameter {@value PrivateMessageReplyCommand#messageArgumentName}.
     */
    @JsonIgnore
    public Component getPrivateMessageReplyMissingMessageArgumentComponent() {
        return deserializeWithUsageTag(privateMessageReplyMissingMessageArgument, privateMessageReplyUsagePlaceholder);
    }

    private String privateMessageReplyMissingMessageArgument = "<gray>No argument specified for <dark_gray>\\<message><gray><br>usage: <dark_gray><usage>";

    /**
     * MiniMessage feedback for command /{@value PrivateMessageReplyCommand#commandName} no reply partner.
     */
    @JsonIgnore
    public Component getPrivateMessageReplyNoPartnerComponent() {
        return deserializeWithUsageTag(privateMessageReplyNoPartner, privateMessageReplyUsagePlaceholder);
    }

    private String privateMessageReplyNoPartner = "<gray>there is nobody to reply to.";

    /**
     * MiniMessage feedback for command /{@value PrivateMessageReplyCommand#commandName} recipient unavailable.<br/>
     * For offline players {@link #getPrivateMessageReplyOfflineRecipientPlayerMessageComponent()} is used instead.
     */
    @JsonIgnore
    public Component getPrivateMessageReplyUnavailableRecipientMessageComponent() {
        return deserializeWithUsageTag(privateMessageReplyUnavailableRecipientMessage, privateMessageReplyUsagePlaceholder);
    }

    private String privateMessageReplyUnavailableRecipientMessage = "<gray>The recipient is not available.";

    /**
     * MiniMessage feedback for command /{@value PrivateMessageReplyCommand#commandName} recipient player offline.
     */
    @JsonIgnore
    public Component getPrivateMessageReplyOfflineRecipientPlayerMessageComponent() {
        return deserializeWithUsageTag(privateMessageReplyOfflineRecipientPlayerMessage, privateMessageReplyUsagePlaceholder);
    }

    private String privateMessageReplyOfflineRecipientPlayerMessage = "<gray>The recipient player is offline.";

    /**
     * MiniMessage feedback for command /{@value PrivateMessageReplyCommand#commandName} missing permission node {@value PermissionConstants#commandPermissionPrivateMessageReply}.
     */
    @JsonIgnore
    public Component getPrivateMessageReplyMissingPermissionComponent() {
        return deserializeWithUsageTag(privateMessageReplyMissingPermission, privateMessageReplyUsagePlaceholder);
    }

    private String privateMessageReplyMissingPermission = MessageFormat.format("<red> You are missing the permission node \"{0}\"",
            PermissionConstants.commandPermissionPrivateMessageReply);

    /// =======================================================
    /// /{@value SocialSpyCommand#commandName} command feedback messages.
    /**
     * MiniMessage {@value #commandUsageTag} tag for /{@value SocialSpyCommand#commandName}.
     */
    private String socialSpyUsagePlaceholder = "/socialSpy \\<state>";

    /**
     * MiniMessage feedback for command /{@value SocialSpyCommand#commandName} missing parameter {@value SocialSpyCommand#stateArgumentName}.
     */
    @JsonIgnore
    public Component getSocialSpyMissingStateArgumentComponent() {
        return deserializeWithUsageTag(socialSpyMissingStateArgument, socialSpyUsagePlaceholder);
    }

    private String socialSpyMissingStateArgument = "<gray>No argument specified for <dark_gray>\\<state><gray><br>usage: <dark_gray><usage>";

    /**
     * MiniMessage feedback for command /{@value SocialSpyCommand#commandName} socialSpy activated.
     */
    @JsonIgnore
    public Component getSocialSpyActivatedMessageComponent() {
        return deserializeWithUsageTag(socialSpyActivatedMessage, socialSpyUsagePlaceholder);
    }

    private String socialSpyActivatedMessage = "<gray>Activated SocialSpy.";

    /**
     * MiniMessage feedback for command /{@value SocialSpyCommand#commandName} socialSpy activated.
     */
    @JsonIgnore
    public Component getSocialSpyDeactivatedMessageComponent() {
        return deserializeWithUsageTag(socialSpyDeactivatedMessage, socialSpyUsagePlaceholder);
    }

    private String socialSpyDeactivatedMessage = "<gray>Deactivated SocialSpy.";

    /**
     * MiniMessage feedback for command /{@value SocialSpyCommand#commandName} missing permission node {@value PermissionConstants#commandPermissionPrivateMessageReply}.
     */
    @JsonIgnore
    public Component getSocialSpyMissingPermissionComponent() {
        return deserializeWithUsageTag(socialSpyMissingPermission, socialSpyUsagePlaceholder);
    }

    private String socialSpyMissingPermission = MessageFormat.format("<red> You are missing the permission node \"{0}\"",
            PermissionConstants.commandPermissionSocialSpy);


    /// =======================================================
    /// /{@value HermesCommand#commandName} command feedback messages.
    /**
     * MiniMessage {@value #commandUsageTag} tag for /{@value HermesCommand#commandName}.
     */
    private String hermesUsagePlaceholder = "/hermes \\<type>";

    /**
     * MiniMessage feedback for command /{@value HermesCommand#commandName} missing parameter {@value HermesCommand#hermesSubCommandType}.
     */
    @JsonIgnore
    public Component getHermesMissingTypeArgumentComponent() {
        return deserializeWithUsageTag(hermesMissingTypeArgument, hermesUsagePlaceholder);
    }

    private String hermesMissingTypeArgument = "<gray>No argument specified for <dark_gray>\\<type><gray><br>usage: <dark_gray><usage>";

    /**
     * MiniMessage feedback for command /{@value HermesCommand#commandName} missing parameter {@value HermesCommand#hermesSubCommandType}.
     */
    @JsonIgnore
    public Component getHermesUnknownTypeArgumentComponent() {
        return deserializeWithUsageTag(hermesUnknownTypeArgument, hermesUsagePlaceholder);
    }

    private String hermesUnknownTypeArgument = "<gray>Invalid argument specified for <dark_gray>\\<type><gray><br>usage: <dark_gray><usage>";

    /**
     * MiniMessage feedback for command /{@value HermesCommand#commandName} subCommand {@value HermesCommand#hermesSubCommandType}.
     */
    @JsonIgnore
    public Component getHermesHelpCommandMessageComponent() {
        return deserializeWithUsageTag(hermesCommandMessage, hermesUsagePlaceholder);
    }

    private String hermesCommandMessage = MessageFormat.format("<gray>available commands<br>{0}",
            new StringBuilder()
                    .append(privateMessageUsagePlaceholder)
                    .append("<br>")
                    .append(privateMessageReplyUsagePlaceholder)
                    .append("<br>")
                    .append(socialSpyUsagePlaceholder)
                    .toString());


    /// =======================================================
    /// Global channel switch feedback messages
    /**
     * MiniMessage to be sent when switching from server to global chat
     */
    private String switchedToGlobalChatMessage = "<gray>Switched to global chat.";

    /**
     * MiniMessage to be sent when switching from server to global chat
     */
    private String switchedToServerChatMessage = "<gray>Switched to server chat.";

    /**
     * Deserialize the given miniMessage with the custom tags of this class.
     *
     * @param miniMessageString the message to deserialize.
     * @param usageMessage      the MiniMessage containing the correct usage information for the command
     * @return the deserialized miniMessage Component.
     */
    public Component deserializeWithUsageTag(String miniMessageString, String usageMessage) {
        return MiniMessage.miniMessage().deserialize(miniMessageString,
                Placeholder.parsed(commandUsageTag, usageMessage));
    }
}
