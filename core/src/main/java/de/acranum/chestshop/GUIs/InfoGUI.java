package de.acranum.chestshop.GUIs;

import de.acranum.chestshop.ChestShop;
import de.acranum.chestshop.Util.ItemBuilder;
import de.acranum.chestshop.Util.lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;

public class InfoGUI implements CommandExecutor {
    public void InfoGUI(CommandSender commandSender) {
        ChestShop plugin = ChestShop.getPlugin(ChestShop.class);
        Player player = (Player) commandSender;
        int maxDiastance = 100;
        Block block = getTargetBlock(player, maxDiastance);
        if (block != null && block.getState() instanceof Sign) {
            if (plugin.getShopconfig().getItemName(block.getLocation()) != null) {
                if (player.hasPermission("chestshop.shopinfo")) {
                    String item = plugin.getShopconfig().getItemName(block.getLocation());
                    ItemStack itemStack = new ItemStack(new ItemBuilder(Material.DIRT).build());
                    String seller = ((Sign) block.getState()).getLine(2);
                    String Preis = ((Sign) block.getState()).getLine(1);
                    if (item.equals("Air")) {
                        player.sendMessage(lang.getMessage("invalidMaterial"));
                    } else {
                        if (plugin.getItemconfig().IDCheck(item) != null) {
                            itemStack = plugin.getItemconfig().getNBT(item);
                        } else {
                            itemStack.setType(Material.valueOf(item));
                        }
                        Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, ChatColor.BOLD + "§3Shop Info");

                        //glass
                        ItemStack blackglass = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).build();
                        inv.setItem(0, blackglass);
                        inv.setItem(2, blackglass);
                        inv.setItem(4, blackglass);

                        inv.setItem(1, itemStack);
                        inv.setItem(3, new ItemBuilder(Material.PAPER).setDisplayname(ChatColor.BOLD + "§fInfo: ").setGlow().setLore("§7" + lang.getMessage("seller").replace("<seller>", seller), "§7" + lang.getMessage("price").replace("<price>", Preis)).build());
                        player.openInventory(inv);
                        if (!Bukkit.getOfflinePlayer(seller).hasPlayedBefore()) inv.setItem(3, new ItemBuilder(Material.PAPER).setDisplayname(ChatColor.BOLD + "§fInfos: ").setGlow().setLore("§7" + lang.getMessage("seller").replace("<seller>", "§a[AdminShop]"), "§7" + lang.getMessage("price").replace("<price>", Preis)).build());
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

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        InfoGUI(commandSender);
        return false;
    }
}