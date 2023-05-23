package net.skret.pvplugin.commands.subcommands.stats;

import net.skret.pvplugin.PvPlugin;
import net.skret.pvplugin.commands.SubCommand;
import net.skret.pvplugin.managers.DatabaseManager;
import net.skret.pvplugin.managers.PlayerManager;
import net.skret.pvplugin.models.KillType;
import net.skret.pvplugin.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ResetSubCommand extends SubCommand {

    private final PlayerManager playerManager;
    private final DatabaseManager databaseManager;

    public ResetSubCommand(PvPlugin plugin, String parentPermission, PlayerManager playerManager, DatabaseManager databaseManager) {
        super(plugin, "reset", parentPermission + ".reset", "/kills reset [player/all] [season/lifetime/all]");
        this.playerManager = playerManager;
        this.databaseManager = databaseManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        new BukkitRunnable() {
            @Override
            public void run() {

                if (args.length != 1 && args.length != 2) {
                    invalidSyntax(sender);
                    return;
                }

                String username = args[0];

                if (username.equals("all")) {

                    databaseManager.resetAllSeasonKills();
                    sender.sendMessage(Color.color("&aReset all season kills for all players!"));
                    return;

                }

                KillType type;

                try {
                    type = KillType.fromString(args[1]);
                } catch (IllegalArgumentException exception) {
                    invalidSyntax(sender);
                    return;
                }

                Player target = Bukkit.getPlayerExact(username);

                sender.sendMessage(Color.color("&aReset " + type + " kills for player &6" + username + "&a!"));

                if (target == null) {
                    databaseManager.overwriteKills(username, type, 0);
                    return;
                }

                playerManager.setKills(target.getUniqueId(), type, 0);
                databaseManager.saveData(target.getUniqueId());
            }
        }.runTaskAsynchronously(plugin);
    }
}
