package net.jitse.simplefactions.listeners;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.events.PlayerChangeChunkEvent;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by Jitse on 23-6-2017.
 */
public class PlayerListener implements Listener {

    private final SimpleFactions plugin;

    public PlayerListener(SimpleFactions plugin){
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();
        if(from.getChunk().getX() != to.getChunk().getX() || from.getChunk().getZ() != to.getChunk().getZ())
            Bukkit.getPluginManager().callEvent(new PlayerChangeChunkEvent(player, from.getChunk(), to.getChunk()));
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerLogin(PlayerLoginEvent event){
        if(!plugin.isJoinable()){
            event.setKickMessage(Chat.format(Settings.SERVER_NAME + "\n\n&cServer is still loading all factions..."));
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        }
    }

    @EventHandler
    public void onPlayerChunkChange(PlayerChangeChunkEvent event){
        Player player = event.getPlayer();
        player.sendMessage("CHUNK_CHANGE: Old: x:" + event.getFrom().getX() + " z:" + event.getFrom().getZ() + " New: x:" + event.getTo().getX() + " z:" + event.getTo().getZ());
    }
}
