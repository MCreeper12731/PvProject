package net.skret.pvplugin.expansions;

import lombok.Getter;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.skret.pvplugin.managers.DatabaseManager;
import net.skret.pvplugin.managers.PlayerManager;
import net.skret.pvplugin.models.KillType;
import net.skret.pvplugin.models.LoadTopTenRunnable;
import net.skret.pvplugin.models.PlayerWrapper;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PvPluginExpansion extends PlaceholderExpansion {

    @Getter
    private final PlayerManager playerManager;
    @Getter
    private final List<PlayerWrapper> topTenSeason;
    @Getter
    private final List<PlayerWrapper> topTenLifetime;

    public PvPluginExpansion(PlayerManager playerManager, DatabaseManager databaseManager) {
        this.playerManager = playerManager;
        this.topTenSeason = new ArrayList<>();
        this.topTenLifetime = new ArrayList<>();
        new LoadTopTenRunnable(playerManager.getPlugin(), this, databaseManager).start();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "pvplugin";
    }

    @Override
    public @NotNull String getAuthor() {
        return "MCreeper12731";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }



    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {

        // %pvplugin_kills_(killtype)_(num)_(name/value)%

        String[] data = params.split("_");
        if (data.length < 2) return "Invalid Placeholder!";
        KillType killType = KillType.fromString(data[1]);
        if (data.length == 2) {
            switch (killType) {
                case SEASON:
                    return String.valueOf(playerManager.getStats(player.getUniqueId()).getSeasonKills());
                case LIFETIME:
                    return String.valueOf(playerManager.getStats(player.getUniqueId()).getLifetimeKills());
            }
        }
        int value = Integer.parseInt(data[2]);
        switch (killType) {
            case SEASON -> {
                if (value >= topTenSeason.size()) return "No placement";
                if (data[3].equals("name")) return topTenSeason.get(value - 1).getUsername();
                if (data[3].equals("value")) return String.valueOf(topTenSeason.get(value - 1).getSeasonKills());
            }
            case LIFETIME -> {
                if (value >= topTenLifetime.size()) return "No placement";
                if (data[3].equals("name")) return topTenLifetime.get(value - 1).getUsername();
                if (data[3].equals("value")) return String.valueOf(topTenLifetime.get(value - 1).getLifetimeKills());
            }
        }
        return "Invalid Placeholder!";
    }


}
