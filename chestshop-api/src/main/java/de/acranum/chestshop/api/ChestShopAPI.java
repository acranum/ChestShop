package de.acranum.chestshop.api;

import de.acranum.chestshop.api.shop.Shop;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;
import java.util.UUID;

public interface ChestShopAPI {

    void registerCommand(String name, CommandExecutor executor);

    List<Shop> getShops(UUID uuid);
    List<Shop> getShops(String ItemName);
    Shop getShop(Location location);

    //ItemConfig
    String IDCheck(String key);
    String searchForItemStack(ItemStack vergleich);
    ItemStack getNBT(String id);

    //Shopconfig
    FileConfiguration getShopconfig();
    //String getShopFromShopConfig(String world, int xCoord, int yCoord, int zCoord);
    List<String> searchShopFromShopConfig(String searchedItem);

    int getShopsFromPlayer(Player p);
    void addShopToPlayer(Player p);
    void removeShopFromPlayer(Player p);

    ItemStack NameToItem(String itemName);

    //config
    void addConfig(FileConfiguration configuration);
    FileConfiguration getConfig(File file);
}
