package de.minnivini.chestshop.listeners;

import de.minnivini.chestshop.ChestShop;
import de.minnivini.chestshop.Util.lang;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

public class InvListener implements Listener {
    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getCurrentItem() != null) {
            if (e.getView().getTitle().equals("§bShop Search")) {
                e.setCancelled(true);
                if (e.getCurrentItem().getItemMeta().getPersistentDataContainer().has(ChestShop.getPlugin(ChestShop.class).getNamespacedKey(), PersistentDataType.STRING)) {
                    switch (e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(ChestShop.getPlugin(ChestShop.class).getNamespacedKey(), PersistentDataType.STRING)) {
                        case "shopTP":
                            String koords = e.getCurrentItem().getItemMeta().getLore().toString().replace(", §7world", "").replace("§7", "");
                            koords = koords.replaceAll("[\\[\\]]", "");
                            String worldStr = String.valueOf(e.getCurrentItem().getItemMeta().getLore().get(1));
                            String formatetWorld = worldStr.substring(2);

                            String[] parts = koords.split(" ");
                            Double x = null;
                            Double y = null;
                            Double z = null;
                            x = Double.valueOf(parts[0]);
                            y = Double.valueOf(parts[1]);
                            z = Double.valueOf(parts[2]);
                            World world = Bukkit.getWorld(formatetWorld);
                            Block sign = world.getBlockAt(x.intValue(),y.intValue(),z.intValue());

                            if (z != null) {
                                if (p.hasPermission("chestshop.tp")){
                                    p.closeInventory();
                                    //int Cooldown = ChestShop.getPlugin(ChestShop.class).getCooldown();
                                    p.teleport(new Location(world, x + 0.5, y + 0.5, z + 0.5).setDirection(getSignFacing(sign)));
                                } else {
                                    p.sendMessage(lang.getMessage("noPermission"));
                                }
                            } else {
                                p.sendMessage("Shittt!");
                            }
                            break;
                    }
                }
            } else if (e.getView().getTitle().equals("§3Shop Info")) {
                e.setCancelled(true);
            }
        }
    }
    private Vector getSignFacing(Block sign) {
        if (sign.getState() instanceof Sign) {
            BlockFace attachedFace = ((org.bukkit.block.data.type.WallSign) sign.getState().getBlock().getBlockData()).getFacing().getOppositeFace();
            Vector direction;

            switch (attachedFace) {
                case NORTH -> direction = new Vector(0, 0, -1);
                case SOUTH -> direction = new Vector(0, 0, 1);
                case EAST -> direction = new Vector(1, 0, 0);
                case WEST -> direction = new Vector(-1, 0, 0);
                default -> direction = new Vector(0, 0, -1);
            }
            return direction;
        }
        return new Vector(0,0,-1);
    }
}