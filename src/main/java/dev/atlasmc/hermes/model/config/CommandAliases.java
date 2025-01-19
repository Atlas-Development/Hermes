package dev.atlasmc.hermes.model.config;

import dev.atlasmc.hermes.command.HermesCommand;
import dev.atlasmc.hermes.command.PrivateMessageCommand;
import dev.atlasmc.hermes.command.PrivateMessageReplyCommand;
import dev.atlasmc.hermes.command.SocialSpyCommand;
import lombok.Data;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@Data
@ConfigSerializable
public class CommandAliases {
    @Comment("Aliases for /" + PrivateMessageCommand.commandName + ".")
    private String[] privateMessageCommandAliases = new String[]{"msg", "tell"};

    @Comment("Aliases for /" + PrivateMessageReplyCommand.commandName + ".")
    private String[] privateMessageReplyCommandAliases = new String[]{"r"};

    @Comment("Aliases for /" + SocialSpyCommand.commandName + ".")
    private String[] socialSpyCommandAliases = new String[]{"sspy"};

    @Comment("Aliases for /" + HermesCommand.commandName + ".")
    private String[] hermesCommandAliases = new String[0];
}
