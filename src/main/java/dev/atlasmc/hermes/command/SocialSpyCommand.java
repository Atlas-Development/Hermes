package dev.atlasmc.hermes.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import dev.atlasmc.hermes.constant.PermissionConstants;
import dev.atlasmc.hermes.model.channel.ChannelManager;
import dev.atlasmc.hermes.model.config.messageConfig.commandFeedback.SocialSpyCommandFeedback;

/**
 * command for viewing private messages of other users<br/>
 * adds the user to the {@link ChannelManager#getPrivateMessageGlobalChannel()}<br/>
 * permission checks: {@link PermissionConstants#commandPermissionSocialSpy}.
 */
public class SocialSpyCommand {
    public static final String commandName = "socialspy";
    public static final String stateArgumentName = "state";

    public static BrigadierCommand createBrigadierCommand(final ChannelManager channelManager,
                                                          final SocialSpyCommandFeedback socialSpyCommandFeedback) {
        final LiteralCommandNode<CommandSource> socialSpyNode = LiteralArgumentBuilder.<CommandSource>literal(commandName)
                .executes(context -> {
                    context.getSource().sendMessage(socialSpyCommandFeedback.getMissingStateArgumentComponent());
                    return Command.SINGLE_SUCCESS;
                })
                .then(RequiredArgumentBuilder.<CommandSource, String>argument(stateArgumentName, StringArgumentType.word())
                        .suggests((ctx, builder) -> {
                            builder.suggest("true");
                            builder.suggest("false");
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            String stateArgumentString = context.getArgument(stateArgumentName, String.class);
//                            Boolean stateArgument = stateArgumentString.equalsIgnoreCase("true")?true:
//                                    stateArgumentString.equalsIgnoreCase("false")?false:11;
                            boolean stateArgument;
                            if(stateArgumentString.equalsIgnoreCase("true")) {
                                stateArgument = true;
                            } else if (stateArgumentString.equalsIgnoreCase("false")) {
                                stateArgument = false;
                            } else {
                                context.getSource().sendMessage(socialSpyCommandFeedback.getInvalidStateArgumentComponent());
                                return Command.SINGLE_SUCCESS;
                            }

                            //permission check
                            if (context.getSource() instanceof Player && !context.getSource().hasPermission(PermissionConstants.commandPermissionSocialSpy)) {
                                context.getSource().sendMessage(socialSpyCommandFeedback.getMissingPermissionComponent());
                                return Command.SINGLE_SUCCESS;
                            }
                            if (stateArgument) {
                                channelManager.getPrivateMessageGlobalChannel().addAudience(context.getSource());
                                context.getSource().sendMessage(socialSpyCommandFeedback.getActivatedMessageComponent());
                            } else {
                                channelManager.getPrivateMessageGlobalChannel().removeAudience(context.getSource());
                                context.getSource().sendMessage(socialSpyCommandFeedback.getDeactivatedMessageComponent());
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build();
        return new BrigadierCommand(socialSpyNode);
    }
}
