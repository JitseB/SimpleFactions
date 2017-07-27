package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.Faction;
import net.jitse.simplefactions.factions.Role;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Jitse on 30-6-2017.
 */
public class ClaimCommand extends SubCommand {

    public ClaimCommand(Role role){
        super(role, "simplefactions.command.claim");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Chat.format(Settings.PLAYER_ONLY_COMMAND));
            return;
        }
        Player player = (Player) sender;
        Faction faction = SimpleFactions.getInstance().getFactionsManager().getFaction(player);
        if(faction.getClaimedChunks().size() >= faction.getTotalPower()){
            player.sendMessage(Chat.format(Settings.NOT_ENOUGH_POWER));
            return;
        }
        if(args.length == 1){
            Chunk chunk = player.getLocation().getChunk();
            Faction chunkFaction = SimpleFactions.getInstance().getFactionsManager().getFaction(chunk);
            if(chunkFaction != null){
                if(chunkFaction.equals(faction)) player.sendMessage(Chat.format(Settings.FACTION_ALREADY_OWNS_LAND));
                else player.sendMessage(Chat.format(Settings.CHUNK_ALREADY_CLAIMED.replace("{faction}", chunkFaction.getName())));
                return;
            }
            Bukkit.getOnlinePlayers().stream()
                    .filter(online -> online.getLocation().getChunk().getX() == chunk.getX() && online.getLocation().getChunk().getZ() == chunk.getZ())
                    .forEach(online -> {
                        if(online != player) online.sendMessage(Chat.format(Settings.NOW_IN.replace("{land}", faction.getName())));
                        SimpleFactions.getInstance().getFactionsManager().getFactionsPlayer(online).setLocation(faction);
                    });
            faction.claimChunk(true, chunk);
            player.sendMessage(Chat.format(Settings.COMMAND_PREFIX + Settings.CLAIMED_CHUNK));
        }
        else if(args.length == 3 && args[1].equalsIgnoreCase("line")){
            try{
                int amount = Integer.parseInt(args[2]);
                if(amount > Settings.MAX_LINE_CLAIM){
                    sender.sendMessage(Chat.format(Settings.INVALID_COMMAND_USAGE));
                    return;
                }
                int yaw = Math.abs(Math.round(player.getLocation().getYaw()));
                List<Chunk> pending = new ArrayList<>();
                pending.add(player.getLocation().getChunk());
                String pole = "";
                if(315 < yaw || yaw <= 45){
                    pole = "South";
                    for (int i = 1; i < amount; i++) pending.add(player.getWorld().getChunkAt(player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ() + i));
                }
                else if(45 < yaw && yaw <= 135){
                    pole = "West";
                    for (int i = 1; i < amount; i++) pending.add(player.getWorld().getChunkAt(player.getLocation().getChunk().getX() - i, player.getLocation().getChunk().getZ()));
                }
                else if(135 < yaw && yaw <= 225){
                    pole = "North";
                    for (int i = 1; i < amount; i++) pending.add(player.getWorld().getChunkAt(player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ() - i));
                }
                else if(225 < yaw && yaw <= 315){
                    pole = "East";
                    for (int i = 1; i < amount; i++) pending.add(player.getWorld().getChunkAt(player.getLocation().getChunk().getX() + i, player.getLocation().getChunk().getZ()));
                }
                boolean failed = false;
                for(Chunk chunk : pending){
                    Faction check = SimpleFactions.getInstance().getFactionsManager().getFaction(chunk);
                    if(check != null && check != faction){
                        player.sendMessage(Chat.format(Settings.CHUNK_ALREADY_CLAIMED.replace("{faction}", check.getName())));
                        failed = true;
                        return;
                    }
                }
                if(failed) return;
                pending.forEach(checkedChunk -> {
                    if(!faction.getClaimedChunks().contains(checkedChunk)) {
                        faction.claimChunk(true, checkedChunk);
                        Bukkit.getOnlinePlayers().stream()
                                .filter(online -> online.getLocation().getChunk().getX() == checkedChunk.getX() && online.getLocation().getChunk().getZ() == checkedChunk.getZ())
                                .forEach(online -> {
                                    if(online != player) online.sendMessage(Chat.format(Settings.NOW_IN.replace("{land}", faction.getName())));
                                    SimpleFactions.getInstance().getFactionsManager().getFactionsPlayer(online).setLocation(faction);
                                });
                    }
                });
                player.sendMessage(Chat.format(Settings.CLAIMED_LINE_OF_CHUNKS.replace("{amount}", String.valueOf(amount)).replace("{pole}", pole)));
            } catch (NumberFormatException exception){
                sender.sendMessage(Chat.format(Settings.INVALID_COMMAND_USAGE));
            }
        }
        else if(args.length == 3 && args[1].equalsIgnoreCase("radius")){
            try{
                int radius = Integer.parseInt(args[2]);
                if(radius > Settings.MAX_RADIUS_CLAIM){
                    sender.sendMessage(Chat.format(Settings.INVALID_COMMAND_USAGE));
                    return;
                }

                List<Chunk> pending = new ArrayList<>();
                Chunk currentChunk = player.getLocation().getChunk();
                for (int i = 0 - radius; i <= radius; i++) {
                    for (int j = 0 - radius; j <= radius; j++) {
                        pending.add(player.getWorld().getChunkAt(currentChunk.getX() + i, currentChunk.getZ() + j));
                    }
                }

                boolean failed = false;
                for(Chunk chunk : pending){
                    Faction check = SimpleFactions.getInstance().getFactionsManager().getFaction(chunk);
                    if(check != null && check != faction){
                        player.sendMessage(Chat.format(Settings.CHUNK_ALREADY_CLAIMED.replace("{faction}", check.getName())));
                        failed = true;
                        return;
                    }
                }
                if(failed) return;
                pending.forEach(checkedChunk -> {
                    if(!faction.getClaimedChunks().contains(checkedChunk)) {
                        faction.claimChunk(true, checkedChunk);
                        Bukkit.getOnlinePlayers().stream()
                                .filter(online -> online.getLocation().getChunk().getX() == checkedChunk.getX() && online.getLocation().getChunk().getZ() == checkedChunk.getZ())
                                .forEach(online -> {
                                    if(online != player) online.sendMessage(Chat.format(Settings.NOW_IN.replace("{land}", faction.getName())));
                                    SimpleFactions.getInstance().getFactionsManager().getFactionsPlayer(online).setLocation(faction);
                                });
                    }
                });
                player.sendMessage(Chat.format(Settings.CLAIMED_RADIUS_OF_CHUNKS.replace("{radius}", String.valueOf(radius))));
            } catch (NumberFormatException exception){
                sender.sendMessage(Chat.format(Settings.INVALID_COMMAND_USAGE));
            }
        }
        else sender.sendMessage(Chat.format(Settings.COMMAND_USAGE_MESSAGE.replace("{syntax}", "/faction claim [radius, max: " + Settings.MAX_RADIUS_CLAIM + " | line, max: " + Settings.MAX_LINE_CLAIM + "]")));
    }
}
