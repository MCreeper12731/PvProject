package net.skret.pvplugin.listeners;

import lombok.RequiredArgsConstructor;
import net.skret.pvplugin.managers.DatabaseManager;
import net.skret.pvplugin.managers.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class PlayerLeaveListener implements Listener {

    private final PlayerManager playerManager;
    private final DatabaseManager databaseManager;

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {

        databaseManager.saveData(event.getPlayer().getUniqueId());

    }

}
