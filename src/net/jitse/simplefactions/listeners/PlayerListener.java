package net.jitse.simplefactions.listeners;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.events.PlayerChangeChunkEvent;
import net.jitse.simplefactions.events.PlayerLeaveServerEvent;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import net.jitse.simplefactions.utilities.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

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
            event.setKickMessage(Chat.format(Settings.SERVER_NAME + "\n\n" + Settings.LOADING_KICK_MESSAGE));
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        this.plugin.getMySql().select("SELECT * FROM FactionPlayers WHERE uuid=?;", resultSet -> {
            try {
                if (resultSet.next()){
                    this.plugin.addPlayer(new net.jitse.simplefactions.factions.Player(
                            UUID.fromString(resultSet.getString("uuid")), resultSet.getInt("kills"),
                            resultSet.getInt("deaths"), resultSet.getInt("power"), resultSet.getTimestamp("lastseen")
                    ));
                }
                else{
                    this.plugin.getMySql().execute("INSERT INTO FactionPlayers VALUES(?,?,?,?,?);",
                            player.getUniqueId().toString(), new Timestamp(System.currentTimeMillis()), 100, 0, 0
                    );
                }
            } catch (SQLException exception) {
                player.kickPlayer(Chat.format(Settings.SERVER_NAME + "\n\n" + Settings.FATAL_LOAD_KICK));
                Logger.log(Logger.LogLevel.ERROR, "An SQL error occured while trying to load " + player.getName() + "'s profile.");
                exception.printStackTrace();
            }
        }, player.getUniqueId());
        event.setJoinMessage(null);
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event){
        Bukkit.getPluginManager().callEvent(new PlayerLeaveServerEvent(event.getPlayer()));
        event.setQuitMessage(null);
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerLeaveServer(PlayerLeaveServerEvent event){
        Player player = event.getPlayer();
        this.plugin.getMySql().execute("UPDATE FactionPlayers SET lastseen=? WHERE uuid=?;", new Timestamp(System.currentTimeMillis()), player.getUniqueId().toString());
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerKick(PlayerKickEvent event){
        Bukkit.getPluginManager().callEvent(new PlayerLeaveServerEvent(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerChunkChange(PlayerChangeChunkEvent event){
        Player player = event.getPlayer();
        player.sendMessage("CHUNK_CHANGE: Old: x:" + event.getFrom().getX() + " z:" + event.getFrom().getZ() + " New: x:" + event.getTo().getX() + " z:" + event.getTo().getZ());
    }
}
