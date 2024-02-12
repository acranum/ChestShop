package de.minnivini.chestshop.commands;

import de.minnivini.chestshop.ChestShop;
import de.minnivini.chestshop.GUIs.ShopSearchGUI;
import de.minnivini.chestshop.Util.lang;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class search {
    ShopSearchGUI shopSearchGUI = new ShopSearchGUI();
    public void search(String searchItem, Player p) {
        if (searchItem != null) {
            List<String> shops = new ArrayList<>();
            //if (searchItem == Material)
            shops = ChestShop.getPlugin(ChestShop.class).searchItemFromShopCongig(searchItem);
            if (shops == null) {
                p.sendMessage(lang.getMessage("noShop"));
            } else {
                shopSearchGUI.ShopSearch(p, shops, searchItem);
            }
        }
        else p.sendMessage(lang.getMessage("noArr"));


    }
}
