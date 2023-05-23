package net.skret.pvplugin.commands;

import lombok.Data;
import net.skret.pvplugin.PvPlugin;
import net.skret.pvplugin.utils.Color;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Data
public abstract class Command implements CommandExecutor {

    protected final PvPlugin plugin;
    protected final String name;
    protected final String permission;
    protected final String syntax;
    protected final Map<String, SubCommand> subCommands;

    public Command(PvPlugin plugin, String name, String permission, String syntax, boolean register) {
        this.plugin = plugin;
        this.name = name;
        this.permission = permission;
        this.syntax = syntax;
        this.subCommands = new HashMap<>();
        if (register) register();
    }

    public void register() {
        plugin.getCommand(this.name).setExecutor(this);
    }

    @Override
    public final boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (!hasPermission(sender)) {
            sender.sendMessage(Color.color("&cYou do not have permission to run this command!"));
            return true;
        }
        execute(sender, args);
        return true;
    }

    public final boolean hasPermission(CommandSender sender) {
        return sender.isOp() || sender.hasPermission(this.permission);
    }

    public void invalidSyntax(CommandSender sender) {
        sender.sendMessage(Color.color("&cInvalid syntax! Correct syntax: " + this.syntax));
    }

    public abstract void execute(CommandSender sender, String[] args);
}
