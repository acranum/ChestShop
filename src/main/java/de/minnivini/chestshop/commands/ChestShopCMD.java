package de.minnivini.chestshop.commands;

import de.minnivini.chestshop.ChestShop;
import de.minnivini.chestshop.GUIs.InfoGUI;
import de.minnivini.chestshop.GUIs.ShopSearchGUI;
import de.minnivini.chestshop.Util.lang;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.checkerframework.checker.units.qual.C;

import java.util.*;

public class ChestShopCMD implements CommandExecutor, TabExecutor {

    ChestShop plugin = ChestShop.getPlugin(ChestShop.class);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        InfoGUI InfoGUI = new InfoGUI();
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
                        search(material, player);
                    } else {
                        player.sendMessage(lang.getMessage("noArr"));
                    }
                } else player.sendMessage(lang.getMessage("noArr"));
            } else player.sendMessage(lang.getMessage("noArr"));
        }
        return false;
    }

    private void search(String mat, Player p) {
        if (mat != null) {
            List<String> shops = plugin.getShopconfig().searchItemFromShopConfig(mat);
            if (shops == null) {
                shops = new ArrayList<>();
            }
            new ShopSearchGUI().ShopSearch(p, shops, mat);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        final List<String> valdArguments = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], Arrays.asList("info", "search"), valdArguments);
            return valdArguments;
        }
        if (args.length == 2 && args[0].toString().equals("search")) {
            ArrayList<String> list = new ArrayList<>();
            for (Material mat : Material.values()) {
                list.add(mat.toString().toLowerCase());
            }
            List<String> spcialItems = plugin.getItemconfig().AllSpecialItems();
            for (String str : spcialItems) {
                list.add(str.toLowerCase());
            }
            StringUtil.copyPartialMatches(args[1], list, valdArguments);
            return valdArguments;
        }

        return Arrays.asList("");
    }
}