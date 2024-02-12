package de.minnivini.chestshop.commands;

import de.minnivini.chestshop.GUIs.InfoGUI;
import de.minnivini.chestshop.Util.lang;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class csCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        InfoGUI InfoGUI = new InfoGUI();
        search search = new search();
        Player player = (Player) commandSender;
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(Objects.requireNonNull(lang.getMessage("PlayerOnly")));
            return true;
        } else {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("info")) {
                    InfoGUI.InfoGUI(commandSender);
                } else if (args[0].equalsIgnoreCase("search") && player.hasPermission("chestshop.search")) {
                    if (args.length > 1) {
                        String material = args[1].toUpperCase();
                        search.search(material, player);
                    } else {
                        player.sendMessage(lang.getMessage("noArr"));
                    }
                } else player.sendMessage(lang.getMessage("noArr"));
            } else player.sendMessage(lang.getMessage("noArr"));
        }
        return false;
    }
    public List<String> onTabComplete(CommandSender commandSender, Command command, String string, String[] strings) {
        List<String> list = new ArrayList<>();
        if (strings.length == 1) {
            list.add("info");
            list.add("search");
        } else if (strings.length == 2) {
            for (Material value : Material.values()) {
                list.add(value.toString().toLowerCase());
            }
        }
        return list;
    }

}
