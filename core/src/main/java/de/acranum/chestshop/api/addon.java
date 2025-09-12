package de.acranum.chestshop.api;

import de.acranum.chestshop.ChestShop;
import de.acranum.chestshop.Util.lang;
import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class addon {
    public static List<Plugin> loadedAddons = new ArrayList<>();

    public static void setupAddons() {

        File addonFolder = new File(ChestShop.getPlugin(ChestShop.class).getDataFolder() + "/add-ons");
        if (!addonFolder.exists()) {
            addonFolder.mkdir();
        }
        File ReadmeFile = new File(addonFolder, "ADDONS.md");
        try {
            InputStream in = ChestShop.getPlugin(ChestShop.class).getResource("ADDONS.md");
            Files.copy(in, ReadmeFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void loadAddons() {
        setupAddons();
        File addonFolder = new File(ChestShop.getPlugin(ChestShop.class).getDataFolder() + "/add-ons");
        File[] jars = addonFolder.listFiles(((dir, name) -> name.endsWith(".jar")));
        if (jars != null) {
            for (File jar : jars) {
                try {
                    Plugin loaded = Bukkit.getServer().getPluginManager().loadPlugin(jar);
                    Bukkit.getServer().getPluginManager().enablePlugin(loaded);
                    Bukkit.getLogger().info(lang.getMessage("loadAddon").replace("<addon>", jar.getName()));
                    loadedAddons.add(loaded);
                } catch (InvalidPluginException | InvalidDescriptionException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }
    public static void reloadAddons() {
        if (loadedAddons == null) return;
        for (Plugin addon : loadedAddons) {
            Bukkit.getPluginManager().disablePlugin(addon);
        }
        loadedAddons.clear();
        loadAddons();
    }

}
