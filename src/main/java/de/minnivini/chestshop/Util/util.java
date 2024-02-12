package de.minnivini.chestshop.Util;

import de.minnivini.chestshop.ChestShop;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class util {
    public String Splt(String Splitter, String input) {
        String[] parts = input.split(Splitter);
        String itemName = null;
        if (parts.length == 2) {
            itemName = parts[0]; // Der Name des Gegenstands (z. B. TNT)
            int amount = Integer.parseInt(parts[1]); // Die Anzahl des Gegenstands als Integer
        }
        return itemName;
    }

}
