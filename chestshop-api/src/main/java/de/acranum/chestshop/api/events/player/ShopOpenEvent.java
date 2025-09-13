package de.acranum.chestshop.api.events.player;

import de.acranum.chestshop.api.events.ShopEvent;
import de.acranum.chestshop.api.shop.Shop;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class ShopOpenEvent extends ShopEvent implements Cancellable {

    private final Player player;
    private final Block CestBlock;
    private boolean cancelled;
    private String cancelMessage;

    public ShopOpenEvent(Shop shop, Player player, Block CestBlock) {
        super(shop);
        this.player = player;
        this.CestBlock = CestBlock;
        this.cancelled = false;
        this.cancelMessage = "";
    }

    public Player getPlayer() {
        return player;
    }

    public Block getChestBlock() {return CestBlock; }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
    public void setCancelled(boolean b, String message) {
        cancelled = b;
        cancelMessage = message;
    }
    public String getCancelMessage() {
        return cancelMessage;
    }
}
