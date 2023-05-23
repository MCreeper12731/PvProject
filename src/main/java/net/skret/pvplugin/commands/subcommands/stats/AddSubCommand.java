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

public class AddSubCommand extends SubCommand {

    private final PlayerManager playerManager;
    private final DatabaseManager databaseManager;

    public AddSubCommand(PvPlugin plugin, String parentPermission, PlayerManager playerManager, DatabaseManager databaseManager) {
        super(plugin, "set", parentPermission + ".set", "/kills set [player] [season/lifetime/all] [amount]");
        this.playerManager = playerManager;
        this.databaseManager = databaseManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length != 2) {
            invalidSyntax(sender);
            return;
        }

        String username = args[0];

        /*KillType type;

        try {
            type = KillType.fromString(args[1]);
        } catch (IllegalArgumentException exception) {
            invalidSyntax(sender);
            return;
        }*/

        int amount;

        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException exception) {
            invalidSyntax(sender);
            return;
        }

        Player target = Bukkit.getPlayer(username);

        sender.sendMessage(Color.color("Added " + amount + " kills to " + username));

        if (target == null) {
            databaseManager.addKills(username, amount);
            return;
        }

        playerManager.addKills(target.getUniqueId(), amount);
        databaseManager.saveData(target.getUniqueId());

    }

}
