package dev.atlasmc.hermes.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import dev.atlasmc.hermes.constant.PermissionConstants;
import dev.atlasmc.hermes.model.channel.ChannelManager;
import dev.atlasmc.hermes.model.config.CommandFeedbackMessages;

/**
 * command for viewing private messages of other users<br/>
 * adds the user to the {@link ChannelManager#getPrivateMessageGlobalChannel()}<br/>
 * permission checks: {@link PermissionConstants#commandPermissionSocialSpy}.
 */
public class SocialSpyCommand {
    public static final String commandName = "socialspy";
    public static final String stateArgumentName = "state";

    public static BrigadierCommand createBrigadierCommand(final ChannelManager channelManager, final CommandFeedbackMessages commandFeedbackMessages) {
        final LiteralCommandNode<CommandSource> socialSpyNode = LiteralArgumentBuilder.<CommandSource>literal(commandName)
                .executes(context -> {
                    context.getSource().sendMessage(commandFeedbackMessages.getSocialSpyMissingStateArgumentComponent());
                    return Command.SINGLE_SUCCESS;
                })
                .then(RequiredArgumentBuilder.<CommandSource, Boolean>argument(stateArgumentName, BoolArgumentType.bool())
                        .executes(context -> {
                            Boolean stateArgument = context.getArgument(stateArgumentName, Boolean.class);
                            //permission check
                            if (context.getSource() instanceof Player && !context.getSource().hasPermission(PermissionConstants.commandPermissionSocialSpy)) {
                                context.getSource().sendMessage(commandFeedbackMessages.getSocialSpyMissingPermissionComponent());
                                return Command.SINGLE_SUCCESS;
                            }
                            if (stateArgument) {
                                channelManager.getPrivateMessageGlobalChannel().addAudience(context.getSource());
                                context.getSource().sendMessage(commandFeedbackMessages.getSocialSpyActivatedMessageComponent());
                            } else {
                                channelManager.getPrivateMessageGlobalChannel().removeAudience(context.getSource());
                                context.getSource().sendMessage(commandFeedbackMessages.getSocialSpyDeactivatedMessageComponent());
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build();
        return new BrigadierCommand(socialSpyNode);
    }
}
