package dev.atlasmc.hermes.model.config.messageConfig.commandFeedback.hermesCommand;

import dev.atlasmc.hermes.command.HermesCommand;
import dev.atlasmc.hermes.constant.MiniMessageCustomTagConstants;
import dev.atlasmc.hermes.model.config.messageConfig.CommandFeedback;
import dev.atlasmc.hermes.model.config.messageConfig.commandFeedback.CommandFeedbackItem;
import dev.atlasmc.hermes.model.config.messageConfig.commandFeedback.PrivateMessageCommandFeedback;
import dev.atlasmc.hermes.model.config.messageConfig.commandFeedback.PrivateMessageReplyCommandFeedback;
import dev.atlasmc.hermes.model.config.messageConfig.commandFeedback.SocialSpyCommandFeedback;
import lombok.Data;
import net.kyori.adventure.text.Component;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.text.MessageFormat;

@Data
@ConfigSerializable
public class HelpCommandFeedback extends CommandFeedbackItem {
    /// =======================================================
    /// /{@value HermesCommand#commandName} help command feedback messages.
    /**
     * MiniMessage {@value CommandFeedback#commandUsageTagName} tag for /{@value HermesCommand#commandName} help.
     */
    @Comment("The MiniMessage to be used in place of the <"+ MiniMessageCustomTagConstants.commandUsage + "> custom tag.")
    public static String usagePlaceholderStatic = "/hermes help";
    private String usagePlaceholder = usagePlaceholderStatic;

    /**
     * MiniMessage feedback for command /{@value HermesCommand#commandName} missing parameter {@value HermesCommand#hermesSubCommandTypeHelp}.
     */
    public Component getHermesUnknownTypeArgumentComponent() {
        return deserializeWithUsageTag(unknownTypeArgument, usagePlaceholderStatic);
    }

    private String unknownTypeArgument = "<gray>Invalid argument specified for <dark_gray>\\<type><gray><br>usage: <dark_gray><usage>";

    /**
     * MiniMessage feedback for command /{@value HermesCommand#commandName}.
     */
    public Component getHelpCommandMessage() {
        return deserializeWithUsageTag(helpCommandMessage, usagePlaceholderStatic);
    }

    /**
     * Messages to be sent when {@link HermesCommand} is executed with the help argument.
     */
    @Comment("Feedback messages for the \"/" + HermesCommand.commandName + " " + HermesCommand.hermesSubCommandTypeHelp + "\" command.")
    private String helpCommandMessage = MessageFormat.format("""
            <dark_gray>[<dark_aqua>hermes<dark_gray>] <gray>Hermes is a chat plugin for velocity.
            supported commands are:<white>
            {0}
            {1}
            {2}
            {3}
            {4}""",
            HermesCommandFeedback.usagePlaceholderStatic,
            HelpCommandFeedback.usagePlaceholderStatic,
            PrivateMessageCommandFeedback.usagePlaceholderStatic,
            PrivateMessageReplyCommandFeedback.usagePlaceholderStatic,
            SocialSpyCommandFeedback.usagePlaceholderStatic);
}
