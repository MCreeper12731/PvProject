package net.skret.pvplugin.managers;

import lombok.Getter;
import net.skret.pvplugin.PvPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {

    private final PvPlugin plugin;

    @Getter
    private FileConfiguration config;

    public ConfigManager(PvPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        this.config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder().getAbsoluteFile() + "/config.yml"));
    }

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder().getAbsoluteFile() + "/config.yml"));

    }

}
