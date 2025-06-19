package de.minnivini.chestshop.Util;

import de.minnivini.chestshop.ChestShop;

import java.io.File;

public class addon {

    public void initilize() {

        File addonFolder = new File(ChestShop.getPlugin(ChestShop.class).getDataFolder() + "/add-ons");

    }

}
