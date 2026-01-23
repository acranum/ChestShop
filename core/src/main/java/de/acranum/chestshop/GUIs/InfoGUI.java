package de.acranum.chestshop.GUIs;

import de.acranum.chestshop.ChestShop;
import de.acranum.chestshop.Util.ItemBuilder;
import de.acranum.chestshop.Util.lang;
import de.acranum.chestshop.api.shop.ShopType;
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

    private final ChestShop plugin = ChestShop.getInstance();
    final int maxDistance = 100;

    public void InfoGUI(CommandSender commandSender) {
        Player player = (Player) commandSender;
        Block block = getTargetBlock(player, maxDistance);

        if (block == null || !(block.getState() instanceof Sign)) {
            player.sendMessage(lang.getMessage("lookatSign"));
            return;
        }
        if (plugin.getShopconfig().getItemName(block.getLocation()) == null) {
            player.sendMessage(lang.getMessage("lookatSign"));
            return;
        }
        if (!player.hasPermission("chestshop.shopinfo")) {
            player.sendMessage(lang.getMessage("noPermission"));
            return;
        }

        String item = plugin.getShopconfig().getItemName(block.getLocation());
        String seller = ((Sign) block.getState()).getLine(2);
        if (plugin.getShopconfig().getShopType(block.getLocation()).equals(ShopType.ADMIN_SHOP)) seller = "§a[AdminShop]";
        String Preis = ((Sign) block.getState()).getLine(1);

        if (item.equalsIgnoreCase("Air") || item.equalsIgnoreCase("?")) {
            player.sendMessage(lang.getMessage("invalidMaterial"));
            return;
        }

        ItemStack itemStack = new ItemStack(new ItemBuilder(Material.BARRIER).build());
        if (plugin.getItemconfig().IDCheck(item) != null) {
            itemStack = plugin.getItemconfig().getNBT(item);
        } else if (!(item.equalsIgnoreCase("Air")) && !(item.equalsIgnoreCase("?"))) {
            itemStack.setType(plugin.getShopconfig().getShop(block.getLocation()).getItem().getType());
        }
        itemStack.setAmount(plugin.getShopconfig().getAmount(block.getLocation()));

        Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, ChatColor.BOLD + "§3Shop Info");

        ItemStack BlackGlass = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).build();
        inv.setItem(0, BlackGlass);
        inv.setItem(2, BlackGlass);
        inv.setItem(4, BlackGlass);

        inv.setItem(1, itemStack);
        inv.setItem(3, new ItemBuilder(Material.PAPER).setDisplayname(ChatColor.BOLD + "§fInfo: ").setGlow().setLore("§7" + lang.getMessage("seller").replace("<seller>", seller), "§7" + lang.getMessage("price").replace("<price>", Preis), "§7" + lang.getMessage("amount").replace("<amount>", String.valueOf(itemStack.getAmount()))).build());
        player.openInventory(inv);
    }
    public Block getTargetBlock(Player player, int maxDistance) {
        BlockIterator iterator = new BlockIterator(player, maxDistance);

        while (iterator.hasNext()) {
            Block block = iterator.next();
            if (!block.getType().isAir()) {
                return block;
            }
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        InfoGUI(commandSender);
        return false;
    }
}