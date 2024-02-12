package de.minnivini.chestshop.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class tabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String string, String[] strings) {
        if (command.getName().equalsIgnoreCase("chestshop")) {
            List<String> list = new ArrayList<>();
            if (string.length() == 1) {
                list.add("info");
                list.add("search");
                Collections.sort(list);
                return list;
            } else if (strings.length == 2) {
                List<String> completer = new ArrayList<>();
                String arg = strings[0].toLowerCase();
                for (String s : list) {
                    if (s.toLowerCase().startsWith(arg)) {
                        completer.remove(s);
                    }
                }
                Collections.sort(completer);
                return completer;
            }

        }
        return null;
    }
}
