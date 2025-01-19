package dev.atlasmc.hermes.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.atlasmc.hermes.constant.PermissionConstants;
import dev.atlasmc.hermes.model.channel.ChannelManager;
import dev.atlasmc.hermes.model.channel.PrivateMessageChannel;
import dev.atlasmc.hermes.model.config.messageConfig.commandFeedback.PrivateMessageCommandFeedback;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;

import java.util.*;

/**
 * command for sending private messages to players<br/>
 * opens a private message channel from executing audience to receiving audience and sets the receivers private message channel to allow replying<br/>
 * permission checks: {@link PermissionConstants#commandPermissionPrivateMessageInitiate}.
 */
public class PrivateMessageCommand {
    public static final String commandName = "message";
    public static final String receiverArgumentName = "receiver";
    public static final String messageArgumentName = "message";

    public static BrigadierCommand createBrigadierCommand(final ProxyServer proxy,
                                                          final ChannelManager channelManager,
                                                          final PrivateMessageCommandFeedback privateMessageCommandFeedback) {
        final LiteralCommandNode<CommandSource> messageNode = LiteralArgumentBuilder.<CommandSource>literal(commandName)
                .executes(context -> {
                    context.getSource().sendMessage(privateMessageCommandFeedback.getMissingReceiverArgumentComponent());
                    return Command.SINGLE_SUCCESS;
                })
                .then(RequiredArgumentBuilder.<CommandSource, String>argument(receiverArgumentName, StringArgumentType.word())
                        .suggests((ctx, builder) -> {
                            final Collection<Player> allPlayers = proxy.getAllPlayers();

                            //try to find players with matching username start
                            try {
                                for (final Player player : allPlayers) {
                                    final String playerName = player.getUsername();
                                    if (playerName.startsWith(ctx.getArgument(receiverArgumentName, String.class)))
                                        builder.suggest(playerName);
                                }
                            } catch (final IllegalArgumentException e) {
                                for (final Player player : allPlayers)
                                    builder.suggest(player.getUsername());
                            }

                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            context.getSource().sendMessage(privateMessageCommandFeedback.getMissingMessageArgumentComponent());
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(RequiredArgumentBuilder.<CommandSource, String>argument(messageArgumentName, StringArgumentType.greedyString())
                                .executes(context -> {
                                    //permission check
                                    if (context.getSource() instanceof Player && !context.getSource().hasPermission(PermissionConstants.commandPermissionPrivateMessageInitiate)) {
                                        context.getSource().sendMessage(privateMessageCommandFeedback.getMissingPermissionComponent());
                                        return Command.SINGLE_SUCCESS;
                                    }

                                    final String messageArgument = context.getArgument(messageArgumentName, String.class);
                                    final String receiverArgument = context.getArgument(receiverArgumentName, String.class);

                                    final Optional<Player> receiver = proxy.getPlayer(receiverArgument);
                                    if (receiver.isEmpty()) {
                                        context.getSource().sendMessage(privateMessageCommandFeedback.getRecipientUnavailableMessageComponent());
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    PrivateMessageChannel privateMessageChannel = channelManager.setPrivateMessageChannel(context.getSource(), receiver.get());
                                    privateMessageChannel.sendMessage(Component.text(messageArgument));
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                ).build();
        return new BrigadierCommand(messageNode);
    }
}
