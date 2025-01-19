package dev.atlasmc.hermes.model.config.messageConfig;

import dev.atlasmc.hermes.command.PrivateMessageCommand;
import dev.atlasmc.hermes.command.PrivateMessageReplyCommand;
import dev.atlasmc.hermes.command.SocialSpyCommand;
import dev.atlasmc.hermes.model.config.messageConfig.commandFeedback.PrivateMessageCommandFeedback;
import dev.atlasmc.hermes.model.config.messageConfig.commandFeedback.PrivateMessageReplyCommandFeedback;
import dev.atlasmc.hermes.model.config.messageConfig.commandFeedback.SocialSpyCommandFeedback;
import dev.atlasmc.hermes.model.config.messageConfig.commandFeedback.hermesCommand.HermesCommandFeedback;
import lombok.Data;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
@Data
public class CommandFeedback {
    public static final String commandUsageTagName = "usage";

    /**
     * Messages to be sent when {@link PrivateMessageCommand} is executed.
     */
    @Comment("""
            Feedback messages for the "/message <player> <message>" command.
            """)
    private final PrivateMessageCommandFeedback privateMessageCommandFeedback = new PrivateMessageCommandFeedback();

    /**
     * Messages to be sent when {@link PrivateMessageReplyCommand} is executed.
     */
    @Comment("""
            Feedback messages for the "/reply <message>" command.
            """)
    private final PrivateMessageReplyCommandFeedback privateMessageReplyCommandFeedback = new PrivateMessageReplyCommandFeedback();

    /**
     * Messages to be sent when {@link SocialSpyCommand} is executed.
     */
    @Comment("""
            Feedback messages for the "/socialSpy <state>" command.
            """)
    private final SocialSpyCommandFeedback socialSpyCommandFeedback = new SocialSpyCommandFeedback();

    /**
     * Messages to be sent when {@link SocialSpyCommand} is executed.
     */
    @Comment("""
            Feedback messages for the "/hermes <type>" command.
            """)
    private final HermesCommandFeedback hermesCommandFeedback = new HermesCommandFeedback();
}
