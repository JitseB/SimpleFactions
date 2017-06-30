package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.Faction;
import net.jitse.simplefactions.factions.Role;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import net.jitse.simplefactions.utilities.LocationSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Jitse on 25-6-2017.
 */
public class SetHomeCommand extends SubCommand {

    public SetHomeCommand(Role role){
        super(role, "simplefactions.command.sethome");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Chat.format(Settings.PLAYER_ONLY_COMMAND));
            return;
        }
        Player player = (Player) sender;
        Faction faction = SimpleFactions.getInstance().getFactionsManager().getFaction(player);
        if(faction == null){
            player.sendMessage(Chat.format(Settings.COMMAND_PREFIX + Settings.NOT_IN_FACTION));
            return;
        }
        if(args.length != 2){
            player.sendMessage(Chat.format(Settings.COMMAND_USAGE_MESSAGE.replace("{syntax}", "/faction home <name>")));
            return;
        }
        SimpleFactions.getInstance().getMySql().execute("INSERT INTO FactionHomes VALUES(?,?,?);", faction.getName(), args[1], LocationSerializer.serialize(player.getLocation()));
    }
}
