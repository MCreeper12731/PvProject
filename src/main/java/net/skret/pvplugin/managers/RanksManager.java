package net.skret.pvplugin.managers;

import lombok.Getter;
import net.skret.pvplugin.models.Rank;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class RanksManager {

    @Getter
    private final List<Rank> staffRanks;
    @Getter
    private final List<Rank> vipRanks;
    @Getter
    private final List<Rank> killRanks;

    public RanksManager(FileConfiguration config) {
        this.staffRanks = List.of(
                new Rank("gm", 'c'),
                new Rank("gs", 'd'),
                new Rank("helper", 'd'),
                new Rank("builder", 'a')
        );
        this.vipRanks = List.of(

        );
        this.killRanks = List.of(
                new Rank(1025, 100000000, "god", 'f', config.getInt("god")),
                new Rank(950, 1025, "highness", 'f',  config.getInt("highness")),
                new Rank(875, 950, "emperor", 'f',    config.getInt("emperor")),
                new Rank(800, 875, "kingsmen", 'f',   config.getInt("kingsmen")),
                new Rank(725, 800, "guardian", 'f',   config.getInt("guardian")),
                new Rank(650, 725, "knight", 'f',     config.getInt("knight")),
                new Rank(575, 650, "calvary", 'f',    config.getInt("calvary")),
                new Rank(500, 575, "soldier", 'f',    config.getInt("soldier")),
                new Rank(425, 500, "defender", 'f',   config.getInt("defender")),
                new Rank(350, 425, "guard", 'f',      config.getInt("guard")),
                new Rank(275, 350, "sentry", 'f',     config.getInt("sentry")),
                new Rank(200, 275, "scout", 'f',      config.getInt("scout")),
                new Rank(125, 200, "peon", 'f',       config.getInt("peon")),
                new Rank(50, 125, "rat", 'f',         config.getInt("rat")),
                new Rank(0, 50, "default", 'f',       config.getInt("default"))
        );
    }


}
