package de.minnivini.chestshop;

import de.minnivini.chestshop.Util.lang;
import de.minnivini.chestshop.commands.csCommand;
import de.minnivini.chestshop.commands.tabCompleter;
import de.minnivini.chestshop.listeners.BlockBreak;
import de.minnivini.chestshop.listeners.InvListener;
import de.minnivini.chestshop.listeners.SignListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.C;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public final class ChestShop extends JavaPlugin {

    private static Economy econ = null;
    private FileConfiguration shopConfig;
    private File shopConfigFile;
    private FileConfiguration ItemConfig;
    private File ItemConfigFile;
    public FileConfiguration defaultConfig;


    @Override
    public void onEnable() {
        setupConfig();
        lang.createLanguageFolder();
        getServer().getPluginManager().registerEvents(new SignListener(), this);
        getServer().getPluginManager().registerEvents(new InvListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreak(), this);
        //getCommand("shopinfo").setExecutor(new ShopInfo());

        setupShopConfig();
        setupItemConfig();

        if (!setupEconomy()) {
            System.out.println(lang.getMessage("noVault"));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getCommand("chestshop").setTabCompleter(new csCommand());
        getCommand("chestshop").setExecutor(new csCommand());
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    @Override
    public void onDisable() {
        saveShopConfig();
        saveItemConfig();
    }
    private void setupConfig() {
        File configFile = new File(this.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            this.saveDefaultConfig();
        }
        defaultConfig = this.getConfig();
    }

    public void loadConfig() {
        getConfig().options().copyDefaults(false);
        saveConfig();
    }
    public String getLanguage() {
        if (defaultConfig.contains("language")) {
            return defaultConfig.getString("language");
        } else {
            return "en";
        }
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
    /*public int getCooldown() {
        if (defaultConfig.contains("TP_cooldown")) {
            int Cooldown = defaultConfig.getInt("TP_cooldown");
                return Cooldown;
        } else return 0;
    }*/

    //------------------------------------------Shop Config-------------------------------------------------------------
    private void setupShopConfig() {
        if (shopConfigFile == null) {
            shopConfigFile = new File(getDataFolder(), "shops.yml");
        }
        shopConfig = YamlConfiguration.loadConfiguration(shopConfigFile);

        if (!shopConfigFile.exists()) {
            saveDefaultShopConfig();
        }
    }

    private void saveShopConfig() {
        try {
            shopConfig.save(shopConfigFile);
        } catch (IOException e) {
            getLogger().severe(lang.getMessage("saveErrShop") + e.getMessage());
        }
    }

    private void saveDefaultShopConfig() {
        saveResource("shops.yml", false);
    }

    //-------------------------------------------Item Config---------------------------------------------------------
    private void setupItemConfig() {
        if (ItemConfigFile == null) {
            ItemConfigFile = new File(getDataFolder(), "items.yml");
        }
        ItemConfig = YamlConfiguration.loadConfiguration(ItemConfigFile);

        if (!ItemConfigFile.exists()) {
            saveDefaultItemConfig();
        }
    }

    private void saveItemConfig() {
        try {
            ItemConfig.save(ItemConfigFile);
        } catch (IOException e) {
            getLogger().severe(lang.getMessage("saveErrItem") + e.getMessage());
        }
    }

    private void saveDefaultItemConfig() {
        saveResource("items.yml", false);
    }

    //-------------------------------------------------Shop Methoden
    public void addItemToShopConfig(String world, int xCoord, int yCoord, int zCoord, String item, Player p) {
        String key = world + "§" + xCoord + "§" + yCoord + "§" + zCoord;
        String key1 = "uuid" + xCoord + "_" + yCoord + "_" + zCoord;
        if (getBlackWorlds() == null) {
            shopConfig.set("shops." + key, item);
            saveShopConfig(); // Speichere die aktualisierte Konfiguration
        } else {
            if (getBlackWorlds().contains(world)) {
                p.sendMessage(lang.getMessage("FalseWorld"));
            } else {
                shopConfig.set("shops." + key, item);
                saveShopConfig(); // Speichere die aktualisierte Konfiguration
            }
        }
    }

    public String getItemFromShopConfig(String world, int xCoord, int yCoord, int zCoord) {
        String key = world + "§" + xCoord + "§" + yCoord + "§" + zCoord;
        return shopConfig.getString("shops." + key);
    }
    /*public String getPlayerFromShopConfig(int xCoord, int yCoord, int zCoord) {
        String key = "uuid" + xCoord + "_" + yCoord + "_" + zCoord;
        return shopConfig.getString("shop." + key);
    }*/

    public void removeItemFromShopConfig(String world, int xCoord, int yCoord, int zCoord) {
        String key = world + "§" + xCoord + "§" + yCoord + "§" + zCoord;
        if (shopConfig.contains("shops." + key)) {
            shopConfig.set("shops." + key, null); // Entferne den Eintrag mit den Koordinaten aus der Konfigurationsdatei
            saveShopConfig(); // Speichere die aktualisierte Konfiguration
        } else {
            getLogger().warning(lang.getMessage("noShoptoRem") + key);
        }
    }
    public List<String> searchItemFromShopCongig(String gesuchtesItem) {
        ConfigurationSection shops = shopConfig.getConfigurationSection("shops");
        List<String> gefundenenKoordinaten = new ArrayList<>();
        List<String> realKoordinaten = new ArrayList<>();

        if (shops != null) {
            for (String koordinaten : shops.getKeys(false)) {
                String item = shops.getString(koordinaten);
                //erstellt Liste
                if (item != null && item.equalsIgnoreCase(gesuchtesItem)) {
                    gefundenenKoordinaten.add(koordinaten);
                    // Hier könntest du die Koordinaten weiter verarbeiten oder andere Aktionen ausführen
                }
            }
        }
        if (!gefundenenKoordinaten.isEmpty()) {
            //mehr als 7
            if (gefundenenKoordinaten.size() > 7) {
                Random random = new Random();
                for (int i = 0; i < 7; i++) {
                    int zufallsIndex = random.nextInt(gefundenenKoordinaten.size());
                    realKoordinaten.add(gefundenenKoordinaten.get(zufallsIndex));
                }
                //weniger als 7
            } else {
                realKoordinaten = gefundenenKoordinaten;
            }
        } else {
            return null;
        }
        return realKoordinaten;
    }

    public String formatiereKoordinaten(String roheKoordinaten) {
        // Ersetze den Unterstrich durch Leerzeichen
        roheKoordinaten = roheKoordinaten.replace("§", " ");

        // Teile die Koordinaten anhand von Leerzeichen
        String[] teile = roheKoordinaten.split(" ");

        // Baue die formatierten Koordinaten
        return teile[0] + " " + teile[1] + " " + teile[2] + " " + teile[3];
    }

    //--------------------------------------------------------item Methoden-----------------------------------------------
    public void addcurrentnumber(ItemStack item) {
        int currentID = ItemConfig.getInt("current_id", 0);
        currentID++;
        String itemID = item.getType().toString() + "#" + currentID;
        saveItemToConfig(itemID, item);

        // Die neue ID in die Config schreiben
        ItemConfig.set("current_id", currentID);
        saveItemConfig();
    }

    public void saveItemToConfig(String itemID, ItemStack itemStack) {
        ConfigurationSection itemSection = ItemConfig.createSection(itemID);
        itemSection.set("ItemStack", itemStack);
        // Typ des Items speichern
        /*itemSection.set("type", itemStack.getType().toString());
        // Custom Name (falls vorhanden)
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            itemSection.set("display-name", itemStack.getItemMeta().getDisplayName());
        }
        // Lore (falls vorhanden)
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore()) {
            itemSection.set("lore", itemStack.getItemMeta().getLore());
        }
        // Enchantments (falls vorhanden)
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasEnchants()) {
            ConfigurationSection enchantsSection = itemSection.createSection("enchants");
            itemStack.getEnchantments().forEach((enchant, level) ->
                    enchantsSection.set(enchant.getName(), level));
        }*/
        saveItemConfig();
    }

    public int curentID() {
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

    public ItemStack getNBT(String id) {
        //if (IDCheck(id) != null) {
            ConfigurationSection itemSection = ItemConfig.getConfigurationSection(id);
            //String type = itemSection.getString("type");

            ItemStack item = new ItemStack(Material.DIRT);

            item = itemSection.getItemStack("ItemStack");
            ItemMeta meta = item.getItemMeta();

            /*//wenn displayname
            if (itemSection.isString("display-name")) {
                String displayName = itemSection.getString("display-name");
                meta.setDisplayName(displayName);
            }
            //wenn lore
            if (itemSection.isString("lore")) {
                List<String> lore = itemSection.getStringList("lore");
                meta.setLore(lore);
            }
            //wenn enchantments
            if (itemSection.isConfigurationSection("enchants")) {
                ConfigurationSection enchantmentsSection = itemSection.getConfigurationSection("enchants");
                for (String enchantmentKey : enchantmentsSection.getKeys(false)) {
                    if (enchantmentsSection.isInt(enchantmentKey)) {
                        int enchantmentLevel = enchantmentsSection.getInt(enchantmentKey);

                        Enchantment enchantment = Enchantment.getByName(enchantmentKey);
                        if (enchantment != null) {
                            meta.addEnchant(enchantment, enchantmentLevel, true);
                        }
                    }
                }
            }
            /*if (enchantmentList != null) {
                for (int i = 0; i < enchantmentList.size(); i++) {
                    String enchantment = String.valueOf(enchantmentList.get(i));
                    int level = enchantmentLevelList.get(i);
                    System.out.println(enchantment);
                    Objects.requireNonNull(meta).addEnchant(Enchantment.getByName(enchantment), level, true);
                }
            }*/
            return item;
        //} else {
        //    getLogger().warning("Die 'item'-Kategorie ist in der Konfiguration nicht vorhanden.");
        //}
    }
}

