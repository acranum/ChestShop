package de.minnivini.chestshop.commands;

import de.minnivini.chestshop.GUIs.InfoGUI;
import de.minnivini.chestshop.Util.lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class csCommand implements CommandExecutor{

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
                } else if (args[0].equalsIgnoreCase("search")) {
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

}
