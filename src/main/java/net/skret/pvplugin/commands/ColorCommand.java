package net.skret.pvplugin.commands;

import net.skret.pvplugin.PvPlugin;
import net.skret.pvplugin.managers.PlayerManager;
import net.skret.pvplugin.utils.Color;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ColorCommand extends Command {

    private final PlayerManager playerManager;

    public ColorCommand(PvPlugin plugin, PlayerManager playerManager) {
        super(plugin, "color", "pvplugin.color", "/color", true);
        this.playerManager = playerManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Color.color("&cOnly a player can run this command!"));
            return;
        }



    }
}
