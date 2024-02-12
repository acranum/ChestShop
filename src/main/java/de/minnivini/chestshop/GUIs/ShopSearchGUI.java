package de.minnivini.chestshop.GUIs;

import de.minnivini.chestshop.Util.ItemBuilder;

import de.minnivini.chestshop.ChestShop;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ShopSearchGUI {
    public void ShopSearch(Player p, List<String> shops, String material) {
        Inventory inventory;

        inventory = Bukkit.createInventory(null, 27, "§bShop Search");

        inventory.setItem(0, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).build());
        inventory.setItem(1, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).build());
        inventory.setItem(2, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).build());
        inventory.setItem(3, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).build());
        inventory.setItem(4, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).build());
        inventory.setItem(5, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).build());
        inventory.setItem(6, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).build());
        inventory.setItem(7, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).build());
        inventory.setItem(8, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).build());
        inventory.setItem(9, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).build());

        inventory.setItem(10, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).build());
        inventory.setItem(11, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).build());
        inventory.setItem(12, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).build());
        inventory.setItem(13, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).build());
        inventory.setItem(14, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).build());
        inventory.setItem(15, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).build());
        inventory.setItem(16, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).build());

        inventory.setItem(17, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).build());
        inventory.setItem(18, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).build());
        inventory.setItem(19, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).build());
        inventory.setItem(20, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).build());
        inventory.setItem(21, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).build());
        inventory.setItem(22, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).build());
        inventory.setItem(23, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).build());
        inventory.setItem(24, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).build());
        inventory.setItem(25, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).build());
        inventory.setItem(26, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).build());

        for (int i = 0; i < shops.size(); i++) {
            String Koordinaten = shops.get(i);
            String[] teile = Koordinaten.split("§");
            String world = teile[0];
            String x = teile[1];
            String y = teile[2];
            String z = teile[3];
            Koordinaten = Koordinaten.replace("§", " ");
            if (ChestShop.getPlugin(ChestShop.class).IDCheck(material) != null) {
                ItemStack item = ChestShop.getPlugin(ChestShop.class).getNBT(material);
                inventory.setItem(i + 10, new ItemBuilder(item.getType()).setDisplayname(ChatColor.RESET + x + " " + y + " " + z).setLocalizedName("shopTP").setLore(world).build());
            } else {
                inventory.setItem(i + 10, new ItemBuilder(Material.valueOf(material)).setDisplayname(ChatColor.RESET + x + " " + y + " " + z).setLocalizedName("shopTP").setLore(world).build());
            }
        }
        p.openInventory(inventory);
    }
}
