package net.skret.pvplugin;

import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.economy.Economy;
import net.skret.pvplugin.commands.DonationCommand;
import net.skret.pvplugin.commands.KillsCommand;
import net.skret.pvplugin.expansions.PvPluginExpansion;
import net.skret.pvplugin.listeners.*;
import net.skret.pvplugin.managers.ConfigManager;
import net.skret.pvplugin.managers.DatabaseManager;
import net.skret.pvplugin.managers.PlayerManager;
import net.skret.pvplugin.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.time.LocalDate;

public final class PvPlugin extends JavaPlugin {

    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {

        LuckPerms luckPerms = null;

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPerms = provider.getProvider();
        }

        Economy economy = getServer().getServicesManager().getRegistration(Economy.class).getProvider();

        ConfigManager configManager = new ConfigManager(this);
        PlayerManager playerManager = new PlayerManager(luckPerms, this, configManager.getConfig());
        new UserChangeRankListener(this, luckPerms, playerManager);
        databaseManager = null;
        try {
            databaseManager = new DatabaseManager(playerManager);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        new KillsCommand(this, playerManager, databaseManager);

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(playerManager, databaseManager), this);
        Bukkit.getPluginManager().registerEvents(new PlayerKillListener(this, playerManager, economy), this);
        Bukkit.getPluginManager().registerEvents(new PlayerLeaveListener(playerManager, databaseManager), this);
        Bukkit.getPluginManager().registerEvents(new PlayerChatListener(playerManager), this);

        getCommand("donated").setExecutor(new DonationCommand());

        new PvPluginExpansion(playerManager, databaseManager).register();


        /*if (isFirstOfEveryThirdMonth()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    databaseManager.resetAllSeasonKills();
                }
            }.runTaskAsynchronously(this);
            getLogger().info("");
            getLogger().info("");
            getLogger().info(Color.color("&aToday the season ends, resetting all season kills!"));
            getLogger().info("");
            getLogger().info("");
        }*/

    }

    @Override
    public void onDisable() {
        try {
            databaseManager.closeConnection();
        } catch (SQLException ignored) {}
    }

    private boolean isFirstOfEveryThirdMonth() {
        LocalDate date = LocalDate.now();
        return date.getDayOfMonth() == 1 && date.getMonthValue() % 6 == 0;
    }
}
