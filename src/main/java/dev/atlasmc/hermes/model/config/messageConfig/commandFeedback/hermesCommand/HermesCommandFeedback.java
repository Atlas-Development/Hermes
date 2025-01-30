package dev.atlasmc.hermes.model.config.messageConfig.commandFeedback.hermesCommand;

import dev.atlasmc.hermes.command.HermesCommand;
import dev.atlasmc.hermes.constant.MiniMessageCustomTagConstants;
import dev.atlasmc.hermes.model.config.messageConfig.CommandFeedback;
import dev.atlasmc.hermes.model.config.messageConfig.commandFeedback.CommandFeedbackItem;
import lombok.Data;
import net.kyori.adventure.text.Component;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@Data
@ConfigSerializable
public class HermesCommandFeedback extends CommandFeedbackItem {
    /// =======================================================
    /// /{@value HermesCommand#commandName} command feedback messages.
    /**
     * MiniMessage {@value CommandFeedback#commandUsageTagName} tag for /{@value HermesCommand#commandName}.
     */
    @Comment("The MiniMessage to be used in place of the <"+ MiniMessageCustomTagConstants.commandUsage + "> custom tag.")
    public static String usagePlaceholderStatic = "/hermes \\<type>";
    private String usagePlaceholder = usagePlaceholderStatic;

    /**
     * MiniMessage feedback for command /{@value HermesCommand#commandName} missing parameter {@value HermesCommand#hermesSubCommandTypeHelp}.
     */
    public Component getMissingTypeArgumentComponent() {
        return deserializeWithUsageTag(missingTypeArgument, usagePlaceholderStatic);
    }

    private String missingTypeArgument = "<dark_gray>[<dark_aqua>hermes<dark_gray>] <gray>No argument specified for <dark_gray>\\<type><gray><br>usage: <dark_gray><usage>";

    private HelpCommandFeedback helpCommandFeedback = new HelpCommandFeedback();
}
