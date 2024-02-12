package de.minnivini.chestshop.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class tabCompleter implements TabCompleter {
    @Override
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
