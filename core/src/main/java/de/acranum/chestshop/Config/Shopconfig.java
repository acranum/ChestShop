package de.acranum.chestshop.Config;

import de.acranum.chestshop.ChestShop;
import de.acranum.chestshop.Util.lang;
import de.acranum.chestshop.Util.util;
import de.acranum.chestshop.api.shop.Shop;
import de.acranum.chestshop.api.shop.ShopType;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Shopconfig {

    public FileConfiguration shopConfig;
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
    public FileConfiguration getShopConfig() {
        return shopConfig;
    }

    public void AddShopToShopConfig(Shop shop, String itemName) {
        Location location = shop.getLocation();
        String key = location.getWorld().getName() + "§" + location.getBlockX() + "§" + location.getBlockY() + "§" + location.getBlockZ();
        shopConfig.set("shops." + key + ".shoptype", shop.getShopType().toString());
        shopConfig.set("shops." + key + ".price", shop.getPrice());
        shopConfig.set("shops." + key + ".item", itemName);
        if (shop.getOwner() == null) shopConfig.set("shops." + key + ".owner", null);
        else shopConfig.set("shops." + key + ".owner", shop.getOwner().getUniqueId().toString());
        shopConfig.set("shops." + key + ".amount", shop.getAmount());
        saveShopConfig();
    }
    public ShopType getShopType(Location location) {
        String key = "shops." + location.getWorld().getName() + "§" + location.getBlockX() + "§" + location.getBlockY() + "§" + location.getBlockZ();
        if (!shopConfig.contains(key + ".shoptype") || shopConfig.getString(key + ".shoptype") == null || shopConfig.getString(key + ".shoptype") == "") return null;
        return ShopType.valueOf(shopConfig.getString(key + ".shoptype"));
    }
    public String getPrice(Location location) {
        String key = "shops." + location.getWorld().getName() + "§" + location.getBlockX() + "§" + location.getBlockY() + "§" + location.getBlockZ();
        if (!shopConfig.contains(key + ".price") || shopConfig.getString(key + ".price") == null || shopConfig.getString(key + ".price") == "") return null;
        return shopConfig.getString(key + ".price");
    }
    public String getItemName(Location location) {
        String key = "shops." + location.getWorld().getName() + "§" + location.getBlockX() + "§" + location.getBlockY() + "§" + location.getBlockZ();
        if (!shopConfig.contains(key + ".item") || shopConfig.getString(key + ".item") == null || shopConfig.getString(key + ".item") == "") return null;
        return shopConfig.getString(key + ".item");
    }
    public OfflinePlayer getOwner(Location location) {
        String key = "shops." + location.getWorld().getName() + "§" + location.getBlockX() + "§" + location.getBlockY() + "§" + location.getBlockZ();
        if (!shopConfig.contains(key + ".owner") || shopConfig.getString(key + ".owner") == null || shopConfig.getString(key + ".owner") == "") return null;
        return Bukkit.getOfflinePlayer(UUID.fromString(shopConfig.getString(key + ".owner")));
    }
    public Integer getAmount(Location location) {
        String key = "shops." + location.getWorld().getName() + "§" + location.getBlockX() + "§" + location.getBlockY() + "§" + location.getBlockZ();
        if (!shopConfig.contains(key + ".amount") || shopConfig.getString(key + ".amount") == null || shopConfig.getString(key + ".amount") == "") return 1;
        return shopConfig.getInt(key + ".amount");
    }

    public Shop getShop(Location location) {
        String key = "shops." + location.getWorld().getName() + "§" + location.getBlockX() + "§" + location.getBlockY() + "§" + location.getBlockZ();
        if (!shopConfig.contains(key) || shopConfig.getString(key) == null || shopConfig.getString(key) == "") return null;
        if (getShopType(location) == null) return null;
        return new Shop(location.getBlock(), getShopType(location), getPrice(location), getOwner(location), NameToItem(getItemName(location)), getAmount(location));
    }

    public ItemStack NameToItem(String ItemName) {
        if (ItemName == null || ItemName.isEmpty()) {
            return null;
        }

        Material material = Material.matchMaterial(ItemName);
        if (material != null) {
            return new ItemStack(material);
        }
        if (plugin.getItemconfig().IDCheck(ItemName) != null) {
            return plugin.getItemconfig().getNBT(ItemName);
        }
        return null;
    }

    public void RemoveShopFromShopConfig(Location location) {
        String key = location.getWorld().getName() + "§" + location.getBlockX() + "§" + location.getBlockY() + "§" + location.getBlockZ();
        if (shopConfig.contains("shops." + key)) {
            shopConfig.set("shops." + key, "");
            saveShopConfig();
        } else {
            plugin.getLogger().warning(lang.getMessage("noShoptoRem") + key);
        }
    }

    public List<String> SearchItemFromShopConfig(String searchedItem, int max) {
        ConfigurationSection shops = shopConfig.getConfigurationSection("shops");
        List<String> foundCoords = new ArrayList<>();
        List<String> randomCoords = new ArrayList<>();

        if (shops != null) {
            for (String coord : shops.getKeys(false)) {
                String item = shopConfig.getString("shops." + coord + ".item");
                if (item != null && item.equalsIgnoreCase(searchedItem)) {
                    foundCoords.add(coord);
                }
            }
            if (foundCoords.isEmpty()) return null;
            Random random = new Random();
            int size = foundCoords.size();
            if (max != 0) size = Math.min(size, max);
            else size = Integer.MAX_VALUE;

            for (int i = 0; i < size; i++) {
                int randomI = random.nextInt(foundCoords.size());
                randomCoords.add(foundCoords.get(randomI));
                foundCoords.remove(randomI);
            }
            return randomCoords;
        }
        return null;
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
        UUID uuid = p.getUniqueId();
        String path = "players." + uuid;
        if (shopConfig.contains(path)) {
            if (shopConfig.getInt(path) < util.getMaxShops(p) || util.getMaxShops(p) == -1) {
                shopConfig.set(path, shopConfig.getInt(path) + 1);
                saveShopConfig();
            } else p.sendMessage(lang.getMessage("maxShops").replace("<amount>", String.valueOf(util.getMaxShops(p))));
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

    public void migrate() {
        ConfigurationSection shops = shopConfig.getConfigurationSection("shops");
        if (shops == null) return;

        int total = 0;
        int i = 0;

        for (String key : shops.getKeys(false)) {
            if (shopConfig.contains("shops." + key + ".item")) continue;
            total++;
        }

        for (String key : shops.getKeys(false)) {
            if (shopConfig.contains("shops." + key + ".item")) continue;
            String itemName = shops.getString(key);

            //get Location
            String[] parts = key.split("§");
            String worldName = parts[0];
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int z = Integer.parseInt(parts[3]);
            Location loc = new Location(Bukkit.getWorld(worldName), x, y, z);

            String section = "shops." + loc.getWorld().getName() + "§" + loc.getBlockX() + "§" + loc.getBlockY() + "§" + loc.getBlockZ();
            if (!shopConfig.contains(section) || shopConfig.getString(section) == null || shopConfig.getString(section) == "") continue;

            if (loc.getBlock().getState() instanceof Sign sign) {
                ShopType shopType = ShopType.SHOP;
                if (sign.getLine(1).equals("§a[Adminshop]")) shopType = ShopType.ADMIN_SHOP;
                int amount = 1;
                if (sign.getLine(1).equals("§a[Adminshop]")) amount = Integer.parseInt(sign.getLine(2));

                shopConfig.set("shops." + key + ".shoptype", shopType.toString());
                shopConfig.set("shops." + key + ".price", sign.getLine(1));
                shopConfig.set("shops." + key + ".item", itemName);
                if (!sign.getLine(1).equals("§a[Adminshop]")) shopConfig.set("shops." + key + ".owner", Bukkit.getOfflinePlayer(sign.getLine(2)).getUniqueId().toString());
                shopConfig.set("shops." + key + ".amount", amount);
                printProgress(i, total);
                i++;
            }
        }

        if (i == 0) Bukkit.getLogger().info("No shops to migrate");
        saveShopConfig();
    }
    int currentLength = 0;
    private void printProgress(int current, int total) {
        int barLength = 10; // Länge des Balkens
        int filled = (int) ((current / (double) total) * barLength);

        StringBuilder bar = new StringBuilder();
        bar.append("[");
        for (int i = 0; i < filled; i++) bar.append("#");
        for (int i = filled; i < barLength; i++) bar.append("-");
        bar.append("] ");

        int percent = (int) ((current / (double) total) * 100);
        bar.append(percent).append("% (").append(current).append("/").append(total).append(")");

        if (currentLength < filled) {
            Bukkit.getLogger().info(String.valueOf(bar));
            currentLength++;
        }

    }


}
