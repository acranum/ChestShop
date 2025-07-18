package de.minnivini.chestshop.listeners;

import de.minnivini.chestshop.ChestShop;
import de.minnivini.chestshop.Util.lang;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
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
        Player p = e.getPlayer();
        if (e.getBlock().getState() instanceof Sign) {
            Sign sign = (Sign) e.getBlock().getState();
            int xCoord = e.getBlock().getLocation().getBlockX();
            int yCoord = e.getBlock().getLocation().getBlockY();
            int zCoord = e.getBlock().getLocation().getBlockZ();
            if (plugin.getShopconfig().getItemFromShopConfig(p.getWorld().getName(), xCoord, yCoord, zCoord) != null) {
                if (sign.getLine(0).equalsIgnoreCase("§a[Adminshop]")) {
                    if (plugin.getShopconfig().getItemFromShopConfig(p.getWorld().getName(), xCoord, yCoord, zCoord) != null) {
                        if (p.isOp() || p.hasPermission("chestshop.admincreate")) {
                            plugin.getShopconfig().removeItemFromShopConfig(p.getWorld().getName() ,xCoord, yCoord, zCoord);
                            if (plugin.getShopconfig().getItemFromShopConfig(p.getWorld().getName(), xCoord, yCoord, zCoord) == "") {
                                p.sendMessage(lang.getMessage("shopRemove"));
                            } else {
                                e.setCancelled(true);
                                p.sendMessage(lang.getMessage("shopRemoveErr"));
                            }
                        } else e.setCancelled(true);
                    }
                } else if (sign.getLine(0).equalsIgnoreCase("§a[Shop]")){
                    if (plugin.getShopconfig().getItemFromShopConfig(p.getWorld().getName(), xCoord, yCoord, zCoord) != null) {
                        if (p.hasPermission("chestshop.break") || sign.getLine(2).equals(p.getName())) {
                            plugin.getShopconfig().removeItemFromShopConfig(p.getWorld().getName() ,xCoord, yCoord, zCoord);
                            if (plugin.getShopconfig().getItemFromShopConfig(p.getWorld().getName(), xCoord, yCoord, zCoord) == "") {
                                p.sendMessage(lang.getMessage("shopRemove"));
                                plugin.getShopconfig().removeShopFromPlayer(p);
                            } else {
                                e.setCancelled(true);
                                p.sendMessage(lang.getMessage("shopRemoveErr"));
                            }
                        } else e.setCancelled(true);
                    }
                }
            }
        }
    }
    @EventHandler
    public void ChestBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        //if (e.getBlock().getType().toString().contains("CHEST")) {
            int[][] directions = {{0, 0, 1}, {0 , 0, -1}, {1, 0, 0}, {-1, 0, 0}};
            Location chestLocation = e.getBlock().getLocation();
            for (int[] direction: directions) {
                int xOffset = direction[0];
                int yOffset = direction[1];
                int zOffset = direction[2];

                Location currentLocation = new Location(chestLocation.getWorld(), chestLocation.getBlockX() + xOffset, chestLocation.getBlockY() + yOffset, chestLocation.getBlockZ() + zOffset);
                Block currentBlock = currentLocation.getBlock();

                if (currentBlock.getState() instanceof Sign) {
                    Sign sign = (Sign) currentBlock.getState();
                    if (sign.getLine(0).equalsIgnoreCase("§a[Shop]") || sign.getLine(0).equalsIgnoreCase("§a[Adminshop]")) {
                        //checks if destroyed block is the attachment of sign
                        if (sign.getLine(0).equalsIgnoreCase("§a[Shop]")) {
                            if (!(e.getBlock().getState() instanceof Chest)) return;
                            Block attachedBlock = currentBlock.getRelative(((org.bukkit.block.data.type.WallSign) currentBlock.getBlockData()).getFacing().getOppositeFace());
                            if (!(attachedBlock.equals(e.getBlock()))) return;
                        } else if (sign.getLine(0).equalsIgnoreCase("§a[Adminshop]")) {
                            if (currentBlock.getType().toString().endsWith("_WALL_SIGN")) {
                                Block attachedBlock = currentBlock.getRelative(((org.bukkit.block.data.type.WallSign) currentBlock.getBlockData()).getFacing().getOppositeFace());
                                if (!(attachedBlock.equals(e.getBlock()))) return;
                            } else if (currentBlock.getType().toString().endsWith("_HANGING_SIGN")) {
                                return; //TODO: Hanging sign attached block
                            }
                        }
                        int xCoord = currentLocation.getBlockX();
                        int yCoord = currentLocation.getBlockY();
                        int zCoord = currentLocation.getBlockZ();
                        String world = currentLocation.getWorld().getName();

                        if (sign.getLine(0).equalsIgnoreCase("§a[Adminshop]")) {
                            if (plugin.getShopconfig().getItemFromShopConfig(world, xCoord, yCoord, zCoord) != null) {
                                if (p.isOp() || p.hasPermission("chestshop.admincreate")) {
                                    plugin.getShopconfig().removeItemFromShopConfig(world ,xCoord, yCoord, zCoord);
                                    if (plugin.getShopconfig().getItemFromShopConfig(world, xCoord, yCoord, zCoord) == "") {
                                        p.sendMessage(lang.getMessage("shopRemove"));
                                    } else {
                                        e.setCancelled(true);
                                        p.sendMessage(lang.getMessage("shopRemoveErr"));
                                    }
                                } else e.setCancelled(true);
                            }
                        } else if (sign.getLine(0).equalsIgnoreCase("§a[Shop]")) {
                            if (plugin.getShopconfig().getItemFromShopConfig(world, xCoord, yCoord, zCoord) != null) {
                                if (p.hasPermission("chestshop.break") || sign.getLine(2).equals(p.getName())) {
                                    plugin.getShopconfig().removeItemFromShopConfig(world ,xCoord, yCoord, zCoord);
                                    if (plugin.getShopconfig().getItemFromShopConfig(world, xCoord, yCoord, zCoord) == "") {
                                        p.sendMessage(lang.getMessage("shopRemove"));
                                        plugin.getShopconfig().removeShopFromPlayer(p);
                                    } else {
                                        e.setCancelled(true);
                                        p.sendMessage(lang.getMessage("shopRemoveErr"));
                                    }
                                } else e.setCancelled(true);
                            }
                        }
                    }
                }
            }
        //}
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

                if (currentBlock.getState() instanceof Sign) {
                    Sign sign = (Sign) currentBlock.getState();
                    if (sign.getLine(0).equalsIgnoreCase("§a[Shop]")) {
                        int xCoord = currentLocation.getBlockX();
                        int yCoord = currentLocation.getBlockY();
                        int zCoord = currentLocation.getBlockZ();
                        String world = currentLocation.getWorld().getName();
                        if (plugin.getShopconfig().getItemFromShopConfig(world, xCoord, yCoord, zCoord) != null) {
                            if (p.hasPermission("chestshop.interact")) return;
                            if (!sign.getLine(2).equals(p.getName())) e.setCancelled(true);
                        }
                    } else if (sign.getLine(0).equalsIgnoreCase("§a[Adminshop]")) {
                        if (!p.hasPermission("chestshop.interact") || !p.hasPermission("chestshop.admincreate")) {
                            e.setCancelled(true);
                        }
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

                    if (currentBlock.getState() instanceof Sign) {
                        Sign sign = (Sign) currentBlock.getState();
                        if (sign.getLine(0).equalsIgnoreCase("§a[Shop]") || sign.getLine(0).equalsIgnoreCase("§a[Adminshop]")) {
                            int xCoord = currentLocation.getBlockX();
                            int yCoord = currentLocation.getBlockY();
                            int zCoord = currentLocation.getBlockZ();
                            String world = currentLocation.getWorld().getName();
                            if (plugin.getShopconfig().getItemFromShopConfig(world, xCoord, yCoord, zCoord) != null) {
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

}