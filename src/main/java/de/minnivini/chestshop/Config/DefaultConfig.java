package de.minnivini.chestshop.Config;

import de.minnivini.chestshop.ChestShop;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class DefaultConfig {

    public FileConfiguration defaultConfig;
    private final ChestShop plugin;
    private Set<Material> blacklistedItems = new HashSet<>();

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

        loadBlacklistedItems();
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
    public Set<Material> getBlacklistedItems() {
        return blacklistedItems;
    }

    private void loadBlacklistedItems() {
        List<String> items = defaultConfig.getStringList("Item_Blacklist");
        blacklistedItems = items.stream().map(Material::getMaterial).filter(Objects::nonNull).collect(Collectors.toSet());
    }
}
