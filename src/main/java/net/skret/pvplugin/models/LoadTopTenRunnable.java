package net.skret.pvplugin.models;

import net.skret.pvplugin.PvPlugin;
import net.skret.pvplugin.expansions.PvPluginExpansion;
import net.skret.pvplugin.managers.DatabaseManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class LoadTopTenRunnable extends BukkitRunnable {

    private final PvPlugin plugin;
    private final PvPluginExpansion expansion;
    private final Connection connection;
    private final DatabaseManager databaseManager;

    public LoadTopTenRunnable(PvPlugin plugin, PvPluginExpansion expansion, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.expansion = expansion;
        this.connection = databaseManager.getConnection();
        this.databaseManager = databaseManager;
    }

    @Override
    public void run() {
        loadTopTenSeason();
        loadTopTenLifetime();
    }

    private void loadTopTenSeason() {
        try {
            databaseManager.saveAllData();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT UUID, Username, SeasonKills FROM player_kills ORDER BY SeasonKills DESC LIMIT 10;"
            );
            ResultSet resultSet = statement.executeQuery();
            expansion.getTopTenSeason().clear();
            while (resultSet.next()) {
                if (resultSet.getInt("SeasonKills") == 0) continue;
                expansion.getTopTenSeason().add(new PlayerWrapper(
                                expansion.getPlayerManager(),
                                UUID.fromString(resultSet.getString("UUID")),
                                resultSet.getString("Username"),
                                resultSet.getInt("SeasonKills"),
                                0
                        )
                );
            }

        } catch (SQLException exception){
            exception.printStackTrace();
        }
    }

    private void loadTopTenLifetime() {
        try {
            databaseManager.saveAllData();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT UUID, Username, LifetimeKills FROM player_kills ORDER BY LifetimeKills DESC LIMIT 10;"
            );
            ResultSet resultSet = statement.executeQuery();
            expansion.getTopTenLifetime().clear();
            while (resultSet.next()) {
                if (resultSet.getInt("LifetimeKills") == 0) continue;
                expansion.getTopTenLifetime().add(new PlayerWrapper(
                                expansion.getPlayerManager(),
                                UUID.fromString(resultSet.getString("UUID")),
                                resultSet.getString("Username"),
                                0,
                                resultSet.getInt("LifetimeKills")
                        )
                );
            }

        } catch (SQLException exception){
            exception.printStackTrace();
        }
    }

    public void start() {
        runTaskTimerAsynchronously(plugin, 20L, 20L * 25L);
    }
}
