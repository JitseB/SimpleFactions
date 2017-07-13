package net.jitse.simplefactions.listeners;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.factions.Faction;
import net.jitse.simplefactions.factions.Partner;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;
import java.util.UUID;

/**
 * Created by Jitse on 23-6-2017.
 */
public class WorldListener implements Listener {

    @EventHandler
    public void onPlayerBlockBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        Chunk chunk = event.getBlock().getChunk();
        Faction fplayer = SimpleFactions.getInstance().getFactionsManager().getFaction(player);
        Faction fchunk = SimpleFactions.getInstance().getFactionsManager().getFaction(chunk);
        if(fchunk != null){
            List<Partner> partnerList = fchunk.getPartners(chunk);
            if(partnerList == null){
                if(fchunk == fplayer) event.setCancelled(false);
                else {
                    event.setCancelled(true);
                    sendMessage(player, fchunk);
                }
                return;
            } else{
                for(Partner partner : partnerList){
                    if(!(partner.getData() instanceof UUID)) continue;
                    if(partner.getData().equals(player.getUniqueId()))
                        return; // Allowed standalone partner.
                }
                if(fplayer != null){
                    for(Partner partner : partnerList){
                        if(!(partner.getData() instanceof Faction)) continue;
                        if(((Faction) partner.getData()).getName().equals(fplayer.getName()))
                            return; // Allowed faction partner.
                    }
                    if(fchunk != fplayer) {
                        event.setCancelled(true);
                        sendMessage(player, fchunk);
                    }
                    return;
                }
            }
            event.setCancelled(true);
            sendMessage(player, fchunk);
        }
    }

    @EventHandler
    public void onPlayerBlockPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();
        Chunk chunk = event.getBlock().getChunk();
        Faction fplayer = SimpleFactions.getInstance().getFactionsManager().getFaction(player);
        Faction fchunk = SimpleFactions.getInstance().getFactionsManager().getFaction(chunk);
        if(fchunk != null){
            List<Partner> partnerList = fchunk.getPartners(chunk);
            if(partnerList == null){
                if(fchunk == fplayer) event.setCancelled(false);
                else {
                    event.setCancelled(true);
                    sendMessage(player, fchunk);
                }
                return;
            } else{
                for(Partner partner : partnerList){
                    if(!(partner.getData() instanceof UUID)) continue;
                    if(partner.getData().equals(player.getUniqueId()))
                        return; // Allowed standalone partner.
                }
                if(fplayer != null){
                    for(Partner partner : partnerList){
                        if(!(partner.getData() instanceof Faction)) continue;
                        if(((Faction) partner.getData()).getName().equals(fplayer.getName()))
                            return; // Allowed faction partner.
                    }
                    if(fchunk != fplayer) {
                        event.setCancelled(true);
                        sendMessage(player, fchunk);
                    }
                    return;
                }
            }
            event.setCancelled(true);
            sendMessage(player, fchunk);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        if(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK){
            Player player = event.getPlayer();
            Chunk chunk = event.getClickedBlock().getChunk();
            Faction fplayer = SimpleFactions.getInstance().getFactionsManager().getFaction(player);
            Faction fchunk = SimpleFactions.getInstance().getFactionsManager().getFaction(chunk);
            if(fchunk != null){
                List<Partner> partnerList = fchunk.getPartners(chunk);
                if(partnerList == null){
                    if(fchunk == fplayer) event.setCancelled(false);
                    else {
                        event.setCancelled(true);
                        sendMessage(player, fchunk);
                    }
                    return;
                } else{
                    for(Partner partner : partnerList){
                        if(!(partner.getData() instanceof UUID)) continue;
                        if(partner.getData().equals(player.getUniqueId()))
                            return; // Allowed standalone partner.
                    }
                    if(fplayer != null){
                        for(Partner partner : partnerList){
                            if(!(partner.getData() instanceof Faction)) continue;
                            if(((Faction) partner.getData()).getName().equals(fplayer.getName()))
                                return; // Allowed faction partner.
                        }
                        if(fchunk != fplayer) {
                            event.setCancelled(true);
                            sendMessage(player, fchunk);
                        }
                        return;
                    }
                }
                event.setCancelled(true);
                sendMessage(player, fchunk);
            }
        }
    }

    private void sendMessage(Player player, Faction faction){
        player.sendMessage(Chat.format(Settings.LAND_CLAIMED.replace("{faction}", faction.getName())));
    }
}
