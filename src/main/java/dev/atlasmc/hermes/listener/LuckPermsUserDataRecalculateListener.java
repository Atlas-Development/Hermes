package dev.atlasmc.hermes.listener;

import dev.atlasmc.hermes.model.config.runtime.PlayerConfiguration;
import net.kyori.adventure.audience.Audience;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import net.luckperms.api.model.user.User;

import java.util.Map;

public class LuckPermsUserDataRecalculateListener {
    public static void onLuckPermsUserDataRecalculate(final UserDataRecalculateEvent event, final Map<Audience, PlayerConfiguration> playerConfigurations) {
        final User user = event.getUser();
        for (PlayerConfiguration playerConfiguration : playerConfigurations.values()) {
            if (playerConfiguration.getPlayerUUID() != user.getUniqueId())
                continue;
            playerConfiguration.updateLpData();
        }
    }
}
