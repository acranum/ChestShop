package de.acranum.chestshop.listeners;

import de.acranum.chestshop.api.events.PostShopCreateEvent;
import de.acranum.chestshop.api.events.ShopCreateEvent;
import de.acranum.chestshop.api.events.ShopDestroyEvent;
import de.acranum.chestshop.api.shop.DestroyReason;
import de.acranum.chestshop.api.shop.Shop;
import de.acranum.chestshop.api.shop.ShopType;
import de.acranum.chestshop.ChestShop;
import de.acranum.chestshop.Util.lang;
import de.acranum.chestshop.Util.util;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
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
    private final Map<UUID, Long> clickCooldown = new HashMap<>();

    private final ChestShop plugin = ChestShop.getInstance();


    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        String price = setPrice(e.getLine(1));
        Player p = e.getPlayer();
        if (e.getLine(0).equalsIgnoreCase("[Shop]") && p.hasPermission("chestshop.create") || (e.getLine(0).equalsIgnoreCase("shop")) && p.hasPermission("chestshop.create") || (e.getLine(0).equalsIgnoreCase(ChatColor.stripColor(lang.getMessage("SignTitle")).replaceAll("[\\[\\]]", ""))) && p.hasPermission("chestshop.create")) {
            int amount = getAmount(e.getLine(3));

            if (plugin.getShopconfig().getShopsFromPlayer(p) >= util.getMaxShops(p) && util.getMaxShops(p) != -1) {
                p.sendMessage(lang.getMessage("maxShops").replace("<amount>", String.valueOf(util.getMaxShops(p))));
                return;
            }
            if (!e.getBlock().getType().toString().endsWith("_WALL_SIGN")) {
                return;
            }
            Block attachedBlock = e.getBlock().getRelative(((org.bukkit.block.data.type.WallSign) e.getBlock().getBlockData()).getFacing().getOppositeFace());
            if (!(attachedBlock.getType() == Material.CHEST) && !(attachedBlock.getType() == Material.TRAPPED_CHEST)) {
                return;
            }
            BlockState state = attachedBlock.getState();
            List<String> WorldsBlacklist = plugin.getDefaultConfig().getBlackWorlds();

            if (WorldsBlacklist.contains(p.getWorld().getName())) {
                p.sendMessage(lang.getMessage("FalseWorld"));
                return;
            }
            ItemStack foundItem = null;
            if (state instanceof Chest chest) {
                ItemStack[] contents = chest.getBlockInventory().getContents();

                if (contents.length > 0) {
                    for (ItemStack item : contents) {
                        if (item != null) {
                            foundItem = item;
                            break;
                        }
                    }
                    if (foundItem != null) {
                        if (foundItem.hasItemMeta()) {
                            String Item = plugin.getItemconfig().searchForItemStack(foundItem);
                            if (Item != null) {
                                itemName = Item;
                            } else {
                                plugin.getItemconfig().addCurrentID(foundItem);
                                int currentID = plugin.getItemconfig().getCurrentID();
                                itemName = foundItem.getType() + "#" + currentID;
                            }
                        } else itemName = foundItem.getType().toString();
                        if (isBlacklisted(itemName)) {
                            p.sendMessage(lang.getMessage("ItemBlacklist"));
                            itemName = "?";
                        }
                        System.out.println("1");
                    } else if (Material.matchMaterial(ChatColor.stripColor(e.getLine(3).replaceAll("[0-9x ]", ""))) != null) {
                        itemName = String.valueOf(Material.matchMaterial(ChatColor.stripColor(e.getLine(3).replaceAll("[0-9x ]", ""))));
                        foundItem = new ItemStack(Material.matchMaterial(itemName));
                    } else itemName = "?";
                }else if (Material.matchMaterial(ChatColor.stripColor(e.getLine(3).replaceAll("[0-9x ]", ""))) != null) {
                    itemName = String.valueOf(Material.matchMaterial(ChatColor.stripColor(e.getLine(3).replaceAll("[0-9x ]", ""))));
                    foundItem = new ItemStack(Material.matchMaterial(itemName));
                } else itemName = "?";
            }
            PlayerShopCreate(e, Bukkit.getOfflinePlayer(p.getUniqueId()), p, itemName, amount, price, foundItem);
        } else if (e.getLine(0).equalsIgnoreCase("[aShop]") && p.hasPermission("chestshop.admincreate") || (e.getLine(0).equalsIgnoreCase("aShop")) && p.hasPermission("chestshop.admincreate") || e.getLine(0).replaceAll("[\\[\\]]", "").equalsIgnoreCase(ChatColor.stripColor(lang.getMessage("AdminSignTitle")).replaceAll("[\\[\\]]", "")) && p.hasPermission("chestshop.admincreate")) {
            BlockState state = null;

            if (e.getBlock().getType().toString().endsWith("_WALL_SIGN")) {
                Block attachedBlock = e.getBlock().getRelative(((org.bukkit.block.data.type.WallSign) e.getBlock().getBlockData()).getFacing().getOppositeFace());
                state = attachedBlock.getState();
            }
            int amount = getAmount(e.getLine(2));

            ItemStack foundItem = null;
            if (state instanceof Chest chest) {
                ItemStack[] contents = chest.getBlockInventory().getContents();

                if (contents.length > 0) {
                    for (ItemStack item : contents) {
                        if (item != null) {
                            foundItem = item;
                            break;
                        }
                    }
                    if (foundItem != null) {
                        if (foundItem.hasItemMeta()) {
                            String Item = plugin.getItemconfig().searchForItemStack(foundItem);
                            if (Item != null) {
                                itemName = Item;
                            } else {
                                plugin.getItemconfig().addCurrentID(foundItem);
                                int currentID = plugin.getItemconfig().getCurrentID();
                                itemName = foundItem.getType() + "#" + currentID;
                            }
                        } else itemName = foundItem.getType().toString();
                        if (isBlacklisted(itemName)) {
                            p.sendMessage(lang.getMessage("ItemBlacklist"));
                            itemName = "?";
                        }
                    } else itemName = "?";
                } else itemName = "?";
            } else itemName = "?";

            if (amount == 0) {
                amount = 1;
            }
            AdminShopCreate(e, p, itemName, price, amount, foundItem);
        }
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        int count;
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        Block clickedBlock = e.getClickedBlock();
        Economy economy = ChestShop.getEconomy();

        if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        if (clickedBlock == null || !(clickedBlock.getState() instanceof Sign)) return;
        if (plugin.getShopconfig().getShop(e.getClickedBlock().getLocation()) == null) return;

        Sign sign = (Sign) clickedBlock.getState();

        long now = System.currentTimeMillis();
        if (clickCooldown.containsKey(uuid) && now - clickCooldown.get(uuid) < 50) { //Double buy prevention
            e.setCancelled(true);
            return;
        }
        if (clickCooldown.containsKey(uuid) && now - clickCooldown.get(uuid) < 300) { //Double click --> edit
            if (!p.getName().equals(sign.getLine(2)) && !p.hasPermission("chestshop.admincreate")) {
                e.setCancelled(true);
                return;
            }

            ShopDestroyEvent shopDestroyEvent = new ShopDestroyEvent(new Shop(clickedBlock, plugin.getShopconfig().getShopType(clickedBlock.getLocation()), plugin.getShopconfig().getPrice(clickedBlock.getLocation()), plugin.getShopconfig().getOwner(clickedBlock.getLocation()), plugin.getShopconfig().NameToItem(plugin.getShopconfig().getItemName(clickedBlock.getLocation())), plugin.getShopconfig().getAmount(clickedBlock.getLocation())), p, DestroyReason.EDIT);
            Bukkit.getPluginManager().callEvent(shopDestroyEvent);
            if (shopDestroyEvent.isCancelled()) {
                e.setCancelled(true);
                return;
            }
            plugin.getShopconfig().removeShopFromPlayer(p);

            return;
        }
        clickCooldown.put(uuid, now);

        if (sign.getLine(3).contains("?") && sign.getLine(0).equalsIgnoreCase(lang.getMessage("SignTitle")) || sign.getLine(3).contains("?") && sign.getLine(0).equalsIgnoreCase(lang.getMessage("AdminSignTitle"))) {
            e.setCancelled(true);
            if (sign.getLine(0).equalsIgnoreCase(lang.getMessage("AdminSignTitle")) && !e.getPlayer().hasPermission("chestshop.admincreate")) {
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(lang.getMessage("noPermission")));
                return;
            }
            if (sign.getLine(0).equalsIgnoreCase(lang.getMessage("SignTitle")) && !e.getPlayer().hasPermission("chestshop.create")) {
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(lang.getMessage("noPermission")));
                return;
            }
            if (!sign.getLine(2).equalsIgnoreCase(p.getName()) && !validNumber(sign.getLine(2)) || p.getInventory().getItemInMainHand() == null || p.getInventory().getItemInMainHand().getType() == Material.AIR) {
                p.sendMessage(lang.getMessage("noItem"));
                return;
            }

            ItemStack item = p.getInventory().getItemInMainHand();
            if (isBlacklisted(item.getType().toString())) {
                p.sendMessage(lang.getMessage("ItemBlacklist"));
                e.setCancelled(true);
                return;
            }
            if (item.hasItemMeta()) {
                if (plugin.getItemconfig().searchForItemStack(item) != null) {
                    itemName = plugin.getItemconfig().searchForItemStack(item);
                } else {
                    plugin.getItemconfig().addCurrentID(item);
                    int currentID = plugin.getItemconfig().getCurrentID();
                    itemName = item.getType() + "#" + currentID;
                }
            } else {
                itemName = item.getType().toString();
            }

            ShopType shopType = plugin.getShopconfig().getShopType(e.getClickedBlock().getLocation());
            String price = plugin.getShopconfig().getPrice(e.getClickedBlock().getLocation());
            OfflinePlayer owner = plugin.getShopconfig().getOwner(e.getClickedBlock().getLocation());
            int amount = plugin.getShopconfig().getAmount(e.getClickedBlock().getLocation());

            //ShopDestroyEvent
            ShopDestroyEvent shopDestroyEvent = new ShopDestroyEvent(new Shop(clickedBlock, shopType, price, owner, item, amount), p, null);
            Bukkit.getPluginManager().callEvent(shopDestroyEvent);
            if (shopDestroyEvent.isCancelled()) return;

            plugin.getShopconfig().RemoveShopFromShopConfig(e.getClickedBlock().getLocation());

            //ShopCreateEvent
            ShopCreateEvent shopCreateEvent = new ShopCreateEvent(new Shop(clickedBlock, shopType, price, owner, item, amount), p);
            Bukkit.getPluginManager().callEvent(shopCreateEvent);
            if (shopCreateEvent.isCancelled()) return;

            plugin.getShopconfig().AddShopToShopConfig(new Shop(e.getClickedBlock(), shopType, price, owner, item, amount), itemName);
            if (!price.contains("(Buying)")) {
                p.sendMessage(lang.getMessage("ShopCreateSells").replace("<item>", itemName).replace("<price>", price));
            } else {
                p.sendMessage(lang.getMessage("ShopCreateBuys").replace("<item>", itemName).replace("<price>", price).replace(" (Buying)", ""));
            }
            p.playSound(p.getLocation(), "block.amethyst_block.place", 1.0f, 1.0f);
            plugin.getShopconfig().addShopToPlayer(p);

            sign.setLine(3, amount + "x §r" + itemName);
            sign.update();

            //PostShopCreateEvent
            PostShopCreateEvent postShopCreateEvent = new PostShopCreateEvent(new Shop(clickedBlock, shopType, price, owner, item, amount), p);
            Bukkit.getPluginManager().callEvent(postShopCreateEvent);
            return;
        }

        if (sign.getLine(0).equalsIgnoreCase(lang.getMessage("AdminSignTitle"))) {
            e.setCancelled(true);

            double price = Double.parseDouble(getPrice(plugin.getShopconfig().getPrice(e.getClickedBlock().getLocation())));

            ItemStack item = plugin.getShopconfig().NameToItem(plugin.getShopconfig().getItemName(clickedBlock.getLocation()));
            if (item == null || item.getType() == Material.AIR) {
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                p.sendMessage(lang.getMessage("invalidMaterial"));
                return;
            }

            int SignAmount = plugin.getShopconfig().getAmount(e.getClickedBlock().getLocation());
            int PlayerItemCount = countItems(p.getInventory(), item);
            int amount = SignAmount;

            if (p.isSneaking()) {
                if (isBuying(plugin.getShopconfig().getPrice(e.getClickedBlock().getLocation()))) {
                    int packs = PlayerItemCount / SignAmount; //How many "packs" a Player can Buy
                    amount = packs * SignAmount; //How many Items in general
                    if (amount >= item.getMaxStackSize()) amount = item.getMaxStackSize();
                } else amount = item.getMaxStackSize();
            }
            price = (amount/SignAmount)*price;

            item.setAmount(amount);
            if (isBuying(plugin.getShopconfig().getPrice(e.getClickedBlock().getLocation()))) {
                if (!p.hasPermission("chestshop.sell")) {
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(lang.getMessage("noPermission")));
                    return;
                }
                count = countItems(p.getInventory(), item);
                if (!p.getInventory().contains(item.getType()) || count < amount) {
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(lang.getMessage("notEngouthItems")));
                    return;
                }
                AdminPlayerSell(sign, item, p, price);

            } else {
                if (!p.hasPermission("chestshop.buy")) {
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(lang.getMessage("noPermission")));
                    return;
                }
                if (inventoryfull(p.getInventory())) {
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    p.sendTitle(lang.getMessage("inventoryFull"), "");
                    return;
                }
                if (economy.getBalance(p) < price) {
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(lang.getMessage("notEnoughMoney")));
                    return;
                }
                AdminPlayerBuy(sign, item, p, price);
            }
        }

        if (sign.getLine(0).equalsIgnoreCase(lang.getMessage("SignTitle"))) {
            e.setCancelled(true);
            if (!p.hasPermission("chestshop.buy")) {
                p.sendMessage(lang.getMessage("noPermission"));
                return;
            }
            OfflinePlayer owner = plugin.getShopconfig().getOwner(e.getClickedBlock().getLocation());
            double price = Double.parseDouble(getPrice(plugin.getShopconfig().getPrice(e.getClickedBlock().getLocation())));

            ItemStack item = plugin.getShopconfig().NameToItem(plugin.getShopconfig().getItemName(clickedBlock.getLocation()));

            if (item == null || item.getType() == Material.AIR) {
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                p.sendMessage(lang.getMessage("invalidMaterial"));
                return;
            }
            if (isBlacklisted(item.getType().toString())) {
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                p.sendMessage(lang.getMessage("ItemBlacklist"));
                return;
            }
            Block chestBlock = clickedBlock.getRelative(((org.bukkit.block.data.type.WallSign) clickedBlock.getBlockData()).getFacing().getOppositeFace());
            if (!(chestBlock.getState() instanceof Chest)) {
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                p.sendMessage(lang.getMessage("noChestBehindSign"));
                return;
            }
            Chest chest = (Chest) chestBlock.getState();
            Inventory chestInventory = chest.getInventory();

            int SignAmount = plugin.getShopconfig().getAmount(e.getClickedBlock().getLocation());
            int PlayerItemCount = countItems(p.getInventory(), item);
            int amount = SignAmount;
            if (p.isSneaking()) {
                if (PlayerItemCount < item.getMaxStackSize() && PlayerItemCount > SignAmount) {
                    amount = PlayerItemCount;
                } else {
                    amount = item.getMaxStackSize();
                }
            }
            price = (price/SignAmount)*amount;


            item.setAmount(amount);
            if (isBuying(plugin.getShopconfig().getPrice(e.getClickedBlock().getLocation()))) {
                if (!p.hasPermission("chestshop.sell")) {
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(lang.getMessage("noPermission")));
                    return;
                }
                if (economy.getBalance(owner) < price) {
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(lang.getMessage("BuyerNotEnoughMoney")));
                    return;
                }
                count = countItems(p.getInventory(), item);
                if (count < amount) {
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(lang.getMessage("notEngouthItems")));
                    return;
                }
                if (isFull(chest, item)) {
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(lang.getMessage("chestFull")));
                    return;
                }
                PlayerSell(chestInventory, sign, item, p, owner, price);
            } else {
                if (!p.hasPermission("chestshop.buy")) {
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(lang.getMessage("noPermission")));
                    return;
                }
                if (inventoryfull(p.getInventory())) {
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    p.sendTitle(lang.getMessage("inventoryFull"), "");
                    return;
                }
                count = countItems(chestInventory, item);
                if (!(count >= amount)) {
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(lang.getMessage("notEngouthItemsChest").replace("<item>", item.getType().name().toLowerCase())));
                    return;
                } // Check if <amount> item is in chest
                if (economy.getBalance(p) < price) {
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(lang.getMessage("notEnoughMoney")));
                    return;
                }
                PlayerBuy(chestInventory, sign, item, p, owner, price);
            }
        }
    }
    public void PlayerShopCreate(SignChangeEvent e, OfflinePlayer owner, Player p, String itemName, Integer amount, String price, ItemStack foundItem) {
        //ShopCreateEvent
        ShopCreateEvent shopCreateEvent = new ShopCreateEvent(new Shop(e.getBlock(), ShopType.SHOP, price, owner, foundItem, amount), p);
        Bukkit.getPluginManager().callEvent(shopCreateEvent);
        if (shopCreateEvent.isCancelled()) {
            return;
        }

        e.setLine(0, lang.getMessage("SignTitle"));
        e.setLine(1, price);
        e.setLine(2, p.getName());
        e.setLine(3, amount + "x §r" + itemName);

        //plugin.getShopconfig().addItemToShopConfig(p.getWorld().getName(), xCoord, yCoord, zCoord, itemName, p);
        plugin.getShopconfig().AddShopToShopConfig(new Shop(e.getBlock(), ShopType.SHOP, price, owner, foundItem, amount), itemName);
        if (!price.contains("(Buying)")) {p.sendMessage(lang.getMessage("ShopCreateSells").replace("<item>", itemName).replace("<price>", price));
        } else {p.sendMessage(lang.getMessage("ShopCreateBuys").replace("<item>", itemName).replace("<price>", price).replace(" (Buying)", ""));}
        p.playSound(p.getLocation(), "block.amethyst_block.place", 1.0f, 1.0f);
        plugin.getShopconfig().addShopToPlayer(p);

        //PostShopCreateEvent
        PostShopCreateEvent postShopCreateEvent = new PostShopCreateEvent(new Shop(e.getBlock(), ShopType.SHOP, price, owner, foundItem, amount), p);
        Bukkit.getPluginManager().callEvent(postShopCreateEvent);
    }
    public void AdminShopCreate(SignChangeEvent e, Player p, String itemName, String price, Integer amount, ItemStack foundItem) {
        //ShopCreateEvent
        ShopCreateEvent shopCreateEvent = new ShopCreateEvent(new Shop(e.getBlock(), ShopType.ADMIN_SHOP, price, null, foundItem, amount), p);
        Bukkit.getPluginManager().callEvent(shopCreateEvent);
        if (shopCreateEvent.isCancelled()) {
            return;
        }

        e.setLine(0, lang.getMessage("AdminSignTitle"));
        e.setLine(1, price);
        e.setLine(2, amount + "x §r");
        e.setLine(3, itemName);

        //plugin.getShopconfig().addItemToShopConfig(p.getWorld().getName(), xCoord, yCoord, zCoord, itemName, p);
        plugin.getShopconfig().AddShopToShopConfig(new Shop(e.getBlock(), ShopType.ADMIN_SHOP, price, null, foundItem, amount), itemName);
        if (!price.contains("(Buying)")) {p.sendMessage(lang.getMessage("ShopCreateSells").replace("<item>", itemName).replace("<price>", price));
        } else {p.sendMessage(lang.getMessage("ShopCreateBuys").replace("<item>", itemName).replace("<price>", price).replace(" (Buying)", ""));}
        p.playSound(p.getLocation(), "block.amethyst_block.place", 1.0f, 1.0f);

        //PostShopCreateEvent
        PostShopCreateEvent postShopCreateEvent = new PostShopCreateEvent(new Shop(e.getBlock(), ShopType.ADMIN_SHOP, price, null, foundItem, amount), p);
        Bukkit.getPluginManager().callEvent(postShopCreateEvent);
    }

    public void PlayerBuy(Inventory chestInv, Sign sign, ItemStack item, Player p, OfflinePlayer owner, Double price) { //TODO: Add PlayerBuy Event
        Economy economy = ChestShop.getEconomy();
        int amount = item.getAmount();
        int count = countItems(chestInv, item);

        chestInv.removeItem(item);
        p.getInventory().addItem(item);

        economy.withdrawPlayer(p, price);
        economy.depositPlayer(owner, price);

        if (owner.isOnline() && !owner.equals(p)) {
            owner.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(lang.getMessage("hasbought").replace("<player>", p.getDisplayName()).replace("<item>", item.getType().name().toLowerCase()).replace("<amount>", String.valueOf(amount))));
        }
        p.sendMessage();
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(lang.getMessage("youhasbought").replace("<item>",item.getType().name().toLowerCase()).replace("<amount>", String.valueOf(amount)) + "(" + (count - amount) + ")"));

    }
    public void AdminPlayerBuy(Sign sign, ItemStack item, Player p, Double price) {
        Economy economy = ChestShop.getEconomy();
        int amount = item.getAmount();

        economy.withdrawPlayer(p, price);
        p.getInventory().addItem(item);

        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(lang.getMessage("youhasbought").replace("<item>", item.getType().name().toLowerCase()).replace("<amount>", String.valueOf(amount)) + "(Adminshop)"));
    }
    public void PlayerSell(Inventory chestInv, Sign sign, ItemStack item, Player p, OfflinePlayer offlinePlayer, Double price) { //TODO: Add PlayerSell Event
        Economy economy = ChestShop.getEconomy();
        int amount = item.getAmount();
        int count = countItems(p.getInventory(), item);

        p.getInventory().removeItem(item);
        chestInv.addItem(item);
        economy.withdrawPlayer(offlinePlayer, price);
        economy.depositPlayer(p, price);
        if (offlinePlayer.isOnline() && !offlinePlayer.equals(p)) {
            offlinePlayer.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(lang.getMessage("hassold").replace("<player>", p.getDisplayName()).replace("<item>", item.getType().name().toLowerCase()).replace("<amount>", String.valueOf(amount))));
        }
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(lang.getMessage("youhassold").replace("<item>", item.getType().name().toLowerCase()).replace("<amount>", String.valueOf(amount)) + "(" + count + ")"));
    }
    public void AdminPlayerSell(Sign sign, ItemStack item, Player p, Double price) {
        Economy economy = ChestShop.getEconomy();
        int amount = item.getAmount();
        int count = countItems(p.getInventory(), item);

        p.getInventory().removeItem(item);
        economy.depositPlayer(p, price);

        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(lang.getMessage("youhassold").replace("<item>", item.getType().name().toLowerCase()).replace("<amount>", String.valueOf(amount)) + "(" + count + ")"));
    }

    private int countItems(Inventory inventory, ItemStack itemToSearch) {
        int count = 0;
        for (ItemStack stack : inventory.getContents()) {
            if (stack != null && stack.isSimilar(itemToSearch)) {
                count += stack.getAmount();
            }
        }
        return count;
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
        if (Preis.toLowerCase().contains("b")) {
            Preis = Preis.replace(" (Buying)", "");
            Preis = Preis.replaceAll("[bB]", "");
            return Preis + lang.getMessage("$") + " (Buying)";
        }
        Preis = Preis.replaceAll("[^0-9.bB ]", "x");
        if (price == null || price.equalsIgnoreCase("") || Preis.contains("x")) {
            Preis = String.valueOf(0);
        }
        Preis = Preis.replaceAll("[ ]", "");
        return Preis + lang.getMessage("$");
    }
    private boolean isBuying(String price) {
        if (price.contains(" (Buying)")) {
            return true;
        }
        return false;
    }
    private Integer getAmount(String amount) {
        StringBuilder digits = new StringBuilder();

        for (char c : amount.toCharArray()) {
            if (Character.isDigit(c)) {
                digits.append(c);
            }
        }
        if (digits.isEmpty()) {return 1;}
        return Integer.parseInt(digits.toString());
    }
    private String getPrice(String price) {
        String Preis;
        Preis = price.replace(" (Buying)", "").replaceAll("[ ]", "").replaceAll("[$]", "");
        Preis = Preis.replaceAll("[^0-9.]", "x");
        if (Preis.contains("x")) Preis = "0";
        if (Preis.equals("") || Preis.equals(null)) Preis = "0";
        return Preis;
    }
    private boolean validNumber(String input) {
        if (input == null || input.isEmpty()) return false;
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {return false;}
    }
    private boolean isFull(Chest chest, ItemStack item) {
        Inventory invCopy = Bukkit.createInventory(null, chest.getInventory().getSize());
        invCopy.setContents(chest.getInventory().getContents());
        HashMap<Integer, ItemStack> rest = invCopy.addItem(item);
        return !rest.isEmpty();
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