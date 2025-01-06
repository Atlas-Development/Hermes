package dev.atlasmc.hermes.model.channel;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

public abstract class Channel {
    @Getter
    protected Audience receiverAudience;

    public Channel() {
        receiverAudience = Audience.empty();
    }

    /**
     * send message to the channel audience.
     *
     * @return whether the action was completed successfully.
     */
    public abstract boolean sendMessage(final Component message);

    /**
     * send message to the channel audience and excluding a given audience.
     *
     * @return whether the action was completed successfully.
     */
    public abstract boolean sendMessage(final Component message, final Audience excludedAudiences);

    public boolean isAudienceEmpty() {
        return receiverAudience == Audience.empty();
    }

    /**
     * check if the receiver audience is able to receive messages.
     *
     * @param proxy the proxy server to perform checks with.
     * @return whether the receiver can receive messages.
     */
    public boolean isReceiverAvailable(final ProxyServer proxy) {
        return receiverAudience.filterAudience(a -> {
            if (a instanceof Player)
                return proxy.getPlayer(((Player) a).getUniqueId()).isPresent();
            else //assume that other types of audiences are always available
                return true;
        }) != Audience.empty();
    }

    /**
     * Check if the receiver audience is completely offline.
     *
     * @return whether all receiver audiences are offline.
     */
    public boolean isReceiverOffline(final ProxyServer proxy) {
        return receiverAudience.filterAudience(a -> {
            if (a instanceof Player)
                return proxy.getPlayer(((Player) a).getUniqueId()).isPresent();
            else //assume that other types of audiences are always online
                return true;
        }) != Audience.empty();
    }
}
