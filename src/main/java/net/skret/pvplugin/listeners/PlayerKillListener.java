package net.skret.pvplugin.listeners;

import net.milkbowl.vault.economy.Economy;
import net.skret.pvplugin.PvPlugin;
import net.skret.pvplugin.managers.PlayerManager;
import net.skret.pvplugin.models.PlayerWrapper;
import net.skret.pvplugin.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class PlayerKillListener implements Listener {

    private final PvPlugin plugin;
    private final PlayerManager playerManager;
    private final Economy economy;
    private final List<String> deathMessages;

    public PlayerKillListener(PvPlugin plugin, PlayerManager playerManager, Economy economy) {
        this.plugin = plugin;
        this.playerManager = playerManager;
        this.economy = economy;
        this.deathMessages = getMessages();
    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent event) {

        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        event.setDeathMessage("");

        if (killer == null) return;

        String deathMessage = deathMessages.get((int) (Math.random() * deathMessages.size()))
                .replace("{killer}", killer.getName())
                .replace("{victim}", victim.getName());
        Bukkit.broadcastMessage(Color.color(deathMessage));

        if (killer.getUniqueId().equals(victim.getUniqueId())) return;

        PlayerWrapper stats = playerManager.getStats(killer.getUniqueId());

        UUID lastKilled = playerManager.getLastKilled(killer.getUniqueId());

        if (lastKilled == null || lastKilled.equals(victim.getUniqueId())) return;

        playerManager.addKill(killer.getUniqueId());
        playerManager.setLastKilled(killer.getUniqueId(), victim.getUniqueId());

        economy.depositPlayer(killer, stats.getKillRank().getKillReward());

    }

    private List<String> getMessages() {
        try {
            Scanner scan = new Scanner(new File(plugin.getDataFolder().getAbsoluteFile() + "/death_messages.txt"));
            List<String> list = new ArrayList<>();
            while (scan.hasNextLine()){
                String message = scan.nextLine();
                if (message.length() == 0) continue;
                list.add(message);
            }
            scan.close();
            return list;
        } catch (FileNotFoundException exception) {
            plugin.getLogger().severe(Color.color("&4Missing kill messages file, please put it back :("));
        }
        return null;
    }

}
