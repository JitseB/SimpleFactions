package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.Faction;
import net.jitse.simplefactions.factions.Role;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Jitse on 25-6-2017.
 */
public class CreateFactionCommand extends SubCommand {

    public CreateFactionCommand(Role role){
        super(role);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Chat.format(Settings.PLAYER_ONLY_COMMAND));
            return;
        }
        Player player = (Player) sender;
        Faction faction = SimpleFactions.getInstance().getFactionsManager().getFaction(player);
        if(faction != null){
            player.sendMessage(Chat.format(Settings.ALREADY_IN_FACTION));
            return;
        }
        if(args.length < 1 || args.length > 2){
            player.sendMessage(Chat.format(Settings.COMMAND_USAGE_MESSAGE.replace("{syntax}", "/faction create <name> [open] (Open: yes/no)")));
        }

        boolean open = false;
        if(args[1] != null){
            if(args[1].equalsIgnoreCase("yes")) open = true;
            else if(args[1].equalsIgnoreCase("no")) open = false;
            else{
                player.sendMessage(Chat.format(Settings.INVALID_COMMAND_USAGE));
                return;
            }
        }
        SimpleFactions.getInstance().getFactionsManager().createFaction(args[0], player, open);
    }
}
