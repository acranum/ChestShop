package de.minnivini.chestshop.listeners;

import de.minnivini.chestshop.Util.lang;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InvListener implements Listener {
    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getCurrentItem() != null) {
            if (e.getView().getTitle().equals("§bShop Search")) {
                e.setCancelled(true);
                if (e.getCurrentItem().getItemMeta().hasLocalizedName()) {
                    switch (e.getCurrentItem().getItemMeta().getLocalizedName()) {
                        case "shopTP":
                            String koords = e.getCurrentItem().getItemMeta().getLore().toString().replace(", §7world", "").replace("§7", "");
                            koords = koords.replaceAll("[\\[\\]]", "");
                            String world = String.valueOf(e.getCurrentItem().getItemMeta().getLore().get(1));
                            String formatetWorld = world.substring(2);

                            String[] parts = koords.split(" ");
                            Double x = null;
                            Double y = null;
                            Double z = null;
                            x = Double.valueOf(parts[0]);
                            y = Double.valueOf(parts[1]);
                            z = Double.valueOf(parts[2]);
                            if (z != null) {
                                if (p.hasPermission("chestshop.tp")){
                                    p.closeInventory();
                                    //int Cooldown = ChestShop.getPlugin(ChestShop.class).getCooldown();
                                    p.teleport(new Location(Bukkit.getWorld(formatetWorld), x, y, z));
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
}