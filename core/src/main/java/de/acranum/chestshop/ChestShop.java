package de.acranum.chestshop;

import de.acranum.chestshop.api.ChestShopAPI;
import de.acranum.chestshop.Config.DefaultConfig;
import de.acranum.chestshop.Config.Itemconfig;
import de.acranum.chestshop.Config.Shopconfig;
import de.acranum.chestshop.GUIs.InfoGUI;
import de.acranum.chestshop.api.ChestShopApiImpl;
import de.acranum.chestshop.Util.lang;
import de.acranum.chestshop.api.ChestShopProvider;
import de.acranum.chestshop.api.addon;
import de.acranum.chestshop.commands.ChestShopCommand;
import de.acranum.chestshop.listeners.BlockBreak;
import de.acranum.chestshop.listeners.InvListener;
import de.acranum.chestshop.listeners.SignListener;
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

    private ChestShopApiImpl chestShopApiImpl;

    //private ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        instance = this;

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
        //protocolManager = ProtocolLibrary.getProtocolManager();
        getCommand("chestshop").setExecutor(new ChestShopCommand());
        getCommand("shopinfo").setExecutor(new InfoGUI());

        addon.loadAddons();
        shopconfig.migrate();
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

        this.chestShopApiImpl = new ChestShopApiImpl(this);
        ChestShopProvider.register(new ChestShopApiImpl(this));
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

    public ChestShopAPI getAPI() {
        return chestShopApiImpl;
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

    //public ProtocolManager getProtocolManager() {return protocolManager;}

    //TODO: remove useless code (aufräumen)
    //TODO: less big main and listener classes, more use of the util package and its methods

    public NamespacedKey getNamespacedKey() {
        NamespacedKey namespacedKey = new NamespacedKey(ChestShop.getPlugin(ChestShop.class), "chestshop");
        return namespacedKey;
    }
}