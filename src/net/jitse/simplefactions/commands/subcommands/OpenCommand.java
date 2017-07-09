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
 * Created by Jitse on 9-7-2017.
 */
public class OpenCommand extends SubCommand {

    public OpenCommand(Role role){
        super(role, "simplefactions.commands.open");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Chat.format(Settings.PLAYER_ONLY_COMMAND));
            return;
        }
        Player player = (Player) sender;
        Faction faction = SimpleFactions.getInstance().getFactionsManager().getFaction(player);
        if(args.length != 2){
            player.sendMessage(Chat.format(Settings.COMMAND_USAGE_MESSAGE.replace("{syntax}", "/faction open <yes/no>")));
            return;
        }
        if(!(args[1].equalsIgnoreCase("yes") || args[1].equalsIgnoreCase("no"))){
            player.sendMessage(Chat.format(Settings.INVALID_COMMAND_USAGE));
            return;
        }
        Boolean newState = args[1].equalsIgnoreCase("yes") ? true : false;
        faction.setOpen(newState);
        player.sendMessage(Chat.format(Settings.UPDATED_FACTION_OPEN.replace("{newstate}", (newState ? "open" : "closed"))));
        if(newState) Chat.broadcast(Chat.format(Settings.FACTION_NOW_OPEN.replace("{faction}", faction.getName())));
    }
}
