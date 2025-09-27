package de.acranum.chestshop.listeners;

import de.acranum.chestshop.ChestShop;
import de.acranum.chestshop.Util.lang;
import de.acranum.chestshop.api.events.ShopDestroyEvent;
import de.acranum.chestshop.api.events.player.ShopOpenEvent;
import de.acranum.chestshop.api.shop.DestroyReason;
import de.acranum.chestshop.api.shop.Shop;
import de.acranum.chestshop.api.shop.ShopType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.HangingSign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class BlockBreak implements Listener {

    ChestShop plugin = ChestShop.getPlugin(ChestShop.class);

    @EventHandler
    public void SignBreak(BlockBreakEvent e) {
        if (plugin.getShopconfig().getShop(e.getBlock().getLocation()) == null) return;
        Player p = e.getPlayer();
        if (e.getBlock().getState() instanceof Sign sign) {

            if (plugin.getShopconfig().getShop(e.getBlock().getLocation()) == null) return;
            if (sign.getLine(0).equalsIgnoreCase(lang.getMessage("AdminSignTitle"))) {
                if (plugin.getShopconfig().getItemName(e.getBlock().getLocation()) != null) {
                    if (!p.isOp() && !p.hasPermission("chestshop.admincreate")) { e.setCancelled(true); return; }

                    ShopDestroyEvent shopDestroyEvent = new ShopDestroyEvent(new Shop(e.getBlock(), plugin.getShopconfig().getShopType(e.getBlock().getLocation()), plugin.getShopconfig().getPrice(e.getBlock().getLocation()),plugin.getShopconfig().getOwner(e.getBlock().getLocation()), plugin.getShopconfig().NameToItem(plugin.getShopconfig().getItemName(e.getBlock().getLocation())), plugin.getShopconfig().getAmount(e.getBlock().getLocation())), p, DestroyReason.PLAYER);
                    Bukkit.getPluginManager().callEvent(shopDestroyEvent);
                    if (shopDestroyEvent.isCancelled()) {
                        e.setCancelled(true);
                        return; }

                    plugin.getShopconfig().RemoveShopFromShopConfig(e.getBlock().getLocation());
                    if (plugin.getShopconfig().getShop(e.getBlock().getLocation()) == null) {
                        p.sendMessage(lang.getMessage("shopRemove"));
                    } else {
                        e.setCancelled(true);
                        p.sendMessage(lang.getMessage("shopRemoveErr"));
                    }
                }
            } else if (sign.getLine(0).equalsIgnoreCase(lang.getMessage("SignTitle"))){
                if (!p.hasPermission("chestshop.interact") && !plugin.getShopconfig().getOwner(e.getBlock().getLocation()).getUniqueId().equals(p.getUniqueId())) { e.setCancelled(true); return; }

                ShopDestroyEvent shopDestroyEvent = new ShopDestroyEvent(new Shop(e.getBlock(), plugin.getShopconfig().getShopType(e.getBlock().getLocation()), plugin.getShopconfig().getPrice(e.getBlock().getLocation()),plugin.getShopconfig().getOwner(e.getBlock().getLocation()), plugin.getShopconfig().NameToItem(plugin.getShopconfig().getItemName(e.getBlock().getLocation())), plugin.getShopconfig().getAmount(e.getBlock().getLocation())), p, DestroyReason.PLAYER);
                Bukkit.getPluginManager().callEvent(shopDestroyEvent);
                if (shopDestroyEvent.isCancelled()) { e.setCancelled(true); return; }

                plugin.getShopconfig().RemoveShopFromShopConfig(e.getBlock().getLocation());
                if (plugin.getShopconfig().getShop(e.getBlock().getLocation()) == null) {
                    p.sendMessage(lang.getMessage("shopRemove"));
                    plugin.getShopconfig().removeShopFromPlayer(p);
                } else {
                    e.setCancelled(true);
                    sign.update();
                    p.sendMessage(lang.getMessage("shopRemoveErr"));
                }
            }
        }
    }
    @EventHandler
    public void ChestBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        for (BlockFace face : BlockFace.values()) {
            Block relative = e.getBlock().getRelative(face);

            if (!(relative.getState() instanceof Sign)) continue;

            Block SignBlock;
            if (relative.getBlockData() instanceof WallSign wallSign) {
                if (!(wallSign.getFacing() == face)) continue;
                SignBlock = relative;
            } else if (relative.getBlockData() instanceof HangingSign) {
                if (!face.equals(BlockFace.DOWN)) continue;
                SignBlock = relative;
            } else if (relative.getState() instanceof Sign && !(relative.getBlockData() instanceof WallSign)) {
                if (!face.equals(BlockFace.UP)) continue;
                SignBlock = relative;
            } else continue;

            Sign sign = (Sign) SignBlock.getState();
            if (plugin.getShopconfig().getShop(SignBlock.getLocation()) == null) return;
            if (plugin.getShopconfig().getShopType(SignBlock.getLocation()).equals(ShopType.ADMIN_SHOP)) {
                if (!p.isOp() && !p.hasPermission("chestshop.admincreate")) { e.setCancelled(true); return; }

                ShopDestroyEvent shopDestroyEvent = new ShopDestroyEvent(new Shop(SignBlock, plugin.getShopconfig().getShopType(SignBlock.getLocation()), plugin.getShopconfig().getPrice(SignBlock.getLocation()),plugin.getShopconfig().getOwner(SignBlock.getLocation()), plugin.getShopconfig().NameToItem(plugin.getShopconfig().getItemName(SignBlock.getLocation())), plugin.getShopconfig().getAmount(SignBlock.getLocation())), p, DestroyReason.PLAYER);
                Bukkit.getPluginManager().callEvent(shopDestroyEvent);
                if (shopDestroyEvent.isCancelled()) {
                    e.setCancelled(true);
                    return; }
                plugin.getShopconfig().RemoveShopFromShopConfig(SignBlock.getLocation());
                plugin.getShopconfig().saveShopConfig();
                if (plugin.getShopconfig().getShop(SignBlock.getLocation()) == null) {
                    p.sendMessage(lang.getMessage("shopRemove"));
                } else {
                    e.setCancelled(true);
                    p.sendMessage(lang.getMessage("shopRemoveErr"));
                }
            } else if (plugin.getShopconfig().getShopType(SignBlock.getLocation()).equals(ShopType.SHOP)){
                if (!p.hasPermission("chestshop.interact") && !plugin.getShopconfig().getOwner(SignBlock.getLocation()).getUniqueId().equals(p.getUniqueId())) { e.setCancelled(true); return; }

                ShopDestroyEvent shopDestroyEvent = new ShopDestroyEvent(new Shop(SignBlock, plugin.getShopconfig().getShopType(SignBlock.getLocation()), plugin.getShopconfig().getPrice(SignBlock.getLocation()),plugin.getShopconfig().getOwner(SignBlock.getLocation()), plugin.getShopconfig().NameToItem(plugin.getShopconfig().getItemName(SignBlock.getLocation())), 1), p, DestroyReason.PLAYER);
                Bukkit.getPluginManager().callEvent(shopDestroyEvent);
                if (shopDestroyEvent.isCancelled()) { e.setCancelled(true); return; }

                plugin.getShopconfig().RemoveShopFromShopConfig(SignBlock.getLocation());
                plugin.getShopconfig().saveShopConfig();
                if (plugin.getShopconfig().getShop(SignBlock.getLocation()) == null) {
                    p.sendMessage(lang.getMessage("shopRemove"));
                    plugin.getShopconfig().removeShopFromPlayer(p);
                } else {
                    e.setCancelled(true);
                    sign.update();
                    p.sendMessage(lang.getMessage("shopRemoveErr")); }
            }
        }
    }
    @EventHandler
    public void ChestInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getClickedBlock() == null) return;
        if (e.getClickedBlock().getType() == Material.CHEST || e.getClickedBlock().getType() == Material.TRAPPED_CHEST) {
            int[][] directions = {{0, 0, 1}, {0, 0, -1}, {1, 0, 0}, {-1, 0, 0}};
            Location chestLocation = e.getClickedBlock().getLocation();
            for (int[] direction : directions) {
                int xOffset = direction[0];
                int yOffset = direction[1];
                int zOffset = direction[2];

                Location currentLocation = new Location(chestLocation.getWorld(), chestLocation.getBlockX() + xOffset, chestLocation.getBlockY() + yOffset, chestLocation.getBlockZ() + zOffset);
                Block currentBlock = currentLocation.getBlock();

                if (currentBlock.getState() instanceof Sign sign) {
                    if (plugin.getShopconfig().getItemName(currentLocation) == null) return;

                    if (sign.getLine(0).equalsIgnoreCase(lang.getMessage("SignTitle"))) {
                        if (!p.hasPermission("chestshop.interact")) {
                            if (!plugin.getShopconfig().getOwner(currentLocation).getUniqueId().equals(p.getUniqueId())) {
                                p.sendMessage(lang.getMessage("noPermission")); e.setCancelled(true); return;
                            }
                        }
                        ShopOpenEvent ShopOpenEvent = new ShopOpenEvent(new Shop(currentBlock, plugin.getShopconfig().getShopType(currentLocation), plugin.getShopconfig().getPrice(currentLocation), plugin.getShopconfig().getOwner(currentLocation), plugin.getShopconfig().NameToItem(plugin.getShopconfig().getItemName(currentLocation)), plugin.getShopconfig().getAmount(currentLocation)),p, e.getClickedBlock());
                        Bukkit.getPluginManager().callEvent(ShopOpenEvent);
                        if (ShopOpenEvent.isCancelled()) { p.sendMessage(ShopOpenEvent.getCancelMessage()); e.setCancelled(true); return; }

                    } else if (sign.getLine(0).equalsIgnoreCase(lang.getMessage("AdminSignTitle"))) {
                        if (!p.hasPermission("chestshop.interact") || !p.hasPermission("chestshop.admincreate")) {
                            p.sendMessage(lang.getMessage("noPermission")); e.setCancelled(true); return;
                        }

                        ShopOpenEvent ShopOpenEvent = new ShopOpenEvent(new Shop(currentBlock, plugin.getShopconfig().getShopType(currentLocation), plugin.getShopconfig().getPrice(currentLocation), plugin.getShopconfig().getOwner(currentLocation), plugin.getShopconfig().NameToItem(plugin.getShopconfig().getItemName(currentLocation)), plugin.getShopconfig().getAmount(currentLocation)),p, e.getClickedBlock());
                        Bukkit.getPluginManager().callEvent(ShopOpenEvent);
                        if (ShopOpenEvent.isCancelled()) {p.sendMessage(ShopOpenEvent.getCancelMessage()); e.setCancelled(true); return; }
                    }
                }
            }
        }
    }
    @EventHandler
    public void HopperMovement(InventoryMoveItemEvent e) {
        InventoryType destination = e.getDestination().getType();
        if (destination == InventoryType.HOPPER) {
            Block source = Objects.requireNonNull(e.getSource().getLocation()).getBlock();
            if (e.getSource().getType() == InventoryType.CHEST) {
                int[][] directions = {{0, 0, 1}, {0, 0, -1}, {1, 0, 0}, {-1, 0, 0}};
                Location chestLocation = source.getLocation();
                for (int[] direction : directions) {
                    int xOffset = direction[0];
                    int yOffset = direction[1];
                    int zOffset = direction[2];

                    Location currentLocation = new Location(chestLocation.getWorld(), chestLocation.getBlockX() + xOffset, chestLocation.getBlockY() + yOffset, chestLocation.getBlockZ() + zOffset);
                    Block currentBlock = currentLocation.getBlock();

                    if (currentBlock.getState() instanceof Sign sign) {
                        if (sign.getLine(0).equalsIgnoreCase(lang.getMessage("SignTitle")) || sign.getLine(0).equalsIgnoreCase(lang.getMessage("AdminSignTitle"))) {
                            if (plugin.getShopconfig().getItemName(currentLocation) != null) {
                                if (plugin.getDefaultConfig().defaultConfig.getBoolean("Shop_hopper_protection")) {
                                    e.setCancelled(true);
                                }
                            }
                        }
                    }
                }

            }
        }

    }
    private String getPrice(String price) {
        String Preis;
        Preis = price.replace(" (Buying)", "").replaceAll("[ ]", "").replaceAll("[$]", "");
        Preis = Preis.replaceAll("[^0-9.]", "x");
        if (Preis.contains("x")) Preis = "0";
        if (Preis.isEmpty()) Preis = "0";
        return Preis;
    }
}