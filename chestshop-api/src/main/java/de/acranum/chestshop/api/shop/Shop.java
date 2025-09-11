package de.acranum.chestshop.api.shop;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Shop {
    private final Block sign;
    private final ShopType type;
    private final String price;
    private final ItemStack item;
    private final OfflinePlayer owner;
    private final int amount;


    public Shop(Block sign, ShopType type, String price, OfflinePlayer owner, ItemStack item, int amount) {
        this.sign = sign;
        this.type = type;
        this.price = price;
        this.item = item;
        this.owner = owner;
        this.amount = amount;
    }

    public Block getSign() {
        return sign;
    }
    public Location getLocation() {
        return sign.getLocation();
    }
    public ShopType getShopType() {
        return type;
    }
    public String getPrice() {
        return price;
    }
    public ItemStack getItem() {
        return item;
    }
    public OfflinePlayer getOwner() {
        return owner;
    }
    public int getAmount() {
        return amount;
    }

    public Block getAttached() {
        Block attachedBlock = sign.getRelative(((org.bukkit.block.data.type.WallSign) sign.getBlockData()).getFacing().getOppositeFace());
        return attachedBlock;
    }
    public Chest getChest() {
        Block attachedBlock = sign.getRelative(((org.bukkit.block.data.type.WallSign) sign.getBlockData()).getFacing().getOppositeFace());
        BlockState state = attachedBlock.getState();
        if (state instanceof Chest chest) {
            return chest;
        }
        return null;
    }
    public ItemStack[] getContents() {
        Block attachedBlock = sign.getRelative(((org.bukkit.block.data.type.WallSign) sign.getBlockData()).getFacing().getOppositeFace());
        BlockState state = attachedBlock.getState();
        if (state instanceof Chest chest) {
            return chest.getInventory().getContents();
        }
        return null;
    }

    public void setItem(ItemStack item) {

    }


    @Override
    public String toString() {
        return "Shop[Sign="+sign+", ShopType="+type+", price="+price +", item="+item+", owner="+owner+", amount="+amount+"]";
    }
}
