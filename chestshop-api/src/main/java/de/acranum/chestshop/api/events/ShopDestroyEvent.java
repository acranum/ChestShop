package de.acranum.chestshop.api.events;

import de.acranum.chestshop.api.shop.DestroyReason;
import de.acranum.chestshop.api.shop.Shop;
import de.acranum.chestshop.api.shop.ShopType;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class ShopDestroyEvent extends ShopEvent implements Cancellable {
    private final Player player;
    private final DestroyReason reason;
    private boolean cancelled;

    private static final HandlerList handlers = new HandlerList();

    public ShopDestroyEvent(Shop shop, Player player, DestroyReason reason) {
        super(shop);
        this.player = player;
        this.reason = reason;
        this.cancelled = false;
    }

    public Player getPlayer() {
        return player;
    }
    public DestroyReason getReason() {
        return reason;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
