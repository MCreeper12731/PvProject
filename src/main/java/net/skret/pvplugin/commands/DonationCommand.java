package net.skret.pvplugin.commands;

import net.skret.pvplugin.utils.Color;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class DonationCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        String middleLine = StringUtils.center(args[0] + " has donated!", 16 + 13);

        Bukkit.broadcastMessage(Color.color("&c❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤"));
        Bukkit.broadcastMessage(Color.color("&a" + middleLine));
        Bukkit.broadcastMessage(Color.color("&c❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤"));

        return true;
    }
}
