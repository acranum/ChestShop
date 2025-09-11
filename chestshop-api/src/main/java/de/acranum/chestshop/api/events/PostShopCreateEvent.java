package de.acranum.chestshop.api.events;

import de.acranum.chestshop.api.shop.Shop;
import de.acranum.chestshop.api.shop.ShopType;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PostShopCreateEvent extends ShopEvent {
    private final Player player;

    private static final HandlerList handlers = new HandlerList();

    public PostShopCreateEvent(Shop shop, Player player) {
        super(shop);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
