package net.skret.pvplugin.commands.subcommands.stats;

import net.skret.pvplugin.PvPlugin;
import net.skret.pvplugin.commands.SubCommand;
import net.skret.pvplugin.managers.DatabaseManager;
import net.skret.pvplugin.utils.Color;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class SaveSubCommand extends SubCommand {

    private final DatabaseManager databaseManager;

    public SaveSubCommand(PvPlugin plugin, String parentPermission, DatabaseManager databaseManager) {
        super(plugin, "save", parentPermission + ".save", "/kills save");
        this.databaseManager = databaseManager;
    }

    @Override
    public void execute(CommandSender sender, String[] ar) {

        new BukkitRunnable() {
            @Override
            public void run() {
                databaseManager.saveAllData();
                sender.sendMessage(Color.color("&aData saved successfully!"));
            }
        }.runTaskAsynchronously(plugin);
    }
}
