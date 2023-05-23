package net.skret.pvplugin.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Rank {

    private final int minKills;
    private final int maxKills;
    private final String name;
    private final char color;
    private final int killReward;

    public Rank(String name, char color) {
        this.minKills = Integer.MAX_VALUE;
        this.maxKills = Integer.MAX_VALUE;
        this.name = name;
        this.color = color;
        this.killReward = 0;
    }

    public boolean isInRange(int kills) {
        boolean a = kills >= minKills; //because
        boolean b = kills < maxKills; //arne

        return a && b;
    }
}
