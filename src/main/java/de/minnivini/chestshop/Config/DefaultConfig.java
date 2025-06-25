package de.minnivini.chestshop.Config;

import de.minnivini.chestshop.ChestShop;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class DefaultConfig {

    public FileConfiguration defaultConfig;
    private final ChestShop plugin;

    public DefaultConfig(ChestShop plugin) {
        this.plugin = plugin;
    }

    public void setupConfig() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveDefaultConfig();
        }
        defaultConfig = plugin.getConfig();

        defaultConfig.setDefaults(YamlConfiguration.loadConfiguration(configFile));
        defaultConfig.options().copyDefaults(true);
        plugin.saveDefaultConfig();
    }
    public String getLanguage() {
        if (defaultConfig.contains("language")) {
            return defaultConfig.getString("language");
        }
        return "en";
    }
    public List<String> getBlackWorlds() {
        if (defaultConfig.contains("World_Blacklist")) {
            List<String> worlds = defaultConfig.getStringList("World_Blacklist");

            if (worlds == null) {
                worlds.add("example%example");
            }
            return worlds;
        }else {
            return Collections.singletonList("example%example");
        }
    }
    public List<String> getBlackItems() {
        if (defaultConfig.contains("Item_Blacklist")) {
            List<String> items = defaultConfig.getStringList("Item_Blacklist");

            if (items == null) {
                items.add("example%example");
            }
            return items;
        }else {
            return Collections.singletonList("example%example");
        }
    }
}
