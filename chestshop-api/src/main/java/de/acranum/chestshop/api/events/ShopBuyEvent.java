package de.acranum.chestshop.api.events;

import de.acranum.chestshop.api.shop.Shop;
import de.acranum.chestshop.api.shop.ShopType;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class ShopBuyEvent extends ShopEvent implements Cancellable {
    private final Player player;
    private boolean cancelled;

    private static final HandlerList handlers = new HandlerList();

    public ShopBuyEvent(Shop shop, Player player) {
        super(shop);
        this.player = player;
        this.cancelled = false;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
