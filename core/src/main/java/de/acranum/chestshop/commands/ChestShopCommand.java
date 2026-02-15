package de.acranum.chestshop.commands;

import de.acranum.chestshop.ChestShop;
import de.acranum.chestshop.GUIs.InfoGUI;
import de.acranum.chestshop.GUIs.ShopSearchGUI;
import de.acranum.chestshop.Util.lang;
import de.acranum.chestshop.api.addon;
import de.acranum.chestshop.api.events.command.CommandEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;

public class ChestShopCommand implements CommandExecutor, TabExecutor {

    ChestShop plugin = ChestShop.getPlugin(ChestShop.class);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        CommandEvent commandEvent = new CommandEvent(commandSender, command, s, args);
        Bukkit.getPluginManager().callEvent(commandEvent);
        if (commandEvent.isCancelled()) {
            return true;
        }
        InfoGUI InfoGUI = new InfoGUI();
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(Objects.requireNonNull(lang.getMessage("PlayerOnly")));
            return true;
        }
        Player player = (Player) commandSender;
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "info":
                    InfoGUI.InfoGUI(commandSender);
                    return true;
                case "search":
                    if (args.length > 1) search(args[1].toUpperCase(), player);
                    else player.sendMessage(lang.getMessage("noArr"));
                    return true;
                case "reload":
                    reload(player);
                    return true;
            }
        } else player.sendMessage(lang.getMessage("noArr"));
        return false;
    }

    private void search(String mat, Player p) {
        if (mat != null) {
            List<String> shops = plugin.getShopconfig().SearchItemFromShopConfig(mat, 7);
            if (shops == null) {
                shops = new ArrayList<>();
            }
            new ShopSearchGUI().ShopSearch(p, shops, mat);
        }
    }
    private void reload(Player p) {
        if (p.isOp()) {
            addon.reloadAddons();
            p.sendMessage(lang.getMessage("reloadComplete"));
        } else p.sendMessage(lang.getMessage("noPermission"));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        final List<String> valdArguments = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], Arrays.asList("info", "search", "reload"), valdArguments);
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