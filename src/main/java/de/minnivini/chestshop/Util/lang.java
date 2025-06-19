package de.minnivini.chestshop.Util;

import de.minnivini.chestshop.ChestShop;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class lang {
    public static String getMessage(String message){
        File languageFolder = new File(ChestShop.getPlugin(ChestShop.class).getDataFolder() + "/language");
        String language = ChestShop.getPlugin(ChestShop.class).getLanguage();
        File langFile = new File(languageFolder, language + ".yml");
        if (!langFile.exists()) {
            return "Check your language files!";
        }
        YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(langFile);
        return langConfig.getString(message);
    }
    public static void createLanguageFolder() {
        File langFolder = new File(ChestShop.getPlugin(ChestShop.class).getDataFolder() + "/language");
        if (!langFolder.exists()) {
            langFolder.mkdir();
        }
        File enFile = new File(langFolder, "en.yml");
        File deFile = new File(langFolder, "de.yml");
        File frFile = new File(langFolder, "fr.yml");
        try {
            if (!enFile.exists()) {
                InputStream in = ChestShop.getPlugin(ChestShop.class).getResource("en.yml");
                Files.copy(in, enFile.toPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (!deFile.exists()) {
                InputStream in = ChestShop.getPlugin(ChestShop.class).getResource("de.yml");
                Files.copy(in, deFile.toPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (!frFile.exists()) {
                InputStream in = ChestShop.getPlugin(ChestShop.class).getResource("fr.yml");
                Files.copy(in, deFile.toPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void checkLanguageUpdates() {
        File langFolder = new File(ChestShop.getPlugin(ChestShop.class).getDataFolder() + "/language");
        File enFile = new File(langFolder, "en.yml");
        File deFile = new File(langFolder, "de.yml");
        File frFile = new File(langFolder, "fr.yml");

        InputStream in = ChestShop.getPlugin(ChestShop.class).getResource("en.yml");
        InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
        YamlConfiguration internalConfig = YamlConfiguration.loadConfiguration(reader);

        if (enFile.exists() ) {
            YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(enFile);

            if (langConfig.getString("version") == null || langConfig.getDouble("version") < internalConfig.getDouble("version")) {
                enFile.delete();
                deFile.delete();
                frFile.delete();
                createLanguageFolder();

            }
        }

    }

}


