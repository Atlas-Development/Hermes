package dev.atlasmc.hermes.model.config.messageConfig;

import dev.atlasmc.hermes.constant.MiniMessageCustomTagConstants;
import lombok.Data;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
@Data
public class MessageConfig {
    @Comment("MiniMessage formats for chat messages.")
    private final MessageFormats messageFormats = new MessageFormats();

    @Comment("MiniMessage formatted feedback messages that are sent when a command is executed")
    private final CommandFeedback commandFeedback = new CommandFeedback();

    @Comment("MiniMessage formats for messages that are sent when certain events occur.")
    private final EventFeedback eventFeedback = new EventFeedback();

    @Comment("Miscellaneous message formats.")
    private final MiscellaneousMessages miscellaneousMessages = new MiscellaneousMessages();
}
