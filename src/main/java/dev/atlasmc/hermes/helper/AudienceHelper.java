package dev.atlasmc.hermes.helper;

import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import dev.atlasmc.hermes.model.config.HermesConfig;
import dev.atlasmc.hermes.model.config.runtime.PlayerConfiguration;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class AudienceHelper {
    /**
     * Resolve the name to use for a given audience.
     *
     * @param audience      the audience to try to find a name for.
     * @param configuration the configuration to get audience names from.
     * @return the resolved name component.
     */
    public static Component resolveAudienceNameComponent(final Audience audience, final HermesConfig configuration) {
        if (audience instanceof Player) {
            return Component.text(((Player) audience).getUsername());
        } else if (audience instanceof ConsoleCommandSource) {
            return MiniMessage.miniMessage().deserialize(configuration.getConsoleName());
        } else {
            return MiniMessage.miniMessage().deserialize(configuration.getUnknownAudienceFallbackName());
        }
    }

    /**
     * Sends a message to the audience tree while avoiding sending a message to the same receiver multiple times.
     *
     * @param receiverAudience the audience to send the message to.
     * @param message          the message component to send.
     */
    public static void sendMessageToAudienceSafe(final Audience receiverAudience, final Component message) {
        sendMessageToAudienceSafe(receiverAudience, message, null);
    }

    /**
     * Sends a message to the audience tree while avoiding sending a message to the same receiver multiple times and excluding an audience.
     *
     * @param receiverAudience the audience to send the message to.
     * @param message          the message component to send.
     * @param excludedAudience the audience to exclude even if it was contained in the specified receiver audiences.
     */
    public static void sendMessageToAudienceSafe(final Audience receiverAudience, final Component message, final Audience excludedAudience) {
        sendMessageToAudienceSafe(receiverAudience, message, excludedAudience, (a) -> true);
    }

    /**
     * Sends a message to the audience tree while avoiding sending a message to the same receiver multiple times, excluding an audience and checking an additional condition.
     *
     * @param receiverAudience            the audience to send the message to.
     * @param message                     the message component to send.
     * @param excludedAudience            the audience to exclude even if it was contained in the specified receiver audiences.
     * @param audienceCanReceiveCondition the additional condition to pass for each audience for it to receive the message
     */
    public static void sendMessageToAudienceSafe(final Audience receiverAudience, final Component message,
                                                 final Audience excludedAudience, Function<Audience, Boolean> audienceCanReceiveCondition) {
        final ArrayList<Audience> handledAudiences = new ArrayList<Audience>();
        //exclude audiences
        if (excludedAudience != null) {
            excludedAudience.forEachAudience(subAudience -> {
                if (handledAudiences.contains(subAudience))
                    return;
                handledAudiences.add(subAudience);
            });
        }
        //send to remaining audiences
        receiverAudience.forEachAudience(subAudience -> {
            try {
                if (handledAudiences.contains(subAudience) || !audienceCanReceiveCondition.apply(subAudience))
                    return;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            subAudience.sendMessage(message);
            handledAudiences.add(subAudience);
        });
    }

    /**
     * check if an audience contains another specified audience.
     *
     * @param superAudience the audience to search in.
     * @param subAudience   the audience to search.
     * @return whether the audience was found.
     */
    public static boolean CheckAudienceContainsAudience(final Audience superAudience, final Audience subAudience) {
        return superAudience.filterAudience(audience -> audience == subAudience) != Audience.empty();
    }

    /**
     * Get a Collection of formatted LuckPerms groups of an audience.
     *
     * @param audience             the audience to get the LuckPerms groups for.
     * @param playerConfigurations the configurations to try to get the collection from.
     * @return an Optional that contains the collection if it was found.
     */
    public static Optional<Collection<Component>> getAudienceLpGroupPrefix(final Audience audience, final Map<Audience, PlayerConfiguration> playerConfigurations) {
        if (!(audience instanceof Player))
            return Optional.empty();
        final PlayerConfiguration configuration = playerConfigurations.get(audience);
        return Optional.of(configuration.getLpGroupPrefixes());
    }

    /**
     * Get the formatted primary LuckPerms group of an audience.
     *
     * @param audience             the audience to get the LuckPerms groups for.
     * @param playerConfigurations the configurations to try to get the Component from.
     * @return an Optional that contains the Component if it was found.
     */
    public static Optional<Component> getAudienceLpPrimaryGroupPrefix(final Audience audience, final Map<Audience, PlayerConfiguration> playerConfigurations) {
        if (!(audience instanceof Player))
            return Optional.empty();
        final PlayerConfiguration configuration = playerConfigurations.get(audience);
        return Optional.of(configuration.getPrimaryGroupPrefix());
    }
}
