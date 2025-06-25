package de.minnivini.chestshop.Config;

import de.minnivini.chestshop.ChestShop;
import de.minnivini.chestshop.Util.lang;
import de.minnivini.chestshop.Util.util;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Shopconfig {

    private FileConfiguration shopConfig;
    private File shopConfigFile;

    private final ChestShop plugin;

    public Shopconfig(ChestShop plugin) {
        this.plugin = plugin;
    }

    public void setupShopConfig() {
        if (shopConfigFile == null) {
            this.shopConfigFile = new File(plugin.getDataFolder(), "shops.yml");
        }
        shopConfig = YamlConfiguration.loadConfiguration(shopConfigFile);

        shopConfig.setDefaults(YamlConfiguration.loadConfiguration(shopConfigFile));
        shopConfig.options().copyDefaults(true);
        if (!shopConfigFile.exists()) {
            saveDefaultShopConfig();
        }
    }
    public void saveShopConfig() {
        try {
            shopConfig.save(shopConfigFile);
        } catch (IOException e) {
            plugin.getLogger().severe(lang.getMessage("saveErrShop") + e.getMessage());
        }
    }

    public void saveDefaultShopConfig() {
        plugin.saveResource("shops.yml", false);
    }

    public void addItemToShopConfig(String world, int xCoord, int yCoord, int zCoord, String item, Player p) {
        String key = world + "§" + xCoord + "§" + yCoord + "§" + zCoord;
        String key1 = "uuid" + xCoord + "_" + yCoord + "_" + zCoord;
        if (plugin.getDefaultConfig().getBlackWorlds() == null) {
            shopConfig.set("shops." + key, item);
            saveShopConfig();
        } else {
            if (plugin.getDefaultConfig().getBlackWorlds().contains(world)) {
                p.sendMessage(lang.getMessage("FalseWorld"));
            } else {
                shopConfig.set("shops." + key, item);
                saveShopConfig();
            }
        }
    }

    public String getItemFromShopConfig(String world, int xCoord, int yCoord, int zCoord) {
        String key = world + "§" + xCoord + "§" + yCoord + "§" + zCoord;
        if (shopConfig.getString("shops." + key) == null) return null;
        return shopConfig.getString("shops." + key);
    }

    public void removeItemFromShopConfig(String world, int xCoord, int yCoord, int zCoord) {
        String key = world + "§" + xCoord + "§" + yCoord + "§" + zCoord;
        if (shopConfig.contains("shops." + key)) {
            shopConfig.set("shops." + key, "");
            saveShopConfig();
        } else {
            plugin.getLogger().warning(lang.getMessage("noShoptoRem") + key);
        }
    }
    public List<String> searchItemFromShopConfig(String gesuchtesItem) {
        ConfigurationSection shops = shopConfig.getConfigurationSection("shops");
        List<String> gefundenenKoordinaten = new ArrayList<>();
        List<String> realKoordinaten = new ArrayList<>();

        if (shops != null) {
            for (String koordinaten : shops.getKeys(false)) {
                String item = shops.getString(koordinaten);
                if (item != null && item.equalsIgnoreCase(gesuchtesItem)) {
                    gefundenenKoordinaten.add(koordinaten);
                }
            }
        }
        if (!gefundenenKoordinaten.isEmpty()) {
            if (gefundenenKoordinaten.size() > 7) {
                Random random = new Random();
                for (int i = 0; i < 7; i++) {
                    int zufallsIndex = random.nextInt(gefundenenKoordinaten.size());
                    realKoordinaten.add(gefundenenKoordinaten.get(zufallsIndex));
                }
            } else {
                realKoordinaten = gefundenenKoordinaten;
            }
        } else {
            return null;
        }
        return realKoordinaten;
    }
    public void addPlayerToShopConfig(Player p, int shops) {
        UUID uuid = p.getUniqueId();
        shopConfig.set("players." + uuid, shops);
    }
    public int getShopsFromPlayer(Player p) {
        UUID uuid = p.getUniqueId();
        if (shopConfig.contains("players." + uuid)) return shopConfig.getInt("players." + uuid);
        addPlayerToShopConfig(p, 0);
        return 0;
    }
    public void addShopToPlayer(Player p) {
        if (util.getMaxShops(p) == 0) return;
        UUID uuid = p.getUniqueId();
        String path = "players." + uuid;
        if (shopConfig.contains(path)) {
            if (shopConfig.getInt(path) < util.getMaxShops(p)) {
                shopConfig.set(path, shopConfig.getInt(path) + 1);
                saveShopConfig();
            } else p.sendMessage(lang.getMessage("maxShops").replace("<amount>", String.valueOf(plugin.getShopconfig().getShopsFromPlayer(p))));
        } else {
            addPlayerToShopConfig(p, 0);
        }
    }
    public void removeShopFromPlayer(Player p) {
        UUID uuid = p.getUniqueId();
        String path = "players." + uuid;
        if (shopConfig.contains(path)) {
            if (shopConfig.getInt(path) > 0) {
                shopConfig.set(path, shopConfig.getInt(path) - 1);
            }
        }
    }

}
