package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.commands.SubCommand;
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
        super(role, "simplefactions.command.create");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Chat.format(Settings.PLAYER_ONLY_COMMAND));
            return;
        }
        Player player = (Player) sender;
        if(args.length != 2 && args.length != 3){
            player.sendMessage(Chat.format(Settings.COMMAND_USAGE_MESSAGE.replace("{syntax}", "/faction create <name> [open: yes/no] ")));
            return;
        }
        if(SimpleFactions.getInstance().getFactionsManager().getFaction(player) != null){
            player.sendMessage(Chat.format(Settings.COMMAND_PREFIX + Settings.ALREADY_IN_FACTION));
            return;
        }

        boolean open = false;
        if(args.length == 3){
            if(args[2].equalsIgnoreCase("yes")) open = true;
            else if(args[2].equalsIgnoreCase("no")) open = false;
            else{
                player.sendMessage(Chat.format(Settings.COMMAND_PREFIX + Settings.INVALID_COMMAND_USAGE));
                return;
            }
        }
        if(args[1].length() > 9){
            player.sendMessage(Chat.format(Settings.COMMAND_PREFIX + Settings.FACTION_NAME_TOO_LONG));
            return;
        }
        if(SimpleFactions.getInstance().getFactionsManager().getFaction(args[1]) != null){
            player.sendMessage(Chat.format(Settings.COMMAND_PREFIX + Settings.FACTION_ALREADY_EXISTS).replace("{name}", args[1]));
            return;
        }
        SimpleFactions.getInstance().getFactionsManager().createFaction(args[1], player, open);
    }
}
