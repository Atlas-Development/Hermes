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
    private final Collection<Component> lpGroupPrefixes;

    @Getter
    private final Component primaryGroupPrefix;

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
        playerUUID = ((Player) audience).getUniqueId();
    }

    public PlayerConfiguration(final Player player, final LuckPerms luckPerms, final ProxyServer proxy,
                               final MiscellaneousMessages miscellaneous) {
        this.playerUUID = player.getUniqueId();
        this.miscellaneous = miscellaneous;
        this.proxy = proxy;
        this.luckPerms = luckPerms;
        setCurrentAudience(player);
        lpGroupPrefixes = updateLpUserPrefixes(proxy, luckPerms);
        primaryGroupPrefix = updateLpUserPrimaryGroupPrefix(proxy, luckPerms);
        privateMessageChannelReceiverUUID = Optional.empty();
        addReceivingNamedChannelName(ChannelConstants.ServerGlobalChannelName);
        isServerGlobalChannelSender = false;
    }

    public void updateLpData() {
        updateLpUserPrefixes(proxy, luckPerms);
        updateLpUserPrimaryGroupPrefix(proxy, luckPerms);
    }

    public Collection<Component> updateLpUserPrefixes(final ProxyServer proxyServer, final LuckPerms luckPerms) {
        final Optional<Player> player = proxyServer.getPlayer(getPlayerUUID());
        return player.map(value -> LuckPermsHelper.GetGroupPrefixes(luckPerms, value, miscellaneous))
                .orElseGet(() -> new ArrayList<>(List.of(Component.text("?"))));
    }

    public Component updateLpUserPrimaryGroupPrefix(final ProxyServer proxyServer, final LuckPerms luckPerms) {
        final Optional<Player> player = proxyServer.getPlayer(getPlayerUUID());
        if (player.isEmpty())
            return Component.text("?");
        return LuckPermsHelper.getPrimaryGroupPrefix(luckPerms, player.get(), miscellaneous);
    }
}
