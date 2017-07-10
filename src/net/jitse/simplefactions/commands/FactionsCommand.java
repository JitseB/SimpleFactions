package net.jitse.simplefactions.commands;

import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Jitse on 25-6-2017.
 */
public class FactionsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("factions")){
            if(args.length == 0){
                sendHelpMessage(sender);
                return false;
            }
            if(args[0].equalsIgnoreCase("help")) sendHelpMessage(sender);
            else if(args[0].equalsIgnoreCase("create")) Commands.CREATE_FACTION.execute(sender, args);
            else if(args[0].equalsIgnoreCase("disband")) Commands.DISBAND_FACTION.execute(sender, args);
            else if(args[0].equalsIgnoreCase("enemy")) Commands.ENEMY.execute(sender, args);
            else if(args[0].equalsIgnoreCase("open")) Commands.OPEN.execute(sender, args);
            else if(args[0].equalsIgnoreCase("invite")) Commands.INVITE.execute(sender, args);
            else if(args[0].equalsIgnoreCase("kick") || args[0].equalsIgnoreCase("remove")) Commands.KICK_MEMBER.execute(sender, args);
            else if(args[0].equalsIgnoreCase("leave")) Commands.LEAVE_FACTION.execute(sender, args);
            else if(args[0].equalsIgnoreCase("role") || args[0].equalsIgnoreCase("setrole")) Commands.ROLE.execute(sender, args);
            else if(args[0].equalsIgnoreCase("sethome")) Commands.SET_HOME.execute(sender, args);
            else if(args[0].equalsIgnoreCase("delhome")) Commands.DELETE_HOME.execute(sender, args);
            else if(args[0].equalsIgnoreCase("rehome")) Commands.RE_HOME.execute(sender, args);
            else if(args[0].equalsIgnoreCase("home")) Commands.HOME.execute(sender, args);
            else if(args[0].equalsIgnoreCase("reset")) Commands.RESET_SYSTEM.execute(sender, args);
            else if(args[0].equalsIgnoreCase("join")) Commands.JOIN.execute(sender, args);
            else if(args[0].equalsIgnoreCase("claim")) Commands.CLAIM_LAND.execute(sender, args);
            else if(args[0].equalsIgnoreCase("autoclaim")) Commands.AUTO_CLAIM.execute(sender, args);
            else if(args[0].equalsIgnoreCase("show")) Commands.SHOW.execute(sender, args);
            else if(args[0].equalsIgnoreCase("power") || args[0].equalsIgnoreCase("pow")) Commands.POWER.execute(sender, args);
            else{
                sender.sendMessage(Chat.format(Settings.COMMAND_USAGE_MESSAGE.replace("{syntax}", "/faction help")));
            }
        }
        return true;
    }

    private void sendHelpMessage(CommandSender sender){
        sender.sendMessage("TODO Help stuff");
    }
}
