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
        if(args.length == 0){
            sender.sendMessage("TODO Help stuff");
            return false;
        }
        if(command.getName().equalsIgnoreCase("factions")){
            if(args[0].equalsIgnoreCase("create")) Commands.CREATE_FACTION.execute(sender, args);
            else if(args[0].equalsIgnoreCase("disband")) Commands.DISBAND_FACTION.execute(sender, args);
            else if(args[0].equalsIgnoreCase("sethome")) Commands.SET_HOME.execute(sender, args);
            else if(args[0].equalsIgnoreCase("delhome")) Commands.DELETE_HOME.execute(sender, args);
            else if(args[0].equalsIgnoreCase("home")) Commands.HOME.execute(sender, args);
            else if(args[0].equalsIgnoreCase("reset")) Commands.RESET_SYSTEM.execute(sender, args);
            else if(args[0].equalsIgnoreCase("claim")) Commands.CLAIM_LAND.execute(sender, args);
            else if(args[0].equalsIgnoreCase("show")) Commands.SHOW.execute(sender, args);
            else{
                sender.sendMessage("TODO Help stuff");
            }
        }
        return true;
    }
}
