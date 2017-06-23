package net.jitse.simplefactions.listeners;

import net.jitse.simplefactions.events.PlayerChangeChunkEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by Jitse on 23-6-2017.
 */
public class PlayerListener implements Listener {

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();
        if(from.getChunk().getX() != to.getChunk().getX() || from.getChunk().getZ() != to.getChunk().getZ())
            Bukkit.getPluginManager().callEvent(new PlayerChangeChunkEvent(player, from.getChunk(), to.getChunk()));
    }



    @EventHandler
    public void onPlayerChunkChange(PlayerChangeChunkEvent event){
        Player player = event.getPlayer();
        player.sendMessage("CHUNK_CHANGE: Old: x:" + event.getFrom().getX() + " z:" + event.getFrom().getZ() + " New: x:" + event.getTo().getX() + " z:" + event.getTo().getZ());
    }
}
