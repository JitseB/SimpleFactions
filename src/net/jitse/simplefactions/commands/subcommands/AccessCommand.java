package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.Faction;
import net.jitse.simplefactions.factions.Partner;
import net.jitse.simplefactions.factions.Role;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Jitse on 13-7-2017.
 */
public class AccessCommand extends SubCommand {

    public AccessCommand(Role role){
        super(role, "simplefactions.commands.access");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Chat.format(Settings.PLAYER_ONLY_COMMAND));
            return;
        }
        Player player = (Player) sender;
        Faction faction = SimpleFactions.getInstance().getFactionsManager().getFaction(player);
        if(args.length != 3) {
            sender.sendMessage(Chat.format(Settings.COMMAND_USAGE_MESSAGE.replace("{syntax}", "/faction access <player | faction> <name>")));
            return;
        }
        Chunk chunk = player.getLocation().getChunk();
        if(args[1].equalsIgnoreCase("player")){
            Player target = Bukkit.getPlayer(args[2]);
            if(target == null || !target.isOnline()){
                player.sendMessage(Chat.format(Settings.INVALID_COMMAND_USAGE));
                return;
            }
            for(Map.Entry<Chunk, List<Partner>> entry : faction.getPartners().entrySet()){
                if(entry.getKey() != chunk) continue;
                for(Partner partner : entry.getValue()){
                    if(!(partner.getData() instanceof UUID)) continue;
                    if(partner.getData().equals(target.getUniqueId())){
                        player.sendMessage(Chat.format(Settings.CAN_ALREADY_BUILD));
                        return;
                    }
                }
            }
            faction.addPartner(chunk, new Partner(target.getUniqueId().toString()), true);
            player.sendMessage(Chat.format(Settings.ADDED_PARTNER));
        }
        else if(args[1].equalsIgnoreCase("faction")){
            Faction target = SimpleFactions.getInstance().getFactionsManager().getFaction(args[2]);
            if(target == null){
                player.sendMessage(Chat.format(Settings.INVALID_COMMAND_USAGE));
                return;
            }
            for(Map.Entry<Chunk, List<Partner>> entry : faction.getPartners().entrySet()){
                if(entry.getKey() != chunk) continue;
                for(Partner partner : entry.getValue()){
                    if(!(partner.getData() instanceof Faction)) continue;
                    if(((Faction) partner.getData()).getName().equals(target.getName())){
                        player.sendMessage(Chat.format(Settings.CAN_ALREADY_BUILD));
                        return;
                    }
                }
            }
            faction.addPartner(chunk, new Partner(target.getName()), true);
            player.sendMessage(Chat.format(Settings.ADDED_PARTNER));
        }
        else player.sendMessage(Chat.format(Settings.INVALID_COMMAND_USAGE));
    }
}
