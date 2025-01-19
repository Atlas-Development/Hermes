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
import dev.atlasmc.hermes.model.channel.Channel;
import dev.atlasmc.hermes.model.channel.ChannelManager;
import dev.atlasmc.hermes.model.config.messageConfig.commandFeedback.PrivateMessageReplyCommandFeedback;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;

/**
 * command for replying to private messages<br/>
 * opens a private message channel from executing audience to receiving audience and sets the receivers private message channel to allow replying<br/>
 * permission checks: {@link PermissionConstants#commandPermissionPrivateMessageReply}.
 */
public class PrivateMessageReplyCommand {
    public static final String commandName = "reply";
    public static final String messageArgumentName = "message";

    public static BrigadierCommand createBrigadierCommand(final ProxyServer proxy, final Logger logger, final PrivateMessageReplyCommandFeedback privateMessageReplyCommandFeedback,
                                                          final ChannelManager channelManager) {
        final LiteralCommandNode<CommandSource> replyNode = LiteralArgumentBuilder.<CommandSource>literal(commandName)
                .executes(context -> {
                    context.getSource().sendMessage(privateMessageReplyCommandFeedback.getMissingMessageArgumentComponent());
                    return Command.SINGLE_SUCCESS;
                })
                .then(RequiredArgumentBuilder.<CommandSource, String>argument(messageArgumentName, StringArgumentType.greedyString())
                        .executes(context -> {
                            final String messageArgument = context.getArgument(messageArgumentName, String.class);
                            //check permission
                            if (context.getSource() instanceof Player && !context.getSource().hasPermission(PermissionConstants.commandPermissionPrivateMessageReply)) {
                                context.getSource().sendMessage(privateMessageReplyCommandFeedback.getMissingPermissionComponent());
                                return Command.SINGLE_SUCCESS;
                            }

                            final Channel privateMessageChannel = channelManager.getPrivateMessageChannel(context.getSource());
                            if (privateMessageChannel == null)
                                context.getSource().sendMessage(privateMessageReplyCommandFeedback.getNoPartnerComponent());
                            else if (!privateMessageChannel.isReceiverAvailable(proxy)) {
                                if (privateMessageChannel.isReceiverOffline(proxy))
                                    context.getSource().sendMessage(privateMessageReplyCommandFeedback.getRecipientPlayerOfflineMessageComponent());
                                else
                                    context.getSource().sendMessage(privateMessageReplyCommandFeedback.getRecipientUnavailableMessageComponent());
                            } else
                                privateMessageChannel.sendMessage(Component.text(messageArgument));
                            return Command.SINGLE_SUCCESS;
                        }))
                .build();

        return new BrigadierCommand(replyNode);
    }
}
