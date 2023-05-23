package net.skret.pvplugin.managers;

import lombok.Getter;
import net.skret.pvplugin.models.KillType;
import net.skret.pvplugin.models.PlayerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.List;
import java.util.UUID;

public class DatabaseManager {

    @Getter
    private final Connection connection;
    private final PlayerManager playerManager;

    public DatabaseManager(PlayerManager playerManager) throws SQLException {
        this.playerManager = playerManager;
        this.connection = DriverManager.getConnection(
                "jdbc:mysql://customer_383907_highscores:ApK6Lu$1A9o$6pgrepLa@na01-sql.pebblehost.com/customer_383907_highscores");
        testDataSource();
    }

    private void testDataSource() throws SQLException {
        if (!connection.isValid(1)) {
            throw new SQLException("Could not establish database connection.");
        }
    }

    public ResultSet getData(UUID uuid) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM player_kills WHERE UUID=?;"
            );
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            return resultSet;
        } catch (SQLException exception){
            exception.printStackTrace();
        }
        return null;
    }

    public ResultSet getData(String username) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM player_kills WHERE UPPER(Username) = UPPER(?);"
            );
            statement.setString(1, username);
            return statement.executeQuery();
        } catch (SQLException exception){
            exception.printStackTrace();
        }
        return null;
    }

    public void loadData(UUID playerId, String username) {
        try {
            ResultSet resultSet = getData(playerId);
            if (resultSet.next()) {
                playerManager.init(
                        UUID.fromString(resultSet.getString("UUID")),
                        resultSet.getString("Username"),
                        resultSet.getInt("SeasonKills"),
                        resultSet.getInt("LifetimeKills")
                );
                return;
            }
            playerManager.init(playerId, username);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO player_kills (UUID, SeasonKills, LifetimeKills, Username) VALUES (?, 0, 0, ?)"
            );
            preparedStatement.setString(1, playerId.toString());
            preparedStatement.setString(2, username);
            preparedStatement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void saveData(UUID playerId) {
        try {
            PlayerWrapper stats = playerManager.getStats(playerId);
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE player_kills SET SeasonKills = ?, LifetimeKills = ? WHERE UUID = ?;"
            );
            statement.setInt(1, stats.getSeasonKills());
            statement.setInt(2, stats.getLifetimeKills());
            statement.setString(3, stats.getId().toString());
            statement.execute();
            if (Bukkit.getPlayerExact(stats.getUsername()) == null) playerManager.remove(stats.getId());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void saveAllData() {
        playerManager.getStats().forEach(stats -> saveData(stats.getId()));
    }

    public void resetAllSeasonKills() {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE player_kills SET SeasonKills = 0"
            );
            statement.execute();
            List<PlayerWrapper> allStats = playerManager.getStats();
            allStats.forEach(stat -> stat.setSeasonKills(0));
            saveAllData();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void overwriteKills(String username, KillType type, int kills) {
        try {
            switch (type) {
                case SEASON -> {
                    PreparedStatement statement = connection.prepareStatement(
                            "UPDATE player_kills SET SeasonKills = ? WHERE UPPER(Username) = UPPER(?);"
                    );
                    statement.setInt(1, kills);
                    statement.setString(2, username);
                    statement.execute();
                }
                case LIFETIME -> {
                    PreparedStatement statement = connection.prepareStatement(
                            "UPDATE player_kills SET LifetimeKills = ? WHERE UPPER(Username) = UPPER(?);"
                    );
                    statement.setInt(1, kills);
                    statement.setString(2, username);
                    statement.execute();
                }
                case ALL -> {
                    PreparedStatement statement = connection.prepareStatement(
                            "UPDATE player_kills SET SeasonKills = ?, LifetimeKills = ? WHERE UPPER(Username) = UPPER(?);"
                    );
                    statement.setInt(1, kills);
                    statement.setInt(2, kills);
                    statement.setString(3, username);
                    statement.execute();
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void addKills(String username, int kills) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE player_kills SET SeasonKills = SeasonKills + ?, LifetimeKills = LifetimeKills + ? WHERE UPPER(Username) = UPPER(?);"
            );
            statement.setInt(1, kills);
            statement.setInt(2, kills);
            statement.setString(3, username);
            statement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void closeConnection() throws SQLException {
        this.connection.close();
    }

}
