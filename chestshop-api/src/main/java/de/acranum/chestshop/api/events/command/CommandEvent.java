package de.acranum.chestshop.api.events.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CommandEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final CommandSender commandSender;
    private final Command command;
    private final String s;
    private final String[] args;
    private boolean cancled;

    public CommandEvent(CommandSender commandSender, Command command, String s, String[] args) {
        this.commandSender = commandSender;
        this.command = command;
        this.s = s;
        this.args = args;
    }

    public CommandSender getCommandSender() { return commandSender;}
    public Command getCommand() { return command;}
    public String get() {return s;}
    public String[] getArgs() {return args;}

    @Override
    public boolean isCancelled() {
        return this.cancled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancled = cancel;
    }
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
