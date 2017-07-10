package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.Faction;
import net.jitse.simplefactions.factions.Player;
import net.jitse.simplefactions.factions.Role;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jitse on 10-7-2017.
 */
public class InviteCommand extends SubCommand {

    private static Map<Player, Faction> pending = new HashMap<>();

    public InviteCommand(Role role){
        super(role, "simplefactions.commands.invite");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if(!(sender instanceof org.bukkit.entity.Player)){
            sender.sendMessage(Chat.format(Settings.PLAYER_ONLY_COMMAND));
            return;
        }
        org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
        // Todo
    }

    public static Map<Player, Faction> getPending(){
        return pending;
    }
}
