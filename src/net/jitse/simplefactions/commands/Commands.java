package net.jitse.simplefactions.commands;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.commands.subcommands.CreateFactionCommand;
import net.jitse.simplefactions.commands.subcommands.DisbandFactionCommand;
import net.jitse.simplefactions.factions.Member;
import net.jitse.simplefactions.factions.Role;
import net.jitse.simplefactions.managers.Settings;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Created by Jitse on 25-6-2017.
 */
public enum Commands {

    CREATE_FACTION(new CreateFactionCommand(Role.MEMBER)),
    DISBAND_FACTION(new DisbandFactionCommand(Role.OWNER));

    private SubCommand subCommand;

    Commands(SubCommand subCommand){
        this.subCommand = subCommand;
    }

    public void execute(CommandSender sender, String[] args){
        if(!(sender instanceof Player)) this.subCommand.onExecute(sender, Arrays.copyOfRange(args, 1, args.length));
        else{
            Player player = (Player) sender;
            Member member = SimpleFactions.getInstance().getFactionsManager().getMember(player);
            if(member == null && !args[0].equalsIgnoreCase("create")){
                player.sendMessage(Settings.NO_PERMISSION_FOR_COMMAND.replace("{role}", this.subCommand.getRole().toString().toLowerCase()));
                return;
            } else this.subCommand.onExecute(sender, Arrays.copyOfRange(args, 1, args.length));
        }
    }
}
