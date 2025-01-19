package dev.atlasmc.hermes.model.config.messageConfig.commandFeedback;

import dev.atlasmc.hermes.model.config.messageConfig.CommandFeedback;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public class CommandFeedbackItem {
    /**
     * Deserialize the given miniMessage with the custom tags of this class.
     *
     * @param miniMessageString the message to deserialize.
     * @param usageMessage      the MiniMessage containing the correct usage information for the command
     * @return the deserialized miniMessage Component.
     */
    public Component deserializeWithUsageTag(String miniMessageString, String usageMessage) {
        return MiniMessage.miniMessage().deserialize(miniMessageString,
                Placeholder.parsed(CommandFeedback.commandUsageTagName, usageMessage));
    }
}
