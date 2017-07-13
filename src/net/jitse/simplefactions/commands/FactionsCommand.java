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
                sendHelpMessage(sender, args);
                return false;
            }
            if(args[0].equalsIgnoreCase("help")) sendHelpMessage(sender, args);
            else if(args[0].equalsIgnoreCase("create")) Commands.CREATE_FACTION.execute(sender, args);
            else if(args[0].equalsIgnoreCase("disband")) Commands.DISBAND_FACTION.execute(sender, args);
            else if(args[0].equalsIgnoreCase("enemy") || args[0].equalsIgnoreCase("setenemy")) Commands.ENEMY.execute(sender, args);
            else if(args[0].equalsIgnoreCase("ally") || args[0].equalsIgnoreCase("setally")) Commands.ALLY.execute(sender, args);
            else if(args[0].equalsIgnoreCase("neutral") || args[0].equalsIgnoreCase("setneutral")) Commands.NEUTRAL.execute(sender, args);
            else if(args[0].equalsIgnoreCase("top")) Commands.FACTION_TOP.execute(sender, args);
            else if(args[0].equalsIgnoreCase("sidebar")) Commands.SIDEBAR_TOGGLE.execute(sender, args);
            else if(args[0].equalsIgnoreCase("fly")) Commands.FLY.execute(sender, args);
            else if(args[0].equalsIgnoreCase("open")) Commands.OPEN.execute(sender, args);
            else if(args[0].equalsIgnoreCase("access")) Commands.ACCESS.execute(sender, args);
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
            else sender.sendMessage(Chat.format(Settings.COMMAND_USAGE_MESSAGE.replace("{syntax}", "/faction help")));
        }
        return true;
    }

    private void sendHelpMessage(CommandSender sender, String[] args){
        sender.sendMessage("");
        if(args.length == 2 && args[0].equalsIgnoreCase("help") && args[1].equalsIgnoreCase("2")){
            Chat.centeredMessage(sender, Chat.format("&8-----     &5&lSimpleFactions&r&5 Command List (2/3):     &8-----"));
            sender.sendMessage(getInfo("/faction kick <player>", "Kick a player from your faction."));
            sender.sendMessage(getInfo("/faction sethome [name]", "Set a home for your faction."));
            sender.sendMessage(getInfo("/faction home [name]", "Teleport yourself to a home."));
            sender.sendMessage(getInfo("/faction delhome <name>", "Delete a home from your faction."));
            sender.sendMessage(getInfo("/faction rehome [name]", "Set a faction's home to a new location."));
            sender.sendMessage(getInfo("/faction role <player> <role>", "Set a member's faction role."));
            sender.sendMessage(getInfo("/faction enemy <faction>", "Become enemies with a faction."));
            sender.sendMessage(getInfo("/faction allies <faction>", "Become allies with a faction."));
            return;
        }
        else if(args.length == 2 && args[0].equalsIgnoreCase("help") && args[1].equalsIgnoreCase("3")){
            Chat.centeredMessage(sender, Chat.format("&8-----     &5&lSimpleFactions&r&5 Command List (3/3):     &8-----"));
            sender.sendMessage(getInfo("/faction neutral <faction>", "Become neutral with a faction."));
            sender.sendMessage(getInfo("/faction open <yes | no>", "Set your faction open to join."));
            sender.sendMessage(getInfo("/faction show [faction]", "Show a (or your) faction's info."));
            sender.sendMessage(getInfo("/faction power", "Show how much power you have."));
            sender.sendMessage(getInfo("/faction fly", "Toggle fly-mode on your faction's land."));
            sender.sendMessage(getInfo("/faction top", "List the top 10 best factions of the server."));
            sender.sendMessage(getInfo("/faction access <player | faction>", "Allow players to build on this specific piece of land."));
            sender.sendMessage(getInfo("/faction sidebar", "Toggle your sidebar scoreboard (on/off)."));
            return;
        }
        Chat.centeredMessage(sender, Chat.format("&8-----     &5&lSimpleFactions&r&5 Command List (1/3):     &8-----"));
        sender.sendMessage(getInfo("/faction help [page]", "Get command help list."));
        sender.sendMessage(getInfo("/faction create <name>", "Create your own faction."));
        sender.sendMessage(getInfo("/faction join <faction>", "Join a faction."));
        sender.sendMessage(getInfo("/faction leave", "Leave your current faction."));
        sender.sendMessage(getInfo("/faction disband", "Disband your faction."));
        sender.sendMessage(getInfo("/faction claim [line, max: " + Settings.MAX_LINE_CLAIM + "]", "Claim land for your faction."));
        sender.sendMessage(getInfo("/faction autoclaim", "Toggle autoclaim-mode."));
        sender.sendMessage(getInfo("/faction invite <player>", "Invite a player to your faction."));
    }

    private String getInfo(String syntax, String description){
        return Chat.format("&f" + syntax + " &8: &7" + description);
    }

    private String getAdminInfo(String syntax, String description){
        return Chat.format("&4" + syntax + "&8: &c" + description);
    }
}
