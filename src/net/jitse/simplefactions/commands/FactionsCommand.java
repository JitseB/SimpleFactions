package net.jitse.simplefactions.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Jitse on 25-6-2017.
 */
public class FactionsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("factions") || command.getName().equalsIgnoreCase("faction") || command.getName().equalsIgnoreCase("f")){
            if(args[0].equalsIgnoreCase("create")) Commands.CREATE_FACTION.execute(sender, args);
            else if(args[0].equalsIgnoreCase("disband")) Commands.DISBAND_FACTION.execute(sender, args);
        }
        return false;
    }
}
