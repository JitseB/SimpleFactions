package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.Faction;
import net.jitse.simplefactions.factions.Home;
import net.jitse.simplefactions.factions.Role;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jitse on 21-7-2017.
 */
public class UnclaimCommand extends SubCommand {

    public UnclaimCommand(Role role){
        super(role, "simplefactions.commands.unclaim");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Chat.format(Settings.PLAYER_ONLY_COMMAND));
            return;
        }
        Player player = (Player) sender;
        Faction faction = SimpleFactions.getInstance().getFactionsManager().getFaction(player);
        if(args.length == 2 && args[1].equalsIgnoreCase("all")){
            if(faction.getClaimedChunks().size() == 0){
                player.sendMessage(Chat.format(Settings.NO_CLAIMED_LAND));
                return;
            }
            List<Chunk> pending = new ArrayList<>();
            for(Chunk chunk : faction.getClaimedChunks()){
                List<Home> temp = new ArrayList<>();
                for(Home home : faction.getHomes()){ // Don't have to loop through ALL factions, because only faction members can place homes on their land!
                    if(chunk.equals(home.getLocation().getChunk())){
                        temp.add(home);
                        SimpleFactions.getInstance().getMySql().execute("DELETE FROM FactionHomes WHERE faction=? AND name=?;", faction.getName(), home.getName());
                    }
                }
                for(Home home : temp) faction.getHomes().remove(home);
                pending.add(chunk);
            }
            for(Chunk chunk : pending){
                faction.unclaimChunk(chunk);
            }
            player.sendMessage(Chat.format(Settings.UNCLAIMED_ALL_LAND));
        } else{
            Chunk chunk = player.getLocation().getChunk();
            if(!faction.getClaimedChunks().contains(chunk)){
                player.sendMessage(Chat.format(Settings.INVALID_COMMAND_USAGE));
                return;
            }
            List<Home> temp = new ArrayList<>();
            for(Home home : faction.getHomes()){
                if(chunk.equals(home.getLocation().getChunk())){
                    temp.add(home);
                    SimpleFactions.getInstance().getMySql().execute("DELETE FROM FactionHomes WHERE faction=? AND name=?;", faction.getName(), home.getName());
                }
            }
            for(Home home : temp) faction.getHomes().remove(home);
            faction.unclaimChunk(chunk);
            player.sendMessage(Chat.format(Settings.UNCLAIMED_LAND));
        }
    }
}
