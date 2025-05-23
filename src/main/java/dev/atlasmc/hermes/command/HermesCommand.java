package dev.atlasmc.hermes.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import dev.atlasmc.hermes.model.config.messageConfig.commandFeedback.hermesCommand.HermesCommandFeedback;

/**
 * Command for general plugin settings.<br/>
 * Subcommand types:
 * <table>
 *     <th>
 *         <td>type</td>
 *         <td>purpose</td>
 *     </th>
 *     <tr>
 *         <td>{@link #hermesSubCommandTypeHelp}</td>
 *         <td>returns a help message configured in {@link HermesCommandFeedback#getHelpCommandFeedback()}.</td>
 *     </tr>
 * </table>
 */
public class HermesCommand {
    public static final String commandName = "hermes";
    public static final String subCommandTypeArgumentName = "type";
    public static final String hermesSubCommandTypeHelp = "help";

    public static BrigadierCommand createBrigadierCommand(final HermesCommandFeedback hermesCommandFeedback) {
        final LiteralCommandNode<CommandSource> hermesHelpNode = LiteralArgumentBuilder.<CommandSource>literal(commandName)
                .executes(context -> {
                    context.getSource().sendMessage(hermesCommandFeedback.getMissingTypeArgumentComponent());
                    return Command.SINGLE_SUCCESS;
                })
                .then(RequiredArgumentBuilder.<CommandSource, String>argument(subCommandTypeArgumentName, StringArgumentType.word())
                        .suggests((ctx, builder) -> {
                            builder.suggest(hermesSubCommandTypeHelp);
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            String typeArgument = context.getArgument(subCommandTypeArgumentName, String.class);
                            if (typeArgument.equals(hermesSubCommandTypeHelp))
                                context.getSource().sendMessage(hermesCommandFeedback.getHelpCommandFeedback().getHelpCommandMessage());
                            else
                                context.getSource().sendMessage(hermesCommandFeedback.getMissingTypeArgumentComponent());
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build();

        return new BrigadierCommand(hermesHelpNode);
    }
}