package dev.atlasmc.hermes.helper;

import com.velocitypowered.api.proxy.Player;
import dev.atlasmc.hermes.constant.MiniMessageCustomTagConstants;
import dev.atlasmc.hermes.model.config.messageConfig.MiscellaneousMessages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.platform.PlayerAdapter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * helper class for LuckPerms related problems.
 */
public class LuckPermsHelper {
    /**
     * gets a players primary luckperms group and uses it to build a parsed component.
     * uses the MiniMessage format {@link MiscellaneousMessages#getLuckPermsPrimaryGroupTagFormat()}.<br/>
     * replaces custom MiniMessage tag {@value MiniMessageCustomTagConstants#lpGroupConfigDisplayName} and {@value MiniMessageCustomTagConstants#lpGroups}.
     *
     * @param luckPerms      the luckPerms instance to use.
     * @param player         the player to get the group prefixes from.
     * @param messageFormats the configuration to use for getting the MiniMessage string.
     * @return a parsed MiniMessage component.
     */
    public static Component getPrimaryGroupPrefix(final LuckPerms luckPerms, final Player player,
                                                  final MiscellaneousMessages messageFormats) {
        final MiniMessage miniMessage = MiniMessage.miniMessage();
        final PlayerAdapter<Player> playerAdapter = luckPerms.getPlayerAdapter(Player.class);
        final String primaryGroupName = playerAdapter.getUser(player).getPrimaryGroup();
        final Group primaryGroup = luckPerms.getGroupManager().getGroup(primaryGroupName);
        final String groupPrefix = primaryGroup.getCachedData().getMetaData().getPrefix() == null ? "?" : primaryGroup.getCachedData().getMetaData().getPrefix();
        final String groupDisplayName = primaryGroup.getDisplayName() == null ? "?" : primaryGroup.getDisplayName();

        return miniMessage.deserialize(messageFormats.getLuckPermsPrimaryGroupTagFormat(),
                Placeholder.parsed(MiniMessageCustomTagConstants.lpGroupConfigDisplayName, groupDisplayName),
                Placeholder.parsed(MiniMessageCustomTagConstants.lpGroupConfigPrefix, groupPrefix));
    }

    /**
     * gets a players luckperms groups and uses them to build a collection of parsed components.
     * uses the MiniMessage format {@link MiscellaneousMessages#getLuckPermsGroupTagFormat()}.<br/>
     * replaces custom MiniMessage tag {@value MiniMessageCustomTagConstants#lpGroupConfigDisplayName} and {@value MiniMessageCustomTagConstants#lpGroupConfigPrefix}.
     *
     * @param luckPerms      the luckPerms instance to use.
     * @param player         the player to get the group prefixes from.
     * @param miscellaneous the configuration to use for getting the MiniMessage string.
     * @return a Collection of the parsed MiniMessage components.
     */
    public static Collection<Component> GetGroupPrefixes(final LuckPerms luckPerms, final Player player,
                                                         final MiscellaneousMessages miscellaneous) {
        final MiniMessage miniMessage = MiniMessage.miniMessage();
        final Collection<Component> groupPrefixComponents = new ArrayList<>();
        final PlayerAdapter<Player> playerAdapter = luckPerms.getPlayerAdapter(Player.class);
        final Collection<Group> playerGroups = playerAdapter.getUser(player).getInheritedGroups(playerAdapter.getQueryOptions(player));

        for (final Group playerGroup : playerGroups) {
            final String groupPrefix = playerGroup.getCachedData().getMetaData().getPrefix() == null ? "?" : playerGroup.getCachedData().getMetaData().getPrefix();
            final String groupDisplayName = playerGroup.getDisplayName() == null ? "?" : playerGroup.getDisplayName();

            final Component groupComponent = miniMessage.deserialize(miscellaneous.getLuckPermsGroupTagFormat(),
                    Placeholder.parsed(MiniMessageCustomTagConstants.lpGroupConfigDisplayName, groupDisplayName),
                    Placeholder.parsed(MiniMessageCustomTagConstants.lpGroupConfigPrefix, groupPrefix));
            groupPrefixComponents.add(groupComponent);
        }
        return groupPrefixComponents;
    }
}
