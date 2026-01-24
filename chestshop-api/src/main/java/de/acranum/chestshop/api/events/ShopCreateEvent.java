package de.acranum.chestshop.api.events;

import de.acranum.chestshop.api.shop.Shop;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class ShopCreateEvent extends ShopEvent implements Cancellable {
    private final Player player;
    private boolean cancelled;

    private static final HandlerList handlers = new HandlerList();

    public ShopCreateEvent(Shop shop, Player player) {
        super(shop);
        this.player = player;
        this.cancelled = false;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
