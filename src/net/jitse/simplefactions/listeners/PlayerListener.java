package net.jitse.simplefactions.listeners;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.commands.subcommands.AutoClaimCommand;
import net.jitse.simplefactions.events.PlayerChangeChunkEvent;
import net.jitse.simplefactions.events.PlayerLeaveServerEvent;
import net.jitse.simplefactions.factions.ChatChannel;
import net.jitse.simplefactions.factions.Faction;
import net.jitse.simplefactions.factions.Member;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import net.jitse.simplefactions.utilities.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Jitse on 23-6-2017.
 */
public class PlayerListener implements Listener {

    private static Map<UUID, ChatChannel> playerChatChannelMap = new HashMap<>();

    private final SimpleFactions plugin;

    public PlayerListener(SimpleFactions plugin){
        this.plugin = plugin;
    }

    public static Map<UUID, ChatChannel> getPlayerChatChannelMap(){
        return playerChatChannelMap;
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        if(!playerChatChannelMap.containsKey(player.getUniqueId())) return;
        Faction faction = plugin.getFactionsManager().getFaction(player);
        ChatChannel chatChannel = playerChatChannelMap.get(player.getUniqueId());
        switch (chatChannel){
            case FACTION:
                faction.getMembers().stream().filter(member -> member.getBukkitOfflinePlayer().isOnline()).forEach(onlineMember -> onlineMember.getBukkitPlayer().sendMessage(
                        Chat.format(Settings.OWN_FACTION_COLOR_CHAT_CHAT_ACCENT + player.getName() + Settings.OWN_FACTION_COLOR + ": " + event.getMessage())
                ));
                break;
            case ALLIES:
                faction.getAllies().forEach(allies -> allies.getMembers().stream().filter(member -> member.getBukkitOfflinePlayer().isOnline()).forEach(onlineAlly -> onlineAlly.getBukkitPlayer().sendMessage(
                        Chat.format(Settings.ALLY_FACTION_COLOR_CHAT_ACCENT + player.getName() + Settings.ALLY_FACTION_COLOR + ": " + event.getMessage())
                )));
                faction.getMembers().stream().filter(member -> member.getBukkitOfflinePlayer().isOnline()).forEach(onlineMember -> onlineMember.getBukkitPlayer().sendMessage(
                        Chat.format(Settings.ALLY_FACTION_COLOR_CHAT_ACCENT + player.getName() + Settings.ALLY_FACTION_COLOR + ": " + event.getMessage())
                ));
                break;
            default:
                break;
        }
        event.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event){
        net.jitse.simplefactions.factions.Player fplayer = this.plugin.getFactionsManager().getFactionsPlayer(event.getEntity());
        net.jitse.simplefactions.factions.Player member = this.plugin.getFactionsManager().getMember(event.getEntity());
        if(fplayer == null) return;
        if(member == null){
            if(fplayer.getPower() <= Settings.POWER_LOST_ON_DEATH) fplayer.setPower(0);
            else {
                int oldPower = fplayer.getPower();
                fplayer.setPower(oldPower - Settings.POWER_LOST_ON_DEATH);
            }
        } else{
            if(member.getPower() <= Settings.POWER_LOST_ON_DEATH) member.setPower(0);
            else {
                int oldPower = member.getPower();
                member.setPower(oldPower - Settings.POWER_LOST_ON_DEATH);
            }
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerLeaveServer(PlayerLeaveServerEvent event){
        Player player = event.getPlayer();
        try{
            this.plugin.getFactionsTagManager().removeTag(this.plugin.getFactionsManager().getFactionsPlayer(player));
            this.plugin.getMySql().execute("UPDATE FactionPlayers SET lastseen=? WHERE uuid=?;", new Timestamp(System.currentTimeMillis()), player.getUniqueId().toString());
            this.plugin.removePlayer(this.plugin.getFactionsManager().getFactionsPlayer(player));
        } catch(Exception ignored){} // Player had already left server -> State only occurs when /faction reset is fired.
        if(playerChatChannelMap.containsKey(player.getUniqueId())) playerChatChannelMap.remove(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerChunkChange(PlayerChangeChunkEvent event){
        Player player = event.getPlayer();
        net.jitse.simplefactions.factions.Player fplayer = this.plugin.getFactionsManager().getFactionsPlayer(player);
        Faction newLocation = this.plugin.getFactionsManager().getFaction(event.getTo());
        if(fplayer.getLocation() != newLocation){
            if(!AutoClaimCommand.getClaiming().contains(player.getUniqueId())){ // Disable sending the message when autoclaiming (spam reduce).
                if(newLocation == null) player.sendMessage(Chat.format(Settings.ENTERING_LAND.replace("{land}", Settings.WILDERNESS_NAME)));
                else player.sendMessage(Chat.format(Settings.ENTERING_LAND.replace("{land}", newLocation.getName())));
            }
            fplayer.setLocation(newLocation);
        }
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
        handlePlayerJoin(event.getPlayer());
        event.setJoinMessage(null);
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event){
        Location from = event.getFrom();
        Location to = event.getTo();
        if(from.getChunk().getX() != to.getChunk().getX() || from.getChunk().getZ() != to.getChunk().getZ())
            Bukkit.getPluginManager().callEvent(new PlayerChangeChunkEvent(event.getPlayer(), from.getChunk(), to.getChunk()));
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event){
        Bukkit.getPluginManager().callEvent(new PlayerLeaveServerEvent(event.getPlayer()));
        event.setQuitMessage(null);
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerKick(PlayerKickEvent event){
        Bukkit.getPluginManager().callEvent(new PlayerLeaveServerEvent(event.getPlayer()));
    }


    public void handlePlayerJoin(Player player){
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        this.plugin.getMySql().selectSync("SELECT * FROM FactionPlayers WHERE uuid=?;", resultSet -> {
            try {
                if (resultSet.next()){
                    net.jitse.simplefactions.factions.Player joinedPlayer = new net.jitse.simplefactions.factions.Player(
                            UUID.fromString(resultSet.getString("uuid")),
                            resultSet.getInt("power"),
                            resultSet.getTimestamp("lastseen"),
                            resultSet.getBoolean("sidebar")
                    );
                    joinedPlayer.setLocation(this.plugin.getFactionsManager().getFaction(player.getLocation().getChunk()));
                    this.plugin.addPlayer(joinedPlayer);

                    Member member = this.plugin.getFactionsManager().getMember(player);
                    if(member == null) this.plugin.getFactionsTagManager().initTags(joinedPlayer);
                    else this.plugin.getFactionsTagManager().initTag(member);
                }
                else{
                    this.plugin.getMySql().execute("INSERT INTO FactionPlayers VALUES(?,?,?,?);", player.getUniqueId().toString(), new Timestamp(System.currentTimeMillis()), 100, true);
                    net.jitse.simplefactions.factions.Player joinedPlayer = new net.jitse.simplefactions.factions.Player(
                            player.getUniqueId(),
                            100,
                            new Timestamp(System.currentTimeMillis()),
                            true
                    );
                    joinedPlayer.setLocation(this.plugin.getFactionsManager().getFaction(player.getLocation().getChunk()));
                    this.plugin.addPlayer(joinedPlayer);
                    this.plugin.getFactionsTagManager().initTags(joinedPlayer);
                }
            } catch (SQLException exception) {
                player.kickPlayer(Chat.format(Settings.SERVER_NAME + "\n\n" + Settings.FATAL_LOAD_KICK));
                Logger.log(Logger.LogLevel.ERROR, "An SQL error occured while trying to load " + player.getName() + "'s profile.");
                exception.printStackTrace();
                return;
            }
            SimpleFactions.getInstance().getSidebarManager().createTeams(player, () -> {
                if(SimpleFactions.getInstance().getFactionsManager().getFaction(player) != null)
                    this.plugin.getSidebarManager().set(SimpleFactions.getInstance().getFactionsManager().getMember(player));
                else this.plugin.getSidebarManager().set(SimpleFactions.getInstance().getFactionsManager().getFactionsPlayer(player));
            });
        }, player.getUniqueId().toString());
    }
}
