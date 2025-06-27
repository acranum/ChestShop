package de.minnivini.chestshop.listeners;

import de.minnivini.chestshop.ChestShop;
import de.minnivini.chestshop.Util.lang;
import de.minnivini.chestshop.Util.util;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class SignListener implements Listener {
    public String itemName;
    boolean mBuying = false;
    private final Map<UUID, Long> clickCooldown = new HashMap<>();

    ChestShop plugin = ChestShop.getPlugin(ChestShop.class);

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        //erstellung vom schild
        String Preis = null;
        Player p = e.getPlayer();
        String player = p.getName();
        if (e.getLine(0).equalsIgnoreCase("[Shop]") && p.hasPermission("chestshop.create")) {
            if (plugin.getShopconfig().getShopsFromPlayer(p) < util.getMaxShops(p)) {
                if (e.getBlock().getType().toString().endsWith("_WALL_SIGN")) {
                    Block attachedBlock = e.getBlock().getRelative(((org.bukkit.block.data.type.WallSign) e.getBlock().getBlockData()).getFacing().getOppositeFace());
                    if (attachedBlock.getType() == Material.CHEST || attachedBlock.getType() == Material.TRAPPED_CHEST) {
                        BlockState state = attachedBlock.getState();

                        List<String> WBlacklist = plugin.getDefaultConfig().getBlackWorlds();
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
                                        if (plugin.getItemconfig().searchForItemStack(firstItem) != null) {
                                            itemName = plugin.getItemconfig().searchForItemStack(firstItem);
                                        } else {
                                            plugin.getItemconfig().addCurrentNumber(firstItem);
                                            int currentID = plugin.getItemconfig().currentID();
                                            itemName = firstItem.getType().toString() + "#" + currentID;
                                        }
                                    } else {
                                        itemName = firstItem.getType().toString();
                                    }
                                    if (isBlacklisted(itemName)) {
                                        p.sendMessage(lang.getMessage("ItemBlacklist"));
                                        itemName = "?";
                                    }
                                    e.setLine(3, itemName);
                                    e.setLine(1, setPrice(e.getLine(1)));

                                    plugin.getShopconfig().addItemToShopConfig(p.getWorld().getName(), xCoord, yCoord, zCoord, itemName, p);
                                    p.sendMessage(lang.getMessage("ShopCreateSells").replace("<item>", itemName).replace("<price>", Preis).replace(" (Buying)", ""));
                                    plugin.getShopconfig().addShopToPlayer(p);
                                } else {
                                    e.setLine(3, "?");
                                    String itemName = "Air";
                                    e.setLine(1, setPrice(e.getLine(1)));
                                    plugin.getShopconfig().addItemToShopConfig(p.getWorld().getName(), xCoord, yCoord, zCoord, itemName, p);

                                    p.sendMessage(lang.getMessage("ShopCreate"));
                                    plugin.getShopconfig().addShopToPlayer(p);
                                }
                            }
                        }
                    }
                }
            } else p.sendMessage(lang.getMessage("maxShops").replace("<amount>", String.valueOf(plugin.getShopconfig().getShopsFromPlayer(p))));
        } else if (e.getLine(0).equalsIgnoreCase("[aShop]") && p.hasPermission("chestshop.admincreate")) {
            List<String> WBlacklist = plugin.getDefaultConfig().getBlackWorlds();
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
                            if (plugin.getItemconfig().searchForItemStack(firstItem) != null) {
                                itemName = plugin.getItemconfig().searchForItemStack(firstItem);
                            } else {
                                plugin.getItemconfig().addCurrentNumber(firstItem);
                                int currentID = plugin.getItemconfig().currentID();
                                itemName = firstItem.getType().toString() + "#" + currentID;
                            }
                        } else {
                            itemName = firstItem.getType().toString();
                        }
                        e.setLine(3, itemName);
                        plugin.getShopconfig().addItemToShopConfig(p.getWorld().getName(), xCoord, yCoord, zCoord, itemName, p);
                        p.sendMessage(lang.getMessage("ShopCreateSells").replace("<item>", itemName).replace("<price>", Preis.replace(" (Buying)", "")));
                    } else {
                        e.setLine(3, "?");
                        String itemName = "Air";
                        plugin.getShopconfig().addItemToShopConfig(p.getWorld().getName(), xCoord, yCoord, zCoord, itemName, p);

                        p.sendMessage(lang.getMessage("ShopCreate"));
                    }
                } else if (e.getLine(3) == null || e.getLine(3).equalsIgnoreCase("")) {
                    e.setLine(3, "?");
                    String itemName = "Air";
                    plugin.getShopconfig().addItemToShopConfig(p.getWorld().getName(), xCoord, yCoord, zCoord, itemName, p);

                    p.sendMessage(lang.getMessage("ShopCreate"));

                } else {
                    itemName = e.getLine(3);
                    plugin.getShopconfig().addItemToShopConfig(p.getWorld().getName(), xCoord, yCoord, zCoord, itemName, p);
                }
            }
        }
    }



    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        int count = 0;
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        Block clickedBlock = e.getClickedBlock();
        if (clickedBlock != null && e.getClickedBlock().getState() instanceof Sign) {
            Sign sign = (Sign) e.getClickedBlock().getState();

            if(!(e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
            long now = System.currentTimeMillis();
            if (clickCooldown.containsKey(uuid) && now - clickCooldown.get(uuid) < 50) { //Double click prevention //TODO: permission: chestshop.edit
                e.setCancelled(true);
                return;
            }
            if (clickCooldown.containsKey(uuid) && now - clickCooldown.get(uuid) < 300) { //Double click --> edit
                if (p.getName().equals(sign.getLine(2))) {
                    plugin.getShopconfig().removeShopFromPlayer(p);
                    return;
                }
            }
            clickCooldown.put(uuid, now);

            if (sign.getLine(3).equalsIgnoreCase("?")) {
                e.setCancelled(true);
                if (sign.getLine(2).equalsIgnoreCase(p.getName()) || validNumber(sign.getLine(2))) {
                    if (p.getInventory().getItemInMainHand() != null && !p.getInventory().getItemInMainHand().equals(Material.AIR)) {
                        ItemStack item = p.getInventory().getItemInMainHand();
                        Material itemMat = item.getType();
                        if (isBlacklisted(itemMat.toString())) {
                            p.sendMessage(lang.getMessage("ItemBlacklist"));
                            e.setCancelled(true);
                            return;
                        }
                        if (item.hasItemMeta()) {
                            if (plugin.getItemconfig().searchForItemStack(item) != null) {
                                itemName = plugin.getItemconfig().searchForItemStack(item);
                            } else {
                                plugin.getItemconfig().addCurrentNumber(item);
                                int currentID = plugin.getItemconfig().currentID();
                                itemName = item.getType().toString() + "#" + currentID;
                            }
                        } else {
                            itemName = item.getType().toString();
                        }
                        int xCoord = sign.getBlock().getLocation().getBlockX();
                        int yCoord = sign.getBlock().getLocation().getBlockY();
                        int zCoord = sign.getBlock().getLocation().getBlockZ();

                        plugin.getShopconfig().addItemToShopConfig(p.getWorld().getName(), xCoord, yCoord, zCoord, itemName, p);
                        p.sendMessage(lang.getMessage("ShopCreateSells").replace("<item>", itemName).replace("<price>", sign.getLine(1).replace(" (Bought)", "")));
                        sign.setLine(3, itemName);
                        sign.update();
                    } else p.sendMessage(lang.getMessage("noItem"));
                } else p.sendMessage(lang.getMessage("noItem"));
                return;
            }

            if (sign.getLine(0).equalsIgnoreCase("§a[Adminshop]")) {
                e.setCancelled(true);
                boolean idMat = false;
                int xCoord = e.getClickedBlock().getLocation().getBlockX();
                int yCoord = e.getClickedBlock().getLocation().getBlockY();
                int zCoord = e.getClickedBlock().getLocation().getBlockZ();

                Double Preis = Double.valueOf(getPrice(sign.getLine(1)));

                Material material = Material.matchMaterial(plugin.getShopconfig().getItemFromShopConfig(p.getWorld().getName(), xCoord, yCoord, zCoord));
                String material1 = "OHOH";
                if (material == null) {
                    String Realmaterial = plugin.getShopconfig().getItemFromShopConfig(p.getWorld().getName(), xCoord, yCoord, zCoord);
                    if (plugin.getItemconfig().IDCheck(Realmaterial) != null) {
                        material1 = Realmaterial;
                        idMat = true;
                    }
                }
                if (material != null && material != Material.AIR || idMat) {
                    ItemStack item;
                    if (plugin.getItemconfig().IDCheck(material1) != null) {
                        item = plugin.getItemconfig().getNBT(material1);
                        item.setAmount(1);
                    } else {
                        item = new ItemStack(material, 1);
                    }
                    int amount = Integer.parseInt(sign.getLine(2));;
                    if (p.isSneaking()) {
                        Preis = (Preis / amount)*item.getMaxStackSize();
                        amount = item.getMaxStackSize();
                    }
                    item.setAmount(amount);
                    Economy economy = ChestShop.getEconomy();

                    if (isBuying(sign.getLine(1))) {
                        if (p.hasPermission("chestshop.sell")) {
                            if (p.getInventory().contains(item.getType())) {
                                count = countItems(p.getInventory(), item);
                                if (count >= amount) {
                                    p.getInventory().removeItem(item);
                                    economy.depositPlayer(p, Preis);
                                    p.sendMessage(lang.getMessage("youhavesold").replace("<amount>", String.valueOf(amount)).replace("<item>", sign.getLine(3)).replace("<price>", String.valueOf(Preis)) + " (" + (count - amount) + ")");
                                } else p.sendMessage(lang.getMessage("notEngouthItems"));
                            } else p.sendMessage(lang.getMessage("notEngouthItems"));
                        } else p.sendMessage(lang.getMessage("noPermission"));
                    } else {
                        if (p.hasPermission("chestshop.buy")) {
                            if (inventoryfull(p.getInventory())) {
                                p.sendTitle(lang.getMessage("inventoryFull"), "");
                                return;
                            }
                            if (economy.getBalance(p) >= Preis) { // Überprüfung auf ausreichendes Geld
                                economy.withdrawPlayer(p, Preis);
                                p.getInventory().addItem(item);
                                p.sendMessage(lang.getMessage("youhavebought").replace("<amount>", String.valueOf(amount)).replace("<item>", sign.getLine(3)).replace("<price>", String.valueOf(Preis)) + " (Adminshop)");
                            } else p.sendMessage(lang.getMessage("notEnoughMoney"));
                        } else p.sendMessage(lang.getMessage("noPermission"));
                    }
                } else p.sendMessage(lang.getMessage("invalidMaterial"));
            }

            if (sign.getLine(0).equalsIgnoreCase("§a[Shop]")) {
                e.setCancelled(true);
                if (p.hasPermission("chestshop.buy")) {

                    int xCoord = e.getClickedBlock().getLocation().getBlockX();
                    int yCoord = e.getClickedBlock().getLocation().getBlockY();
                    int zCoord = e.getClickedBlock().getLocation().getBlockZ();

                    OfflinePlayer offlineplayer = Bukkit.getOfflinePlayer(sign.getLine(2));
                    Double Preis = Double.valueOf(getPrice(sign.getLine(1)));

                    Material material = Material.matchMaterial(plugin.getShopconfig().getItemFromShopConfig(p.getWorld().getName(), xCoord, yCoord, zCoord));
                    String material1 = "OHOH";
                    if (material == null) {
                        String Realmaterial = plugin.getShopconfig().getItemFromShopConfig(p.getWorld().getName(), xCoord, yCoord, zCoord);
                        if (plugin.getItemconfig().IDCheck(Realmaterial) != null) {
                            material1 = Realmaterial;
                            material = Material.DIRT;

                        }
                    }
                    if (material != null && material != Material.AIR) {
                        if (isBlacklisted(material.toString())) {
                            p.sendMessage(lang.getMessage("ItemBlacklist"));
                            e.setCancelled(true);
                            return;
                        }
                        Block chestBlock = clickedBlock.getRelative(((org.bukkit.block.data.type.WallSign) clickedBlock.getBlockData()).getFacing().getOppositeFace());
                        if (chestBlock.getState() instanceof Chest) {
                            Chest chest = (Chest) chestBlock.getState();
                            Inventory chestInventory = chest.getInventory();
                            ItemStack item;
                            if (plugin.getItemconfig().IDCheck(material1) != null) {
                                item = plugin.getItemconfig().getNBT(material1);
                                item.setAmount(1);
                            } else {
                                item = new ItemStack(material, 1);
                            }
                            int amount = 1;
                            if (p.isSneaking()) {
                                Preis = Preis*item.getMaxStackSize();
                                amount = item.getMaxStackSize();
                            }
                            item.setAmount(amount);
                            Economy economy = ChestShop.getEconomy();
                            if (isBuying(sign.getLine(1))) {
                                if (p.hasPermission("chestshop.sell")) {
                                    if (economy.getBalance(offlineplayer) >= Preis) {
                                        if (p.getInventory().contains(item.getType())) {
                                            count = countItems(p.getInventory(), item);
                                            if (count >= amount) {
                                                if (isNotFull(chest, item)) {
                                                    p.getInventory().removeItem(item);

                                                    economy.withdrawPlayer(offlineplayer, Preis);
                                                    economy.depositPlayer(p, Preis);
                                                    if (offlineplayer.isOnline() || !offlineplayer.equals(p)) {
                                                        offlineplayer.getPlayer().sendMessage(lang.getMessage("hassold").replace("<player>", p.getDisplayName()).replace("<item>", sign.getLine(3).toLowerCase()).replace("<amount>", String.valueOf(amount)));
                                                    }
                                                    p.sendMessage(lang.getMessage("youhassold").replace("<item>", sign.getLine(3).toLowerCase()).replace("<amount>", String.valueOf(amount)) + "(" + (count - amount) + ")");
                                                } else p.sendMessage(lang.getMessage("chestFull"));
                                            } else p.sendMessage(lang.getMessage("notEngouthItems"));
                                        } else p.sendMessage(lang.getMessage("notEngouthItems"));
                                    } else p.sendMessage(lang.getMessage("BuyerNotEnoughMoney"));
                                } else p.sendMessage(lang.getMessage("noPermission"));
                            } else {
                                if (p.hasPermission("chestshop.buy")) {
                                    if (inventoryfull(p.getInventory())) {
                                        p.sendTitle(lang.getMessage("inventoryFull"), "");
                                        return;
                                    }
                                    count = countSpecalItems(chestInventory, item);
                                    if (count >= amount) { // Check if <amount> item is in chest
                                        if (economy.getBalance(p) >= Preis) { // Überprüfung auf ausreichendes Geld
                                            chestInventory.removeItem(item);
                                            p.getInventory().addItem(item);

                                            economy.withdrawPlayer(p, Preis);
                                            economy.depositPlayer(offlineplayer, Preis);
                                            if (offlineplayer.isOnline() || !offlineplayer.equals(p)) {
                                                offlineplayer.getPlayer().sendMessage(lang.getMessage("hasbought").replace("<player>", p.getDisplayName()).replace("<item>", sign.getLine(3).toLowerCase()).replace("<amount>", String.valueOf(amount)));
                                            }
                                            p.sendMessage(lang.getMessage("youhasbought").replace("<item>", sign.getLine(3).toLowerCase()).replace("<amount>", String.valueOf(amount)) + "(" + (count - amount) + ")");
                                        } else p.sendMessage(lang.getMessage("notEnoughMoney"));
                                    } else p.sendMessage(lang.getMessage("notEngouthItemsChest").replace("<item>", sign.getLine(3)));
                                } else p.sendMessage(lang.getMessage("noPermission"));
                            }
                        } else p.sendMessage(lang.getMessage("noChestBehindSign"));
                    } else p.sendMessage(lang.getMessage("invalidMaterial"));
                } else p.sendMessage(lang.getMessage("noPermission"));
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
                if (stack.getItemMeta().equals(item.getItemMeta())) {
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
    private boolean isBlacklisted(String itemStack) {
        Material material = Material.getMaterial(itemStack);
        if (material == null) {
            return false;
        }

        return plugin.getDefaultConfig().getBlacklistedItems().contains(material);
    }

    private String setPrice(String price) {
        String Preis;
        Preis = price.replaceAll("[$]", "");
        Preis = Preis.replaceAll("[^0-9.bB ]", "x");
        if (price == null || price.equalsIgnoreCase("") || Preis.contains("x")) {
            Preis = String.valueOf(0);
            return "0" + lang.getMessage("$");
        }
        Preis = Preis.replaceAll("[ ]", "");
        if (Preis.toLowerCase().contains("b")) {
            Preis = Preis.replaceAll("[bB]", "");
            Preis.replace(" (Buying)", "");
            return Preis + lang.getMessage("$") + " (Buying)";
        }
        return Preis + lang.getMessage("$");
    }
    private boolean isBuying(String price) {
        if (price.contains(" (Buying)")) {
            return true;
        }
        return false;
    }
    private String getPrice(String price) {
        String Preis;
        Preis = price.replace(" (Buying)", "").replaceAll("[ ]", "").replaceAll("[$]", "");
        System.out.println(Preis);
        Preis = Preis.replaceAll("[^0-9.]", "x");
        if (Preis.contains("x")) Preis = "0";
        return Preis;
    }
    private boolean validNumber(String input) {
        if (input == null || input.isEmpty()) return false;
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {return false;}
    }
    private boolean isNotFull(Chest chest, ItemStack item) {
        HashMap<Integer, ItemStack> rest = chest.getInventory().addItem(item);
        return rest.isEmpty();
    }
    private boolean inventoryfull(Inventory inv) {
        if (inv.firstEmpty() == -1) return true;
        return false;
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