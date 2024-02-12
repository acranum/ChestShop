package de.minnivini.chestshop.listeners;

import de.minnivini.chestshop.ChestShop;
import de.minnivini.chestshop.Util.lang;
import de.minnivini.chestshop.Util.util;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.C;

import java.nio.Buffer;
import java.util.List;
import java.util.UUID;

public class SignListener implements Listener {
    public String itemName;
    util util = new util();

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        //erstellung vom schild
        String Preis = null;
        Player p = e.getPlayer();
        String player = e.getPlayer().getName();

        if (e.getBlock().getType().toString().endsWith("_WALL_SIGN")) {
            Block attachedBlock = e.getBlock().getRelative(((org.bukkit.block.data.type.WallSign) e.getBlock().getBlockData()).getFacing().getOppositeFace());
            if (attachedBlock.getType() == Material.CHEST || attachedBlock.getType() == Material.TRAPPED_CHEST) {
                BlockState state = attachedBlock.getState();

                if (e.getLine(0).equalsIgnoreCase("[Shop]") && p.hasPermission("chestshop.create")){
                    List<String> WBlacklist = ChestShop.getPlugin(ChestShop.class).getBlackWorlds();
                    if (WBlacklist.contains(p.getWorld().getName())) {
                        lang.getMessage("FalseWorld");
                    } else {
                        e.setLine(0, "§a[Shop]");
                        e.setLine(2, "IcBinKakaWuzt");
                        Preis = e.getLine(1);
                        int xCoord = e.getBlock().getLocation().getBlockX();
                        int yCoord = e.getBlock().getLocation().getBlockY();
                        int zCoord = e.getBlock().getLocation().getBlockZ();
                        String uuid = String.valueOf(e.getPlayer().getUniqueId());
                        //get first item
                        if (state instanceof Chest) {
                            Chest chest = (Chest) state;
                            Inventory chestInventory = chest.getBlockInventory();
                            ItemStack[] contents = chestInventory.getContents();

                            if (contents.length > 0 && contents[0] != null) {
                                ItemStack firstItem = contents[0];
                                if (firstItem.hasItemMeta()) {
                                    if (ChestShop.getPlugin(ChestShop.class).searchForItemStack(firstItem) != null) {
                                        itemName = ChestShop.getPlugin(ChestShop.class).searchForItemStack(firstItem);
                                    } else {
                                        ChestShop.getPlugin(ChestShop.class).addcurrentnumber(firstItem);
                                        int currentID = ChestShop.getPlugin(ChestShop.class).curentID();
                                        itemName = firstItem.getType().toString() + "#" + currentID;
                                    }
                                } else {itemName = firstItem.getType().toString();}
                                e.setLine(3, itemName);
                                if (e.getLine(1) == null || e.getLine(1).equalsIgnoreCase("")) {
                                    Preis = String.valueOf(0);
                                    e.setLine(1, "0");
                                }
                                ChestShop.getPlugin(ChestShop.class).addItemToShopConfig(p.getWorld().getName(), xCoord, yCoord, zCoord, itemName, p);
                                p.sendMessage(lang.getMessage("ShopCreatewitch") + itemName + lang.getMessage("for") + Preis + lang.getMessage("sells"));
                            } else {
                                e.setLine(3, "Air");
                                String itemName = "Air";
                                if (e.getLine(1) == null || e.getLine(1).equalsIgnoreCase("")) {
                                    Preis = String.valueOf(0);
                                    e.setLine(1, "0");
                                }
                                if (e.getLine(1) == null || e.getLine(1) == "") {
                                    e.setLine(1, "0");
                                }
                                ChestShop.getPlugin(ChestShop.class).addItemToShopConfig(p.getWorld().getName(), xCoord, yCoord, zCoord, itemName, p);

                                p.sendMessage(lang.getMessage("ShopCreate"));
                            }
                        }
                    }
                } else if (e.getLine(0).equalsIgnoreCase("[aShop]") && p.hasPermission("chestshop.admincreate")) {
                    List<String> WBlacklist = ChestShop.getPlugin(ChestShop.class).getBlackWorlds();
                    if (WBlacklist.contains(p.getWorld().getName())) {
                        lang.getMessage("FalseWorld");
                    } else {
                        e.setLine(0, "§a[Adminshop]");
                        String amount = e.getLine(2);
                        if (e.getLine(2) == null || !e.getLine(2).matches("\\d+") || e.getLine(2) == "0") {
                            e.setLine(2, "1");
                        }
                        Preis = e.getLine(1);
                        int xCoord = e.getBlock().getLocation().getBlockX();
                        int yCoord = e.getBlock().getLocation().getBlockY();
                        int zCoord = e.getBlock().getLocation().getBlockZ();
                        String uuid = String.valueOf(e.getPlayer().getUniqueId());
                        //get first item
                        if (state instanceof Chest) {
                            Chest chest = (Chest) state;
                            Inventory chestInventory = chest.getBlockInventory();
                            ItemStack[] contents = chestInventory.getContents();

                            if (contents.length > 0 && contents[0] != null) {
                                ItemStack firstItem = contents[0];
                                if (firstItem.hasItemMeta()) {
                                    if (ChestShop.getPlugin(ChestShop.class).searchForItemStack(firstItem) != null) {
                                        itemName = ChestShop.getPlugin(ChestShop.class).searchForItemStack(firstItem);
                                    } else {
                                        ChestShop.getPlugin(ChestShop.class).addcurrentnumber(firstItem);
                                        int currentID = ChestShop.getPlugin(ChestShop.class).curentID();
                                        itemName = firstItem.getType().toString() + "#" + currentID;
                                    }
                                } else {
                                    itemName = firstItem.getType().toString();
                                }
                                e.setLine(3, itemName);
                                if (e.getLine(1) == null || e.getLine(1).equalsIgnoreCase("") || !e.getLine(1).matches("\\d+")) {
                                    Preis = String.valueOf(0);
                                    e.setLine(1, "0");
                                }
                                ChestShop.getPlugin(ChestShop.class).addItemToShopConfig(p.getWorld().getName(), xCoord, yCoord, zCoord, itemName, p);
                                p.sendMessage(lang.getMessage("ShopCreatewitch") + itemName + lang.getMessage("for") + Preis + lang.getMessage("sells"));
                            } else {
                                e.setLine(3, "Air");
                                String itemName = "Air";
                                if (e.getLine(1) == null || e.getLine(1).equalsIgnoreCase("") || !e.getLine(1).matches("\\d+")) {
                                    Preis = String.valueOf(0);
                                    e.setLine(1, "0");
                                }
                                if (e.getLine(1) == null || e.getLine(1) == "") {
                                    e.setLine(1, "0");
                                }
                                ChestShop.getPlugin(ChestShop.class).addItemToShopConfig(p.getWorld().getName(), xCoord, yCoord, zCoord, itemName, p);

                                p.sendMessage(lang.getMessage("ShopCreate"));
                            }
                        }
                    }
                }
            }
        }
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        int count = 0;
        Player player = e.getPlayer();
        if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        Block clickedBlock = e.getClickedBlock();
        if (clickedBlock != null && e.getClickedBlock().getState() instanceof Sign) {
            Sign sign = (Sign) e.getClickedBlock().getState();
            if (sign.getLine(0).equalsIgnoreCase("§a[Adminshop]")) {
                e.setCancelled(true);
                if (player.hasPermission("chestshop.buy")) {
                    int xCoord = e.getClickedBlock().getLocation().getBlockX();
                    int yCoord = e.getClickedBlock().getLocation().getBlockY();
                    int zCoord = e.getClickedBlock().getLocation().getBlockZ();

                    Double Preis = Double.valueOf(sign.getLine(1));

                    Material material = Material.matchMaterial(ChestShop.getPlugin(ChestShop.class).getItemFromShopConfig(player.getWorld().getName(), xCoord, yCoord, zCoord));
                    String material1 = "OHOH";
                    int amount;
                    amount = Integer.parseInt(sign.getLine(2));
                    if (material == null) {
                        String Realmaterial = ChestShop.getPlugin(ChestShop.class).getItemFromShopConfig(player.getWorld().getName(), xCoord, yCoord, zCoord);
                        if (ChestShop.getPlugin(ChestShop.class).IDCheck(Realmaterial) != null) {
                            material1 = Realmaterial;
                            material = Material.DIRT;
                        }
                    }
                    if (material != null && material != Material.AIR) {
                        ItemStack item;
                        if (ChestShop.getPlugin(ChestShop.class).IDCheck(material1) != null) {
                            item = ChestShop.getPlugin(ChestShop.class).getNBT(material1);
                            item.setAmount(amount);
                        } else {
                            item = new ItemStack(material, amount);
                        }
                        Economy economy = ChestShop.getEconomy();
                        if (economy.getBalance(player) >= Preis) { // Überprüfung auf ausreichendes Geld
                            economy.withdrawPlayer(player, Preis);
                            player.getInventory().addItem(item);
                            player.sendMessage(lang.getMessage("youHave") + amount + " " + sign.getLine(3) + lang.getMessage("for") + Preis + lang.getMessage("bought") + "Adminshop)");
                        } else {
                            player.sendMessage(lang.getMessage("notEnoughMoney"));
                        }
                    } else {
                        player.sendMessage(lang.getMessage("invalidMaterial"));
                    }
                } else {
                    player.sendMessage(lang.getMessage("noPermission"));
                }
            }

            if (sign.getLine(0).equalsIgnoreCase("§a[Shop]")) {
                e.setCancelled(true);
                if (player.hasPermission("chestshop.buy")) {

                    int xCoord = e.getClickedBlock().getLocation().getBlockX();
                    int yCoord = e.getClickedBlock().getLocation().getBlockY();
                    int zCoord = e.getClickedBlock().getLocation().getBlockZ();

                    OfflinePlayer offlineplayer = Bukkit.getOfflinePlayer(sign.getLine(2));
                    Double Preis = Double.valueOf(sign.getLine(1));

                    Material material = Material.matchMaterial(ChestShop.getPlugin(ChestShop.class).getItemFromShopConfig(player.getWorld().getName(), xCoord, yCoord, zCoord));
                    String material1 = "OHOH";
                    int amount;
                    if (player.isSneaking()) {
                        amount = 64;
                        Preis = 64 * Preis;
                    } else amount = 1;
                    if (material == null) {
                        String Realmaterial = ChestShop.getPlugin(ChestShop.class).getItemFromShopConfig(player.getWorld().getName(), xCoord, yCoord, zCoord);
                        if (ChestShop.getPlugin(ChestShop.class).IDCheck(Realmaterial) != null) {
                            material1 = Realmaterial;
                            material = Material.DIRT;

                        }
                    }
                    if (material != null && material != Material.AIR) {
                        Block chestBlock = clickedBlock.getRelative(((org.bukkit.block.data.type.WallSign) clickedBlock.getBlockData()).getFacing().getOppositeFace());
                        if (chestBlock.getState() instanceof Chest) {
                            Chest chest = (Chest) chestBlock.getState();
                            Inventory chestInventory = chest.getInventory();
                            ItemStack item;
                            if (ChestShop.getPlugin(ChestShop.class).IDCheck(material1) != null) {
                                item = ChestShop.getPlugin(ChestShop.class).getNBT(material1);
                                item.setAmount(amount);
                                count = countSpecalItems(chestInventory, item);
                            } else {
                                item = new ItemStack(material, amount);
                                count = countItems(chestInventory, material);
                            }
                            if (count >= amount) { // Überprüfung, ob mindestens ein Artikel vorhanden ist
                                Economy economy = ChestShop.getEconomy();
                                if (economy.getBalance(player) >= Preis) { // Überprüfung auf ausreichendes Geld
                                    economy.withdrawPlayer(player, Preis);
                                    economy.depositPlayer(offlineplayer, Preis);
                                    //offlineplayer.getPlayer().sendMessage(player + lang.getMessage("hasbought" + sign.getLine(3)));
                                    chestInventory.removeItem(item);
                                    player.getInventory().addItem(item);
                                    player.sendMessage(lang.getMessage("youHave") + amount + " " + sign.getLine(3) + lang.getMessage("for") + Preis + lang.getMessage("bought") + (count - amount) + ")");
                                } else {
                                    player.sendMessage(lang.getMessage("notEnoughMoney"));
                                }
                            } else {
                                player.sendMessage(lang.getMessage("notEnough") + sign.getLine(3) + lang.getMessage("inChest"));
                            }
                        } else {
                            //player.sendMessage(lang.getMessage("noChestBehindSign"));
                        }
                    } else {
                        player.sendMessage(lang.getMessage("invalidMaterial"));
                    }
                } else {
                    player.sendMessage(lang.getMessage("noPermission"));
                }
            }
        }
    }
    private int countSpecalItems(Inventory inventory, ItemStack itemToSearch) {
        int count = 0;
        for (ItemStack stack : inventory.getContents()) {
            if (stack != null && stack.isSimilar(itemToSearch)) {
                count += stack.getAmount();
            }
        }
        return count;
    }
    private int countItems(Inventory inventory, Material material) {
        int count = 0;
        for (ItemStack stack : inventory.getContents()) {
            if (stack != null && stack.getType() == material) {
                count += stack.getAmount();
            }
        }
        return count;
    }
    private String FormtItems(String item) {
        item.replace("_", " ");
        return item;
    }


}