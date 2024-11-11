package de.minnivini.chestshop.commands;

import de.minnivini.chestshop.ChestShop;
import de.minnivini.chestshop.Util.util;
import de.minnivini.chestshop.Util.lang;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.List;

public class ShopInfo{
    List<Enchantment> enchantmentList = new ArrayList<>();
    List<Integer> enchantmentLevelList = new ArrayList<>();

    public void shopInfoCMD(CommandSender commandSender) {
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
                    player.sendMessage("§dShopinfo:");
                    player.sendMessage("    §b-Item: §e" + item.toLowerCase());
                    if (ChestShop.getPlugin(ChestShop.class).IDCheck(item) != null) {
                        ItemStack itemStack = ChestShop.getPlugin(ChestShop.class).getNBT(item);
                        if (itemStack.getItemMeta().hasDisplayName()) player.sendMessage("          §b-displayname: §e" + itemStack.getItemMeta().getDisplayName());
                        if (itemStack.getItemMeta().hasLore()) player.sendMessage("          §b-lore: §e" + itemStack.getItemMeta().getLore());
                        if (itemStack.getItemMeta().hasEnchants()) {
                            player.sendMessage("          §b-enchants: \n");
                            itemStack.getItemMeta().getEnchants().forEach((enchant, level) ->
                                    player.sendMessage("§e            -" + enchant.getKey().getKey().toLowerCase() + " " + level + "\n"));
                        }
                        if (itemStack.getItemMeta().hasLocalizedName()) player.sendMessage("        §bLocelizedname: §e" + itemStack.getItemMeta().getLocalizedName());
                        if (itemStack.getItemMeta().getItemFlags() != null){
                            player.sendMessage("         §bItemFlags: \n");
                            itemStack.getItemMeta().getItemFlags().forEach((itemFlag ->
                                    player.sendMessage("§e          -" + itemFlag)));
                        }
                        if (itemStack.getItemMeta().hasAttributeModifiers()) {
                            player.sendMessage("§b         Attribute: \n");
                            itemStack.getItemMeta().getAttributeModifiers().forEach((attribute, attributeModifier) ->
                                    player.sendMessage("§b          -" + attribute + " " + attributeModifier));
                        }
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