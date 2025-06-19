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
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class SignListener implements Listener {
    public String itemName;
    util util = new util();
    boolean mBuying = false;

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        //erstellung vom schild
        String Preis = null;
        Player p = e.getPlayer();
        String player = p.getName();
        if (e.getLine(0).equalsIgnoreCase("[Shop]") && p.hasPermission("chestshop.create")) {
            if (e.getBlock().getType().toString().endsWith("_WALL_SIGN")) {
                Block attachedBlock = e.getBlock().getRelative(((org.bukkit.block.data.type.WallSign) e.getBlock().getBlockData()).getFacing().getOppositeFace());
                if (attachedBlock.getType() == Material.CHEST || attachedBlock.getType() == Material.TRAPPED_CHEST) {
                    BlockState state = attachedBlock.getState();

                    List<String> WBlacklist = ChestShop.getPlugin(ChestShop.class).getBlackWorlds();
                    if (WBlacklist.contains(p.getWorld().getName())) {
                        lang.getMessage("FalseWorld");
                    } else {
                        e.setLine(0, "§a[Shop]");
                        e.setLine(2, (player));
                        Preis = e.getLine(1);
                        int xCoord = e.getBlock().getLocation().getBlockX();
                        int yCoord = e.getBlock().getLocation().getBlockY();
                        int zCoord = e.getBlock().getLocation().getBlockZ();
                        String uuid = String.valueOf(e.getPlayer().getUniqueId());

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
                                if (!CheckBlackItems(itemName)) {
                                    p.sendMessage(lang.getMessage("ItemBlacklist"));
                                    itemName = "?";
                                }
                                e.setLine(3, itemName);
                                e.setLine(1, setPrice(e.getLine(1)));

                                ChestShop.getPlugin(ChestShop.class).addItemToShopConfig(p.getWorld().getName(), xCoord, yCoord, zCoord, itemName, p);
                                p.sendMessage(lang.getMessage("ShopCreateSells").replace("<item>", itemName).replace("<price>", Preis.replace(" (Buying)", "")));
                            } else {
                                e.setLine(3, "?");
                                String itemName = "Air";
                                e.setLine(1, setPrice(e.getLine(1)));
                                ChestShop.getPlugin(ChestShop.class).addItemToShopConfig(p.getWorld().getName(), xCoord, yCoord, zCoord, itemName, p);

                                p.sendMessage(lang.getMessage("ShopCreate"));
                            }
                        }
                    }
                }
            }
        } else if (e.getLine(0).equalsIgnoreCase("[aShop]") && p.hasPermission("chestshop.admincreate")) {
            List<String> WBlacklist = ChestShop.getPlugin(ChestShop.class).getBlackWorlds();
            BlockState state = null;
            if (e.getBlock().getType().toString().endsWith("_WALL_SIGN")) {
                Block attachedBlock = e.getBlock().getRelative(((org.bukkit.block.data.type.WallSign) e.getBlock().getBlockData()).getFacing().getOppositeFace());
                state = attachedBlock.getState();
            }

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

                e.setLine(1, setPrice(e.getLine(1)));

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
                        ChestShop.getPlugin(ChestShop.class).addItemToShopConfig(p.getWorld().getName(), xCoord, yCoord, zCoord, itemName, p);
                        p.sendMessage(lang.getMessage("ShopCreateSells").replace("<item>", itemName).replace("<price>", Preis.replace(" (Buying)", "")));
                    } else {
                        e.setLine(3, "?");
                        String itemName = "Air";
                        ChestShop.getPlugin(ChestShop.class).addItemToShopConfig(p.getWorld().getName(), xCoord, yCoord, zCoord, itemName, p);

                        p.sendMessage(lang.getMessage("ShopCreate"));
                    }
                } else if (e.getLine(3) == null || e.getLine(3).equalsIgnoreCase("")) {
                    e.setLine(3, "?");
                    String itemName = "Air";
                    ChestShop.getPlugin(ChestShop.class).addItemToShopConfig(p.getWorld().getName(), xCoord, yCoord, zCoord, itemName, p);

                    p.sendMessage(lang.getMessage("ShopCreate"));

                } else {
                    itemName = e.getLine(3);
                    ChestShop.getPlugin(ChestShop.class).addItemToShopConfig(p.getWorld().getName(), xCoord, yCoord, zCoord, itemName, p);
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

            if (sign.getLine(3).equalsIgnoreCase("?")) {
                e.setCancelled(true);
                if (sign.getLine(2).equalsIgnoreCase(player.getName()) || sign.getLine(2).equalsIgnoreCase("1")) {
                    if (player.getInventory().getItemInMainHand() != null && !player.getInventory().getItemInMainHand().equals(Material.AIR)) {
                        ItemStack item = player.getInventory().getItemInMainHand();
                        Material itemMat = item.getType();
                        if (!CheckBlackItems(itemMat.toString())) {
                            player.sendMessage(lang.getMessage("ItemBlacklist"));
                            e.setCancelled(true);
                            return;
                        }
                        if (item.hasItemMeta()) {
                            if (ChestShop.getPlugin(ChestShop.class).searchForItemStack(item) != null) {
                                itemName = ChestShop.getPlugin(ChestShop.class).searchForItemStack(item);
                            } else {
                                ChestShop.getPlugin(ChestShop.class).addcurrentnumber(item);
                                int currentID = ChestShop.getPlugin(ChestShop.class).curentID();
                                itemName = item.getType().toString() + "#" + currentID;
                            }
                        } else {
                            itemName = item.getType().toString();
                        }
                        int xCoord = sign.getBlock().getLocation().getBlockX();
                        int yCoord = sign.getBlock().getLocation().getBlockY();
                        int zCoord = sign.getBlock().getLocation().getBlockZ();

                        ChestShop.getPlugin(ChestShop.class).removeItemFromShopConfig(player.getWorld().getName(), xCoord, yCoord, zCoord);
                        ChestShop.getPlugin(ChestShop.class).addItemToShopConfig(player.getWorld().getName(), xCoord, yCoord, zCoord, itemName, player);
                        player.sendMessage(lang.getMessage("ShopCreateSells").replace("<item>", itemName).replace("<price>", sign.getLine(1).replace(" (Bought)", "")));
                        sign.setLine(3, itemName);
                        sign.update();
                    } else player.sendMessage(lang.getMessage("noItem"));
                } else player.sendMessage(lang.getMessage("noItem"));
                return;
            }

            if (sign.getLine(0).equalsIgnoreCase("§a[Adminshop]")) {
                e.setCancelled(true);
                if (player.hasPermission("chestshop.buy")) {
                    int xCoord = e.getClickedBlock().getLocation().getBlockX();
                    int yCoord = e.getClickedBlock().getLocation().getBlockY();
                    int zCoord = e.getClickedBlock().getLocation().getBlockZ();

                    Double Preis = Double.valueOf(getPrice(sign.getLine(1)));

                    Material material = Material.matchMaterial(ChestShop.getPlugin(ChestShop.class).getItemFromShopConfig(player.getWorld().getName(), xCoord, yCoord, zCoord));
                    String material1 = "OHOH";
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
                            item.setAmount(1);
                        } else {
                            item = new ItemStack(material, 1);
                        }
                        int amount = Integer.parseInt(sign.getLine(2));;
                        if (player.isSneaking()) {
                            Preis = (Preis / amount)*item.getMaxStackSize();
                            amount = item.getMaxStackSize();
                            item.setAmount(item.getMaxStackSize());
                        }
                        Economy economy = ChestShop.getEconomy();

                        if (isBuying(sign.getLine(1))) {
                            if (player.getInventory().contains(item.getType())) {
                                count = countItems(player.getInventory(), item);
                                if (count >= amount) {
                                    player.getInventory().removeItem(item);
                                    economy.depositPlayer(player, Preis);
                                        player.sendMessage(lang.getMessage("youhavesold").replace("<amount>", String.valueOf(amount)).replace("<item>", sign.getLine(3)).replace("<price>", String.valueOf(Preis)) + " (" + (count - amount) + ")");
                                } else player.sendMessage(lang.getMessage("notEngouthItems"));
                            } else player.sendMessage(lang.getMessage("notEngouthItems"));
                        } else {
                            if (economy.getBalance(player) >= Preis) { // Überprüfung auf ausreichendes Geld
                                economy.withdrawPlayer(player, Preis);
                                player.getInventory().addItem(item);
                                player.sendMessage(lang.getMessage("youhavebought").replace("<amount>", String.valueOf(amount)).replace("<item>", sign.getLine(3)).replace("<price>", String.valueOf(Preis)) + " (Adminshop)");
                            } else player.sendMessage(lang.getMessage("notEnoughMoney"));
                        }
                    } else player.sendMessage(lang.getMessage("invalidMaterial"));
                } else player.sendMessage(lang.getMessage("noPermission"));
            }

            if (sign.getLine(0).equalsIgnoreCase("§a[Shop]")) {
                e.setCancelled(true);
                if (player.hasPermission("chestshop.buy")) {

                    int xCoord = e.getClickedBlock().getLocation().getBlockX();
                    int yCoord = e.getClickedBlock().getLocation().getBlockY();
                    int zCoord = e.getClickedBlock().getLocation().getBlockZ();

                    OfflinePlayer offlineplayer = Bukkit.getOfflinePlayer(sign.getLine(2));
                    Double Preis = Double.valueOf(getPrice(sign.getLine(1)));

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
                        if (!CheckBlackItems(material.toString())) {
                            player.sendMessage(lang.getMessage("ItemBlacklist"));
                            e.setCancelled(true);
                            return;
                        }
                        Block chestBlock = clickedBlock.getRelative(((org.bukkit.block.data.type.WallSign) clickedBlock.getBlockData()).getFacing().getOppositeFace());
                        if (chestBlock.getState() instanceof Chest) {
                            Chest chest = (Chest) chestBlock.getState();
                            Inventory chestInventory = chest.getInventory();
                            ItemStack item;
                            if (ChestShop.getPlugin(ChestShop.class).IDCheck(material1) != null) {
                                item = ChestShop.getPlugin(ChestShop.class).getNBT(material1);
                                item.setAmount(amount);
                            } else {
                                item = new ItemStack(material, amount);
                            }
                            Economy economy = ChestShop.getEconomy();
                            if (isBuying(sign.getLine(1))) {
                                if (economy.getBalance(offlineplayer) >= Preis) {
                                    if (player.getInventory().contains(item.getType())) {
                                        count = countItems(player.getInventory(), item);
                                        if (count >= amount) {
                                            if (isNotFull(chest, item)) {
                                                player.getInventory().removeItem(item);


                                                economy.withdrawPlayer(offlineplayer, Preis);
                                                economy.depositPlayer(player, Preis);
                                                if (offlineplayer.isOnline() || !offlineplayer.equals(player)) {
                                                    offlineplayer.getPlayer().sendMessage(lang.getMessage("hassold").replace("<player>", player.getDisplayName()).replace("<item>", sign.getLine(3).toLowerCase()).replace("<amount>", String.valueOf(amount)));
                                                }
                                                player.sendMessage(lang.getMessage("youhassold").replace("<item>", sign.getLine(3).toLowerCase()).replace("<amount>", String.valueOf(amount)) + "(" + (count - amount) + ")");
                                            } else player.sendMessage(lang.getMessage("chestFull"));
                                        } else player.sendMessage(lang.getMessage("notEngouthItems"));
                                    } else player.sendMessage(lang.getMessage("notEngouthItems"));
                                } else player.sendMessage(lang.getMessage("BuyerNotEnoughMoney"));
                            } else {
                                count = countSpecalItems(chestInventory, item);
                                if (count >= amount) { // Überprüfung, ob mindestens ein Artikel vorhanden ist
                                    if (economy.getBalance(player) >= Preis) { // Überprüfung auf ausreichendes Geld
                                        chestInventory.removeItem(item);
                                        player.getInventory().addItem(item);

                                        economy.withdrawPlayer(player, Preis);
                                        economy.depositPlayer(offlineplayer, Preis);
                                        if (offlineplayer.isOnline() || !offlineplayer.equals(player)) {
                                            offlineplayer.getPlayer().sendMessage(lang.getMessage("hasbought").replace("<player>", player.getDisplayName()).replace("<item>", sign.getLine(3).toLowerCase()).replace("<amount>", String.valueOf(amount)));
                                        }
                                        player.sendMessage(lang.getMessage("youhasbought").replace("<item>", sign.getLine(3).toLowerCase()).replace("<amount>", String.valueOf(amount)) + "(" + (count - amount) + ")");
                                    } else player.sendMessage(lang.getMessage("notEnoughMoney"));
                                } else player.sendMessage(lang.getMessage("notEngouthItemsChest").replace("<item>", sign.getLine(3)));
                            }
                        } else player.sendMessage(lang.getMessage("noChestBehindSign"));
                    } else player.sendMessage(lang.getMessage("invalidMaterial"));
                } else player.sendMessage(lang.getMessage("noPermission"));
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
    private int countItems(Inventory inventory, ItemStack item) {
        Material mat = item.getType();
        int count = 0;
        for (ItemStack stack : inventory.getContents()) {
            if (stack != null && stack.getType() == mat) {
                System.out.println(stack.getItemMeta());
                System.out.println(item.getItemMeta());
                if (stack.getItemMeta().equals(item.getItemMeta())) {
                    System.out.println("test");
                    count += stack.getAmount();
                }
            }
        }
        return count;
    }
    private String FormtItems(String item) {
        item.replace("_", " ");
        return item;
    }
    private boolean CheckBlackItems(String itemm) {
        List<String> BlackItems = ChestShop.getPlugin(ChestShop.class).getBlackItems();
        for (String item : BlackItems) {
            Material blacklistedMaterial = Material.matchMaterial(item);
            String blacklistedItem = blacklistedMaterial.toString();
            if (itemm.equals(blacklistedItem)) return false;
        }
        return true;
    }

    private String setPrice(String price) {
        String Preis;
        Preis = price.replaceAll("[^0-9.bB ]", "x");
        if (price == null || price.equalsIgnoreCase("") || Preis.contains("x")) {
            Preis = String.valueOf(0);
            return "0";
        }
        Preis = Preis.replaceAll("[ ]", "");
        if (Preis.toLowerCase().contains("b")) {
            Preis = Preis.replaceAll("[bB]", "");
            Preis.replace(" (Buying)", "");
            return Preis + " (Buying)";
        }
        return Preis;
    }
    private boolean isBuying(String price) {
        if (price.contains(" (Buying)")) {
            return true;
        }
        return false;
    }
    private String getPrice(String price) {
        String Preis;
        if (price.contains(" (Buying)")) {
            Preis = price.replaceAll("[^0-9.]", "");
            return Preis;
        } else {
            Preis = price.replaceAll("[^0-9.]", "x");
            if (Preis.contains("x")) Preis = "0";
            return Preis;
        }
    }
    private boolean isNotFull(Chest chest, ItemStack item) {
        HashMap<Integer, ItemStack> rest = chest.getInventory().addItem(item);
        return rest.isEmpty();
    }
    private int getItemSlot(Inventory inv, Material mat) {
        int amount = 0;
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (item != null && item.getType() == mat) {
                return i;
            }
        }
        return 0;
    }



}