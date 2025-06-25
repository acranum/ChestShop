package de.minnivini.chestshop;

import de.minnivini.chestshop.Config.DefaultConfig;
import de.minnivini.chestshop.Config.Itemconfig;
import de.minnivini.chestshop.Config.Shopconfig;
import de.minnivini.chestshop.GUIs.InfoGUI;
import de.minnivini.chestshop.Util.lang;
import de.minnivini.chestshop.commands.ChestShopCMD;
import de.minnivini.chestshop.listeners.BlockBreak;
import de.minnivini.chestshop.listeners.InvListener;
import de.minnivini.chestshop.listeners.SignListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChestShop extends JavaPlugin {

    private static Economy econ = null;

    private static ChestShop instance;
    private Shopconfig shopconfig;
    private Itemconfig itemconfig;
    private DefaultConfig defaultConfig;

    @Override
    public void onEnable() {
        registerClasses();

        defaultConfig.setupConfig();
        lang.createLanguageFolder();
        lang.checkLanguageUpdates();
        getServer().getPluginManager().registerEvents(new SignListener(), this);
        getServer().getPluginManager().registerEvents(new InvListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreak(), this);

        shopconfig.setupShopConfig();
        itemconfig.setupItemConfig();

        if (!setupEconomy()) {
            System.out.println(lang.getMessage("noVault"));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getCommand("chestshop").setExecutor(new ChestShopCMD());
        getCommand("shopinfo").setExecutor(new InfoGUI());
    }
    @Override
    public void onDisable() {
        shopconfig.saveShopConfig();
        itemconfig.saveItemConfig();
    }

    private void registerClasses() {
        shopconfig = new Shopconfig(this);
        itemconfig = new Itemconfig(this);
        defaultConfig = new DefaultConfig(this);
    }
    public Shopconfig getShopconfig() {
        return shopconfig;
    }
    public Itemconfig getItemconfig() {
        return itemconfig;
    }
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    public static ChestShop getInstance() { return instance;}

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) return false;

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;

        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    //TODO: remove useless code (aufräumen)
    //TODO: less big main and listener classes, more use of the util package and its methods

    public NamespacedKey getNamespacedKey() {
        NamespacedKey namespacedKey = new NamespacedKey(ChestShop.getPlugin(ChestShop.class), "chestshop");
        return namespacedKey;
    }
}