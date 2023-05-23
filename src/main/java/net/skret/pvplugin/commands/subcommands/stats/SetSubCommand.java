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

public class SetSubCommand extends SubCommand {

    private final PlayerManager playerManager;
    private final DatabaseManager databaseManager;

    public SetSubCommand(PvPlugin plugin, String parentPermission, PlayerManager playerManager, DatabaseManager databaseManager) {
        super(plugin, "set", parentPermission + ".set", "/kills set [player] [season/lifetime/all] [amount]");
        this.playerManager = playerManager;
        this.databaseManager = databaseManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length != 3) {
            invalidSyntax(sender);
            return;
        }

        String username = args[0];

        KillType type;

        try {
            type = KillType.fromString(args[1]);
        } catch (IllegalArgumentException exception) {
            invalidSyntax(sender);
            return;
        }

        int amount;

        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException exception) {
            invalidSyntax(sender);
            return;
        }

        if (amount < 0) {
            sender.sendMessage("&cAmount cannot be lower than 0!");
            return;
        }

        Player target = Bukkit.getPlayer(username);

        sender.sendMessage(Color.color("&aSet " + type + " kills for player &6" + username + "&a to " + amount + "!"));

        if (target == null) {
            databaseManager.overwriteKills(username, type, amount);
            return;
        }

        playerManager.setKills(target.getUniqueId(), type, amount);
        databaseManager.saveData(target.getUniqueId());

    }
}
