package net.skret.pvplugin.commands.subcommands.stats;

import net.skret.pvplugin.PvPlugin;
import net.skret.pvplugin.commands.SubCommand;
import net.skret.pvplugin.managers.DatabaseManager;
import net.skret.pvplugin.managers.PlayerManager;
import net.skret.pvplugin.utils.Color;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DisplaySubCommand extends SubCommand {

    private final DatabaseManager databaseManager;

    public DisplaySubCommand(PvPlugin plugin, String parentPermission, DatabaseManager databaseManager) {
        super(plugin, "display", parentPermission + ".display", "/kills display [player]");
        this.databaseManager = databaseManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        new BukkitRunnable() {
            @Override
            public void run() {

                if (args.length != 1) {
                    invalidSyntax(sender);
                    return;
                }

                String username = args[0];

                ResultSet resultSet = databaseManager.getData(username);

                if (resultSet == null) {
                    sender.sendMessage(Color.color("&cPlayer has never joined this server!"));
                    return;
                }

                try {
                    if (resultSet.next()) {
                        sender.sendMessage(Color.color(String.format(
                                "&eShowing stats for %s:%n &7- &eSeason Kills: %d%n &7- &eLifetime Kills: %d",
                                resultSet.getString("Username"),
                                resultSet.getInt("SeasonKills"),
                                resultSet.getInt("LifetimeKills")
                        )));
                        return;
                    }
                    sender.sendMessage(Color.color("&cPlayer has never joined this server!"));
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }
}
