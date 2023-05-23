package net.skret.pvplugin.models;

public enum KillType {

    SEASON,
    LIFETIME,
    ALL;

    public static KillType fromString(String string) {
        return KillType.valueOf(string.toUpperCase());
    }

}
