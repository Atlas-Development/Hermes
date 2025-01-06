package dev.atlasmc.hermes.model.channel;

import com.velocitypowered.api.proxy.Player;
import dev.atlasmc.hermes.constant.NamedChannelType;
import dev.atlasmc.hermes.constant.PermissionConstants;
import dev.atlasmc.hermes.helper.AudienceHelper;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import java.util.function.Function;

public class NamedChannel extends Channel {
    /**
     * the channel type used for permission checks.<br/>
     * {@link NamedChannelType#OTHER} causes no additional checks.
     */
    private final NamedChannelType channelType;

    /**
     * the name used to identify this named channel.
     */
    @Getter
    @Setter
    private String name;

    public NamedChannel(final NamedChannelType channelType, final String name) {
        super();
        this.channelType = channelType;
        this.name = name;
    }

    /**
     * adds an audience to {@link #receiverAudience}.
     *
     * @param audience the audience to add.
     */
    public void addAudience(final Audience audience) {
        this.receiverAudience = Audience.audience(this.receiverAudience, audience);
    }

    /**
     * removes an audience from {@link #receiverAudience}.
     *
     * @param audience the audience to remove.
     */
    public void removeAudience(final Audience audience) {
        this.receiverAudience = this.receiverAudience.filterAudience(containedAudience -> {
            return containedAudience != audience;
        });
    }

    /**
     * send message to the channel audience while avoiding sending a message to the same receiver multiple times.
     *
     * @return whether the action was completed successfully.
     */
    @Override
    public boolean sendMessage(final Component message) {
        return sendMessage(message, Audience.empty());
    }

    /**
     * send message to the channel audience while avoiding sending a message to the same receiver multiple times and excluding a given audience.
     *
     * @return whether the action was completed successfully.
     */
    @Override
    public boolean sendMessage(final Component message, final Audience excludedAudiences) {
        //use permission check condition depending on the type of channel
        final Function<Audience, Boolean> permissionCheck = switch (this.channelType) {
            case SERVER_GLOBAL -> (a) -> !(a instanceof Player) ||
                    ((Player) a).hasPermission(PermissionConstants.chatReceiveServerGlobalMessage);
            case SERVER -> (a) -> !(a instanceof Player) ||
                    ((Player) a).hasPermission(PermissionConstants.getChatReceiveServerMessage(this.name));
            default -> (a) -> true;
        };

        AudienceHelper.sendMessageToAudienceSafe(this.receiverAudience, message, excludedAudiences, permissionCheck);
        return true;
    }
}
