package net.skret.pvplugin.commands;

import net.skret.pvplugin.PvPlugin;
import net.skret.pvplugin.utils.Color;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public abstract class SubCommand extends Command {

    public SubCommand(PvPlugin plugin, String name, String permission, String syntax) {
        super(plugin, name, permission, syntax, false);
    }

    public final void onCommand(CommandSender sender, String[] args) {
        if (!hasPermission(sender)) {
            sender.sendMessage(Color.color("&cYou do not have permission to run this command!"));
            return;
        }
        execute(sender, args);
    }
}
