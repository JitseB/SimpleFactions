package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.events.PlayerLeaveServerEvent;
import net.jitse.simplefactions.factions.Faction;
import net.jitse.simplefactions.factions.Home;
import net.jitse.simplefactions.factions.Role;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Jitse on 21-7-2017.
 */
public class AdminCommand extends SubCommand implements Listener {

    private static Set<UUID> bypassing = new HashSet<>();

    public AdminCommand(){
        super(Role.MEMBER, "simplefactions.admin");
    }

    public static Set<UUID> getBypassing(){
        return bypassing;
    }

    @EventHandler
    public void onServerLeave(PlayerLeaveServerEvent event){
        if(bypassing.contains(event.getPlayer().getUniqueId()))
            bypassing.remove(event.getPlayer().getUniqueId());
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(args.length == 3 && args[1].equalsIgnoreCase("disband")){
            Faction target = SimpleFactions.getInstance().getFactionsManager().getFaction(args[2]);
            if(target == null){
                sender.sendMessage(Chat.format(Settings.INVALID_COMMAND_USAGE));
                return;
            }
            SimpleFactions.getInstance().getFactionsManager().disbandFaction(target);
            sender.sendMessage(Chat.format(Settings.FACTION_DISBAND.replace("{faction}", target.getName()).replace("{by}", "you")));
        }
        else if(args.length == 2 && args[1].equalsIgnoreCase("unclaim")){
            // Unclaim chunk player's standing in.
            if(!(sender instanceof Player)){
                sender.sendMessage(Chat.format(Settings.PLAYER_ONLY_COMMAND));
                return;
            }
            Player player = (Player) sender;
            Chunk chunk = player.getLocation().getChunk();
            Faction faction = SimpleFactions.getInstance().getFactionsManager().getFaction(chunk);
            for(Home home : faction.getHomes()){ // Don't have to loop through ALL factions, because only faction members can place homes on their land!
                if(chunk.equals(home.getLocation().getChunk())){
                    faction.getHomes().remove(home);
                    SimpleFactions.getInstance().getMySql().execute("DELETE FROM FactionHomes WHERE faction=? AND name=?;", faction.getName(), home.getName());
                }
            }
            faction.unclaimChunk(chunk);
            player.sendMessage(Chat.format(Settings.UNCLAIMED_LAND));
        }
        else if(args.length == 2 && args[1].equalsIgnoreCase("bypass")){
            if(!(sender instanceof Player)){
                sender.sendMessage(Chat.format(Settings.PLAYER_ONLY_COMMAND));
                return;
            }
            Player player = (Player) sender;
            if(bypassing.contains(player.getUniqueId())){
                bypassing.remove(player.getUniqueId());
                player.sendMessage(Chat.format(Settings.TOGGLED_BYPASS_OFF));
            } else{
                bypassing.add(player.getUniqueId());
                player.sendMessage(Chat.format(Settings.TOGGLED_BYPASS_ON));
            }
        }
        else sender.sendMessage(Chat.format(Settings.COMMAND_USAGE_MESSAGE.replace("{snytax}", "/faction <disband | unclaim | bypass> [faction]")));
    }
}
