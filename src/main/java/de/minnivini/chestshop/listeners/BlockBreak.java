package de.minnivini.chestshop.listeners;

import de.minnivini.chestshop.ChestShop;
import de.minnivini.chestshop.Util.lang;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.Objects;

public class BlockBreak implements Listener {

    @EventHandler
    public void SignBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (e.getBlock().getState() instanceof Sign) {
            int xCoord = e.getBlock().getLocation().getBlockX();
            int yCoord = e.getBlock().getLocation().getBlockY();
            int zCoord = e.getBlock().getLocation().getBlockZ();
            if (ChestShop.getPlugin(ChestShop.class).getItemFromShopConfig(player.getWorld().getName(), xCoord, yCoord, zCoord) != null) {
                if (player.hasPermission("chestshop.break")) {
                    ChestShop.getPlugin(ChestShop.class).removeItemFromShopConfig(player.getWorld().getName() ,xCoord, yCoord, zCoord);
                    if (ChestShop.getPlugin(ChestShop.class).getItemFromShopConfig(player.getWorld().getName(), xCoord, yCoord, zCoord) == null) {
                        player.sendMessage(lang.getMessage("shopRemove"));
                    } else player.sendMessage(lang.getMessage("shopRemoveErr"));
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }
    @EventHandler
    public void ChestBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
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

                        int xCoord = currentLocation.getBlockX();
                        int yCoord = currentLocation.getBlockY();
                        int zCoord = currentLocation.getBlockZ();
                        String world = currentLocation.getWorld().getName();
                        if (ChestShop.getPlugin(ChestShop.class).getItemFromShopConfig(world, xCoord, yCoord, zCoord) != null) {
                            if (player.hasPermission("chestshop.break")) {
                                ChestShop.getPlugin(ChestShop.class).removeItemFromShopConfig(player.getWorld().getName() ,xCoord, yCoord, zCoord);
                                if (ChestShop.getPlugin(ChestShop.class).getItemFromShopConfig(player.getWorld().getName(), xCoord, yCoord, zCoord) == null) {
                                    player.sendMessage(lang.getMessage("shopRemove"));
                                } else {
                                    e.setCancelled(true);
                                    player.sendMessage(lang.getMessage("shopRemoveErr"));
                                }
                            } else e.setCancelled(true);
                        }
                    }
                }
            }
        //}
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
                            if (ChestShop.getPlugin(ChestShop.class).getItemFromShopConfig(world, xCoord, yCoord, zCoord) != null) {
                                if (ChestShop.getPlugin(ChestShop.class).defaultConfig.getBoolean("SHOP_HOPPER_PROTECTION")) {
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