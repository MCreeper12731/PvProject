package net.skret.pvplugin.listeners;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedDataManager;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import net.luckperms.api.event.user.track.UserPromoteEvent;
import net.skret.pvplugin.PvPlugin;
import net.skret.pvplugin.managers.PlayerManager;
import org.checkerframework.checker.units.qual.A;

public class UserChangeRankListener {

    private final PvPlugin plugin;
    private final PlayerManager playerManager;

    public UserChangeRankListener(PvPlugin plugin, LuckPerms luckPerms, PlayerManager playerManager) {
        this.plugin = plugin;
        this.playerManager = playerManager;

        EventBus eventBus = luckPerms.getEventBus();

        eventBus.subscribe(this.plugin, UserDataRecalculateEvent.class, this::onUserChangeGroup);
    }

    private void onUserChangeGroup(UserDataRecalculateEvent event) {

        //plugin.getLogger().severe("Testing who sent the command: " + event.getUser().getUsername());

        //playerManager.updateColor(event.getUser().getUniqueId());

    }
}
