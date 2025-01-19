package dev.atlasmc.hermes.model.config.runtime;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.atlasmc.hermes.constant.ChannelConstants;
import dev.atlasmc.hermes.helper.LuckPermsHelper;
import dev.atlasmc.hermes.model.config.messageConfig.MiscellaneousMessages;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;

import java.util.*;

public class PlayerConfiguration extends ChannelConfiguration {
    @Getter
    private UUID playerUUID;

    @Getter
    private Collection<Component> lpGroupPrefixes;

    @Getter
    private Component primaryGroupPrefix;

    @Getter
    private final MiscellaneousMessages miscellaneous;

    @Getter
    @Setter
    private Optional<UUID> privateMessageChannelReceiverUUID;

    @Getter
    @Setter
    private boolean isServerGlobalChannelSender;

    @Getter
    private final ProxyServer proxy;

    @Getter
    private final LuckPerms luckPerms;

    @Override
    public void setCurrentAudience(Audience audience) {
        super.setCurrentAudience(audience);
        this.playerUUID = ((Player) audience).getUniqueId();
    }

    public PlayerConfiguration(final Player player, final LuckPerms luckPerms, final ProxyServer proxy,
                               final MiscellaneousMessages miscellaneousMessages) {
        this.playerUUID = player.getUniqueId();
        this.miscellaneous = miscellaneousMessages;
        this.proxy = proxy;
        this.luckPerms = luckPerms;
        this.setCurrentAudience(player);
        this.lpGroupPrefixes = LuckPermsHelper.GetGroupPrefixes(luckPerms, player, miscellaneousMessages);
        this.primaryGroupPrefix = LuckPermsHelper.getPrimaryGroupPrefix(luckPerms, player, miscellaneousMessages);
        this.privateMessageChannelReceiverUUID = Optional.empty();
        this.addReceivingNamedChannelName(ChannelConstants.ServerGlobalChannelName);
        this.isServerGlobalChannelSender = false;
    }

    public void updateLpData() {
        final Optional<Player> player = this.proxy.getPlayer(getPlayerUUID());
        if(player.isEmpty())
            return;
        this.lpGroupPrefixes = LuckPermsHelper.GetGroupPrefixes(luckPerms, player.get(), miscellaneous);
        this.primaryGroupPrefix = LuckPermsHelper.getPrimaryGroupPrefix(luckPerms, player.get(), miscellaneous);
    }
}
