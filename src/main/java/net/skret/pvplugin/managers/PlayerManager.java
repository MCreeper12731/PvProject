package net.skret.pvplugin.managers;

import lombok.Getter;
import lombok.Setter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.node.types.WeightNode;
import net.luckperms.api.track.Track;
import net.luckperms.api.track.TrackManager;
import net.skret.pvplugin.PvPlugin;
import net.skret.pvplugin.models.KillType;
import net.skret.pvplugin.models.PlayerWrapper;
import net.skret.pvplugin.models.Rank;
import net.skret.pvplugin.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class PlayerManager {

    @Getter
    private final LuckPerms luckPerms;
    @Getter
    private final PvPlugin plugin;
    private final RanksManager ranksManager;
    private final Map<UUID, PlayerWrapper> players = new HashMap<>();
    private final Map<UUID, UUID> lastKilled = new HashMap<>();

    public PlayerManager(LuckPerms luckPerms, PvPlugin plugin, FileConfiguration config) {
        this.luckPerms = luckPerms;
        this.plugin = plugin;
        this.ranksManager = new RanksManager(config);
    }

    public void init(UUID playerId, String username) {
        init(playerId, username, 0, 0);
    }

    public void init(UUID playerId, String username, int seasonKills, int lifetimeKills) {
        players.put(playerId, new PlayerWrapper(this, playerId, username, seasonKills, lifetimeKills));
    }

    public void addKill(UUID playerId) {
        addKills(playerId, 1);
    }

    public void addKills(UUID playerId, int amount) {
        players.get(playerId).addKills(amount);
        updateRank(playerId);
    }

    public void setLastKilled(UUID killerId, UUID victimId) {
        lastKilled.put(killerId, victimId);
    }

    public UUID getLastKilled(UUID killerId) {
        return lastKilled.get(killerId);
    }

    public PlayerWrapper getStats(UUID playerId) {
        return players.get(playerId);
    }

    public List<PlayerWrapper> getStats() {
        return new ArrayList<>(players.values());
    }

    public void updateRank(UUID playerId) {
        PlayerWrapper stats = players.get(playerId);
        User user = luckPerms.getUserManager().getUser(playerId);

        if (user == null) return;

        Rank prevRank = stats.getKillRank();

        if (prevRank != null && prevRank.isInRange(stats.getSeasonKills())) return;

        for (Rank rank : ranksManager.getKillRanks()) {
            if (rank.isInRange(stats.getSeasonKills())) {
                stats.setKillRank(rank);
                user.data().add(InheritanceNode.builder(rank.getName()).build());
                break;
            }

        }

        if (prevRank != null && prevRank.getMinKills() < stats.getKillRank().getMinKills()) {
            Bukkit.getPlayer(stats.getId()).sendMessage(Color.color("&aCongratulations, you ranked up to &6" + stats.getKillRank().getName() + " &arank!"));
        }

        luckPerms.getUserManager().saveUser(user);
    }

    public void updateColor(UUID playerId) {

        PlayerWrapper stats = players.get(playerId);
        for (Rank rank : ranksManager.getStaffRanks()) {

            stats.setChatColor('f');

            return;

            //isRank(playerId, rank).thenAcceptAsync(result -> {
                //if (result) stats.setChatColor(rank.getColor());
            //});

        }

    }

    private CompletableFuture<Boolean> isRank(UUID playerId, Rank rank) {
        /*return luckPerms.getUserManager().loadUser(playerId)
                .thenApplyAsync(user -> {
                    Collection<Group> inheritedGroups = user.getInheritedGroups(user.getQueryOptions());
                    return inheritedGroups.stream().anyMatch(g -> g.getName().equals(rank.getName()));
                });*/
        return null;
    }

    public void setKills(UUID playerId, KillType type, int kills) {

        PlayerWrapper stats = players.get(playerId);

        switch (type) {
            case SEASON -> {
                stats.setSeasonKills(kills);
                updateRank(playerId);
            }
            case LIFETIME -> {
                stats.setLifetimeKills(kills);
            }
            case ALL -> {
                stats.setSeasonKills(kills);
                updateRank(playerId);
                stats.setLifetimeKills(kills);
            }
        }
    }

    public void remove(UUID uuid) {
        players.remove(uuid);
    }

}
