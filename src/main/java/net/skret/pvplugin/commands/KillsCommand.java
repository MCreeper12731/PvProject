package net.skret.pvplugin.commands;

import net.skret.pvplugin.PvPlugin;
import net.skret.pvplugin.commands.subcommands.stats.*;
import net.skret.pvplugin.managers.DatabaseManager;
import net.skret.pvplugin.managers.PlayerManager;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class KillsCommand extends Command {

    public KillsCommand(PvPlugin plugin, PlayerManager playerManager, DatabaseManager databaseManager) {
        super(plugin, "kills", "pvplugin.kills", "/kills [display/reset/set/save]", true);
        subCommands.put("display", new DisplaySubCommand(plugin, this.permission, databaseManager));
        subCommands.put("reset", new ResetSubCommand(plugin, this.permission, playerManager, databaseManager));
        subCommands.put("save", new SaveSubCommand(plugin, this.permission, databaseManager));
        subCommands.put("set", new SetSubCommand(plugin, this.permission, playerManager, databaseManager));
        subCommands.put("add", new AddSubCommand(plugin, this.permission, playerManager, databaseManager));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length == 0) {
            invalidSyntax(sender);
            return;
        }

        SubCommand subCommand = subCommands.get(args[0]);

        if (subCommand == null) {
            invalidSyntax(sender);
            return;
        }

        subCommand.onCommand(sender, Arrays.copyOfRange(args, 1, args.length));

    }
}
