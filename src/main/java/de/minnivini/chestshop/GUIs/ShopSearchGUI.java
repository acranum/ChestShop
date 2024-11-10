package de.minnivini.chestshop.GUIs;

import de.minnivini.chestshop.Util.ItemBuilder;

import de.minnivini.chestshop.ChestShop;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ShopSearchGUI {
    public void ShopSearch(Player p, List<String> shops, String material) {
        Inventory inventory;
        ItemStack Black_glass = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).build();
        ItemStack Red_glass = new ItemBuilder(Material.RED_STAINED_GLASS_PANE).build();
        inventory = Bukkit.createInventory(null, 27, "§bShop Search");

        inventory.setItem(0, Black_glass);
        inventory.setItem(1, Black_glass);
        inventory.setItem(2, Black_glass);
        inventory.setItem(3, Black_glass);
        inventory.setItem(4, Black_glass);
        inventory.setItem(5, Black_glass);
        inventory.setItem(6, Black_glass);
        inventory.setItem(7, Black_glass);
        inventory.setItem(8, Black_glass);
        inventory.setItem(9, Black_glass);

        inventory.setItem(10, Red_glass);
        inventory.setItem(11, Red_glass);
        inventory.setItem(12, Red_glass);
        inventory.setItem(13, Red_glass);
        inventory.setItem(14, Red_glass);
        inventory.setItem(15, Red_glass);
        inventory.setItem(16, Red_glass);

        inventory.setItem(17, Black_glass);
        inventory.setItem(18, Black_glass);
        inventory.setItem(19, Black_glass);
        inventory.setItem(20, Black_glass);
        inventory.setItem(21, Black_glass);
        inventory.setItem(22, Black_glass);
        inventory.setItem(23, Black_glass);
        inventory.setItem(24, Black_glass);
        inventory.setItem(25, Black_glass);
        inventory.setItem(26, Black_glass);

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
