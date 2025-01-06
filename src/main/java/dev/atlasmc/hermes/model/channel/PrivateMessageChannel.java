package dev.atlasmc.hermes.model.channel;

import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import dev.atlasmc.hermes.constant.MiniMessageCustomTagConstants;
import dev.atlasmc.hermes.helper.AudienceHelper;
import dev.atlasmc.hermes.model.config.HermesConfig;
import dev.atlasmc.hermes.model.config.MessageFormats;
import dev.atlasmc.hermes.model.config.runtime.PlayerConfiguration;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class PrivateMessageChannel extends Channel {
    private Audience senderAudience;
    private final Channel privateMessageGlobalChannel;
    private final Collection<PrivateMessageChannel> replyChannels;

    private final HermesConfig configuration;
    private final Map<Audience, PlayerConfiguration> playerConfigurations;

    public PrivateMessageChannel(final Audience senderAudience, final Audience receiverAudience, final Channel privateMessageGlobalChannel,
                                 final HermesConfig configuration, final Map<Audience, PlayerConfiguration> playerConfigurations) {
        super();
        this.senderAudience = senderAudience;
        this.configuration = configuration;
        this.receiverAudience = receiverAudience;
        this.privateMessageGlobalChannel = privateMessageGlobalChannel;
        this.playerConfigurations = playerConfigurations;
        this.replyChannels = new ArrayList<>();
    }

    /**
     * ignores excludedAudience as the target should only ever be a single audience and calls {@link #sendMessage(Component, Audience)}.
     *
     * @param message the message to send.
     * @return whether the message was sent successfully.
     */
    @Override
    public boolean sendMessage(final Component message, final Audience excludedAudience) {
        return this.sendMessage(message);
    }

    /**
     * sends a message in the formats specified in {@link MessageFormats} to the respective audiences specified in this object.<br/>
     * uses the following minimessage formats:
     * <table>
     *     <tr>
     *         <th>audience</th>
     *         <th>format</th>
     *     </tr>
     *     <tr>
     *         <td>{@link #senderAudience}</td>
     *         <td>{@link MessageFormats#getPrivateMessageSenderFormat()}</td>
     *     </tr>
     *     <tr>
     *         <td>{@link Channel#getReceiverAudience()}</td>
     *         <td>{@link MessageFormats#getPrivateMessageReceiverFormat()}</td>
     *     </tr>
     *     <tr>
     *         <td>{@link PrivateMessageChannel#getReceiverAudience()}</td>
     *         <td>{@link MessageFormats#getPrivateMessageThirdPartyFormat()}</td>
     *     </tr>
     * </table>
     *
     * @param message the message to send.
     * @return whether the message was sent successfully.
     */
    @Override
    public boolean sendMessage(final Component message) {
        final MiniMessage miniMessage = MiniMessage.miniMessage();
        final boolean senderIsPlayer = senderAudience instanceof Player;
        final boolean receiverIsPlayer = receiverAudience instanceof Player;
        final boolean senderIsConsole = senderAudience instanceof ConsoleCommandSource;
        final boolean receiverIsConsole = receiverAudience instanceof ConsoleCommandSource;
        final Optional<Collection<Component>> senderLpGroupsPrefix = AudienceHelper.getAudienceLpGroupPrefix(senderAudience, playerConfigurations);
        final Optional<Collection<Component>> receiverLpGroupsPrefix = AudienceHelper.getAudienceLpGroupPrefix(receiverAudience, playerConfigurations);
        final Optional<Component> senderLpPrimaryGroupPrefix = AudienceHelper.getAudienceLpPrimaryGroupPrefix(senderAudience, playerConfigurations);
        final Optional<Component> receiverLpPrimaryGroupPrefix = AudienceHelper.getAudienceLpPrimaryGroupPrefix(receiverAudience, playerConfigurations);

        //define custom tag replacements
        final TagResolver[] customTagResolver = new TagResolver[]{
                Placeholder.component(MiniMessageCustomTagConstants.sender, AudienceHelper.resolveAudienceNameComponent(senderAudience, configuration)),
                Placeholder.component(MiniMessageCustomTagConstants.receiver, AudienceHelper.resolveAudienceNameComponent(receiverAudience, configuration)),
                Placeholder.component(MiniMessageCustomTagConstants.message, message),
                Placeholder.component(MiniMessageCustomTagConstants.senderLpPrimaryGroupPrefix, senderLpPrimaryGroupPrefix.orElse(Component.empty())),
                Placeholder.component(MiniMessageCustomTagConstants.receiverLpPrimaryGroupPrefix, receiverLpPrimaryGroupPrefix.orElse(Component.empty())),
                Formatter.joining(MiniMessageCustomTagConstants.senderLpGroupsPrefix, senderLpGroupsPrefix.orElse(new ArrayList<>())),
                Formatter.joining(MiniMessageCustomTagConstants.receiverLpGroupsPrefix, receiverLpGroupsPrefix.orElse(new ArrayList<>())),
                Formatter.booleanChoice(MiniMessageCustomTagConstants.senderHasLpGroupPrefix, senderLpGroupsPrefix.isPresent()),
                Formatter.booleanChoice(MiniMessageCustomTagConstants.receiverHasLpGroupPrefix, receiverLpGroupsPrefix.isPresent()),
                Formatter.booleanChoice(MiniMessageCustomTagConstants.senderIsPlayer, senderIsPlayer),
                Formatter.booleanChoice(MiniMessageCustomTagConstants.receiverIsPlayer, receiverIsPlayer),
                Formatter.booleanChoice(MiniMessageCustomTagConstants.senderIsConsole, senderIsConsole),
                Formatter.booleanChoice(MiniMessageCustomTagConstants.receiverIsConsole, receiverIsConsole),
        };

        //send message to sender and receiver
        final Component senderMiniMessageComponent = miniMessage.deserialize(configuration.getMessageFormats().getPrivateMessageSenderFormat(), customTagResolver);
        AudienceHelper.sendMessageToAudienceSafe(senderAudience, senderMiniMessageComponent);
        final Component receiverMiniMessageComponent = miniMessage.deserialize(configuration.getMessageFormats().getPrivateMessageReceiverFormat(), customTagResolver);
        AudienceHelper.sendMessageToAudienceSafe(receiverAudience, receiverMiniMessageComponent);

        //Send message to global Channel
        final Component privateMessageGlobalMiniMessageComponent = miniMessage.deserialize(configuration.getMessageFormats().getPrivateMessageThirdPartyFormat(), customTagResolver);
        final Audience excludedAudience = Audience.audience(senderAudience, receiverAudience);
        AudienceHelper.sendMessageToAudienceSafe(privateMessageGlobalChannel.getReceiverAudience(), privateMessageGlobalMiniMessageComponent, excludedAudience);
        return true;
    }

    /**
     * adds a {@link PrivateMessageChannel} as reply channel.<br/>
     * this is necessary to ensure update propagation when audiences change.
     *
     * @param channel the channel to add.
     */
    public void addReplyChannel(final PrivateMessageChannel channel) {
        if (!replyChannels.contains(channel))
            replyChannels.add(channel);
    }

    /**
     * updates the receiver audience of this channel.
     * If this is called to update after setting a new sender on another channel, try to use {@link #updateAudience} on the sender channel instead.
     *
     * @param newAudience the new audience to register in place of the old one.
     */
    public void updateReceiverAudience(final Audience newAudience) {
        this.receiverAudience = newAudience;
    }

    /**
     * update the sending audience of this channel and calls {@link #updateReceiverAudience(Audience)} on associated reply channels.<br/>
     * note that the reply channels have to have been added via {@link #addReplyChannel(PrivateMessageChannel)}.
     *
     * @param newAudience the new audience to register in place of the old one.
     */
    public void updateAudience(final Audience newAudience) {
        this.senderAudience = newAudience;
        for (PrivateMessageChannel channel : this.replyChannels) {
            channel.updateReceiverAudience(newAudience);
        }
    }
}
