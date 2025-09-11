package de.acranum.chestshop.Config;

import de.acranum.chestshop.ChestShop;
import de.acranum.chestshop.Util.lang;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

public class Itemconfig {

    public FileConfiguration ItemConfig;
    private File ItemConfigFile;

    private final ChestShop plugin;

    public Itemconfig(ChestShop plugin) {
        this.plugin = plugin;
    }

    public void setupItemConfig() {
        if (ItemConfigFile == null) {
            ItemConfigFile = new File(plugin.getDataFolder(), "items.yml");
        }
        ItemConfig = YamlConfiguration.loadConfiguration(ItemConfigFile);

        ItemConfig.setDefaults(YamlConfiguration.loadConfiguration(ItemConfigFile));
        ItemConfig.options().copyDefaults(true);
        if (!ItemConfigFile.exists()) {
            saveDefaultItemConfig();
        }
    }

    public void saveItemConfig() {
        try {
            ItemConfig.save(ItemConfigFile);
        } catch (IOException e) {
            plugin.getLogger().severe(lang.getMessage("saveErrItem") + e.getMessage());
        }
    }

    public void saveDefaultItemConfig() {
        plugin.saveResource("items.yml", false);
    }

    public void addCurrentID(ItemStack item) {
        int currentID = ItemConfig.getInt("current_id", 0);
        currentID++;
        String itemID = item.getType() + "#" + currentID;
        saveItemToConfig(itemID, item);

        ItemConfig.set("current_id", currentID);
        saveItemConfig();
    }

    public void saveItemToConfig(String itemID, ItemStack itemStack) {
        ConfigurationSection itemSection = ItemConfig.createSection(itemID);
        itemSection.set("ItemStack", itemStack);
        saveItemConfig();
    }

    public int getCurrentID() {
        return ItemConfig.getInt("current_id", 0);
    }

    public String IDCheck(String key) {
        return ItemConfig.getString(key);
    }
    public String searchForItemStack(ItemStack vergleich) {
        for (String key : ItemConfig.getKeys(false)) {
            ItemStack configItem = ItemConfig.getItemStack(key + ".ItemStack");
            if (vergleich.isSimilar(configItem)) {
                return key;
            }
        }
        return null;
    }
    public List<String> AllSpecialItems() {
        List<String> Items = new ArrayList<>();
        for (String key : ItemConfig.getKeys(false)) {
            if (!(Objects.equals(key, "current_id"))) {
                Items.add(key);
            }
        }
        return Items;
    }
    public ItemStack getNBT(String id) {
        ConfigurationSection itemSection = ItemConfig.getConfigurationSection(id);

        ItemStack item = new ItemStack(Material.AIR);

        if (itemSection.getItemStack("ItemStack") != null) {
            item = itemSection.getItemStack("ItemStack");
        }
        return item;
    }

}

