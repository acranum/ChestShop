package de.minnivini.chestshop.GUIs;

import de.minnivini.chestshop.ChestShop;
import de.minnivini.chestshop.Util.ItemBuilder;
import de.minnivini.chestshop.Util.lang;
import de.minnivini.chestshop.Util.util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InfoGUI {
    public void InfoGUI(CommandSender commandSender) {
        util util = new util();

        Player player = (Player) commandSender;
        int maxDiastance = 100;
        Block block = getTargetBlock(player, maxDiastance);
        if (block != null && block.getState() instanceof Sign) {
            int xCoord = block.getLocation().getBlockX();
            int yCoord = block.getLocation().getBlockY();
            int zCoord = block.getLocation().getBlockZ();
            if (ChestShop.getPlugin(ChestShop.class).getItemFromShopConfig(player.getWorld().getName(), xCoord, yCoord, zCoord) != null) {
                if (player.hasPermission("chestshop.shopinfo")) {
                    String item = ChestShop.getPlugin(ChestShop.class).getItemFromShopConfig(player.getWorld().getName(), xCoord, yCoord, zCoord);
                    ItemStack itemStack = new ItemStack(new ItemBuilder(Material.DIRT).build());
                    String seller = ((Sign) block.getState()).getLine(2);
                    String Preis = ((Sign) block.getState()).getLine(1);

                    if (item.equals("Air")) {
                        player.sendMessage(lang.getMessage("invalidMaterial"));
                    } else {
                        if (ChestShop.getPlugin(ChestShop.class).IDCheck(item) != null) {
                            itemStack = ChestShop.getPlugin(ChestShop.class).getNBT(item);
                        } else {
                            itemStack.setType(Material.valueOf(item));
                        }

                        Inventory inv = Bukkit.createInventory(null, 9, "§bShop Info");

                        inv.setItem(2, itemStack);
                        inv.setItem(6, new ItemBuilder(Material.PAPER).setDisplayname("§dInfos: ").setLore(ChatColor.WHITE + lang.getMessage("seller") + seller, ChatColor.WHITE + lang.getMessage("Prize") + Preis).build());
                        player.openInventory(inv);
                    }
                } else player.sendMessage(lang.getMessage("noPermission"));
            } else player.sendMessage(lang.getMessage("lookatSign"));
        } else player.sendMessage(lang.getMessage("lookatSign"));

    }
    public Block getTargetBlock(Player player, int maxDistance) {
        BlockIterator iterator = new BlockIterator(player, maxDistance);

        while (iterator.hasNext()) {
            Block block = iterator.next();
            if (!block.getType().isAir()) {
                return block;
            }
        }
        return null; // Wenn kein Block gefunden wird
    }
}
