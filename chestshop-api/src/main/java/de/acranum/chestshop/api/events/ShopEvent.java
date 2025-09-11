package de.acranum.chestshop.api.events;

import de.acranum.chestshop.api.shop.Shop;
import de.acranum.chestshop.api.shop.ShopType;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class ShopEvent extends Event {

    private final Shop shop;

    private static final HandlerList handlers = new HandlerList();


    public ShopEvent(Shop shop) {
        this.shop = shop;
    }

    public Shop getShop() {
        return shop;
    }


    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
