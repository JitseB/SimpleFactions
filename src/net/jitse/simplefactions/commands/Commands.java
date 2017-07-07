package net.jitse.simplefactions.commands;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.commands.subcommands.*;
import net.jitse.simplefactions.factions.Member;
import net.jitse.simplefactions.factions.Role;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Jitse on 25-6-2017.
 */
public enum Commands {

    CREATE_FACTION(new CreateFactionCommand(Role.MEMBER)),
    DISBAND_FACTION(new DisbandFactionCommand(Role.OWNER)),
    SET_HOME(new SetHomeCommand(Role.MEMBER)),
    CLAIM_LAND(new ClaimCommand(Role.MOD)),
    RESET_SYSTEM(new ResetCommand());

    private SubCommand subCommand;

    Commands(SubCommand subCommand){
        this.subCommand = subCommand;
    }

    public void execute(CommandSender sender, String[] args){
        if(!(sender instanceof Player)) this.subCommand.onExecute(sender, args);
        else{
            Player player = (Player) sender;
            if(!player.hasPermission(this.subCommand.getPermission())){
                player.sendMessage(Chat.format(Settings.NO_PERMISSION_COMMAND.replace("{permission}", this.subCommand.getPermission())));
                return;
            }
            if(player.hasPermission("simplefactions.override")){
                this.subCommand.onExecute(sender, args);
                return;
            }
            Member member = SimpleFactions.getInstance().getFactionsManager().getMember(player);
            if((member == null || member.getRole().ordinal() < this.subCommand.getRole().ordinal()) && !args[0].equalsIgnoreCase("create")){
                player.sendMessage(Chat.format(Settings.NO_ROLE_PERMISSION_COMMAND.replace("{role}", this.subCommand.getRole().toString().toLowerCase())));
                return;
            } else this.subCommand.onExecute(sender, args);
        }
    }
}
