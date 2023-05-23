package net.skret.pvplugin.models;

import lombok.Data;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;
import net.skret.pvplugin.PvPlugin;
import net.skret.pvplugin.managers.PlayerManager;
import net.skret.pvplugin.utils.Color;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

@Data
public class PlayerWrapper {

    private final PlayerManager playerManager;

    private UUID id;
    private String username;
    private int seasonKills;
    private int lifetimeKills;
    private Rank killRank;
    private char chatColor = 'f';

    public PlayerWrapper(PlayerManager playerManager, UUID playerId, String username, int seasonKills, int lifetimeKills) {
        this.playerManager = playerManager;
        this.id = playerId;
        this.username = username;
        this.seasonKills = seasonKills;
        this.lifetimeKills = lifetimeKills;
    }

    public void incrementKills() {
        addKills(1);
    }

    public void addKills(int amount) {
        this.seasonKills += amount;
        this.lifetimeKills += amount;
    }

    @Override
    public String toString() {
        return "Showing stats for" + username + ": season kills: " + seasonKills + ", lifetime kills: " + lifetimeKills;
    }
}
