package de.acranum.chestshop.api;

import de.acranum.chestshop.api.shop.Shop;
import de.acranum.chestshop.ChestShop;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ChestShopApiImpl implements ChestShopAPI {

    private ChestShop plugin = ChestShop.getInstance();


    public ChestShopApiImpl(ChestShop plugin) {
    }


    @Override
    public void registerCommand(String name, CommandExecutor executor) {
    }

    @Override
    public List<Shop> getShops(UUID uuid) {
        return List.of();
    }

    @Override
    public List<Shop> getShops(String itemName) {
        plugin.getShopconfig().SearchItemFromShopConfig(itemName, 0);
        return List.of();
    }

    @Override
    public Shop getShop(Location location) {
        if (plugin.getShopconfig().getShop(location) == null) return null;
        return plugin.getShopconfig().getShop(location);
    }


    //itemConfig
    @Override
    public String IDCheck(String key) {
        return plugin.getItemconfig().IDCheck(key);
    }

    @Override
    public String searchForItemStack(ItemStack vergleich) {
        return plugin.getItemconfig().searchForItemStack(vergleich);
    }

    @Override
    public ItemStack getNBT(String id) {
        return plugin.getItemconfig().getNBT(id);
    }


    //ShopConfig
    @Override
    public FileConfiguration getShopconfig() {
        return plugin.getShopconfig().shopConfig;
    }
    //@Override
    //public String getItemFromShopConfig(String world, int xCoord, int yCoord, int zCoord) {
    //}

    @Override
    public List<String> searchShopFromShopConfig(String searchedItem) {
        return plugin.getShopconfig().SearchItemFromShopConfig(searchedItem, 0);
    }

    @Override
    public int getShopsFromPlayer(Player p) {
        return plugin.getShopconfig().getShopsFromPlayer(p);
    }

    @Override
    public void addShopToPlayer(Player p) {
        plugin.getShopconfig().addShopToPlayer(p);
    }

    @Override
    public void removeShopFromPlayer(Player p) {
        plugin.getShopconfig().removeShopFromPlayer(p);
    }

    @Override
    public ItemStack NameToItem(String itemName) {
        return plugin.getAPI().NameToItem(itemName);
    }

    @Override
    public void addConfig(FileConfiguration configuration) {

    }

    @Override
    public FileConfiguration getConfig(File file) {
        return null;
    }

    //shopConfig


    //@Override
    //public Shop getShop
}
