package net.skret.pvplugin.utils;

public class CusMath {

    public static int round(double num) {
        num += 180;
        int a = (int)(num / 45) * 45;
        int b = a + 45;
        return (num - a > b - num) ? b - 180 : a - 180;
    }

}
