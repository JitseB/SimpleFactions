package net.jitse.simplefactions.listeners;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.commands.subcommands.AutoClaimCommand;
import net.jitse.simplefactions.events.PlayerChangeChunkEvent;
import net.jitse.simplefactions.events.PlayerLeaveServerEvent;
import net.jitse.simplefactions.factions.*;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import net.jitse.simplefactions.utilities.Logger;
import net.jitse.simplefactions.utilities.RelationState;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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

    @EventHandler (priority = EventPriority.HIGHEST)
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
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player))
            return;
        Faction fplayer = SimpleFactions.getInstance().getFactionsManager().getFaction((Player) event.getEntity());
        Faction fdamager = SimpleFactions.getInstance().getFactionsManager().getFaction((Player) event.getDamager());
        if(fplayer == null || fdamager == null)
            return;
        if(fplayer.getAllies().contains(fdamager) || fplayer.equals(fdamager))
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

        // Flight logic.
        Faction playerFaction = SimpleFactions.getInstance().getFactionsManager().getFaction(player);
        Faction faction = SimpleFactions.getInstance().getFactionsManager().getFaction(event.getTo());
        if(faction == null){
            if((player.getAllowFlight() || player.isFlying()) && player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR){
                player.sendMessage(Chat.format(Settings.DISABLED_FLIGHT));
                player.teleport(new Location(
                        player.getWorld(),
                        player.getLocation().getX(),
                        player.getWorld().getHighestBlockYAt(player.getLocation()),
                        player.getLocation().getZ(),
                        player.getLocation().getYaw(),
                        player.getLocation().getPitch())
                );
                player.setAllowFlight(false);
                player.setFlying(false);
            }
            return;
        }
        boolean allowedFlight = false;
        if(playerFaction != null && faction.equals(playerFaction)){
            Role role = SimpleFactions.getInstance().getFactionsManager().getMember(player).getRole();
            switch (role){
                case MEMBER:
                    if(faction.getSetting(PermCategory.MEM, PermSetting.FLY))
                        allowedFlight = true;
                    break;
                case MOD:
                    if(faction.getSetting(PermCategory.MOD, PermSetting.FLY))
                        allowedFlight = true;
                    break;
                case OWNER:
                    allowedFlight = true;
                    break;
            }
        } else{
            if(playerFaction == null && faction.getSetting(PermCategory.NEU, PermSetting.FLY)){
                allowedFlight = true;
            } else{
                RelationState relation = faction.getAllies().contains(playerFaction) ? RelationState.ALLIES : (faction.getEnemies().contains(playerFaction) ? RelationState.ENEMIES : null);
                if(relation == null){
                    if(faction.getSetting(PermCategory.NEU, PermSetting.FLY))
                        allowedFlight = true;
                } else{
                    switch (relation){
                        case ALLIES:
                            if(faction.getSetting(PermCategory.ALL, PermSetting.FLY))
                                allowedFlight = true;
                            break;
                        case ENEMIES:
                            if(faction.getSetting(PermCategory.ENE, PermSetting.FLY))
                                allowedFlight = true;
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        if(!allowedFlight && playerFaction != null && faction.getPartners(event.getTo()) != null){
            boolean partners = false;
            for(Partner partner : faction.getPartners(event.getTo())){
                if(partner.getData() instanceof Faction && partner.getData().equals(playerFaction))
                    partners = true;
                if(partner.getData() instanceof UUID && partner.getData().equals(player.getUniqueId()))
                    partners = true;
            }
            if(partners && faction.getSetting(PermCategory.MEM, PermSetting.FLY))
                allowedFlight = true;
        }
        if(!allowedFlight && (player.getAllowFlight() || player.isFlying()) && player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR){
            player.sendMessage(Chat.format(Settings.DISABLED_FLIGHT));
            player.setAllowFlight(false);
            player.setFlying(false);
            player.teleport(new Location(
                    player.getWorld(),
                    player.getLocation().getX(),
                    player.getWorld().getHighestBlockYAt(player.getLocation()),
                    player.getLocation().getZ(),
                    player.getLocation().getYaw(),
                    player.getLocation().getPitch())
            );
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
        this.plugin.getMySql().select("SELECT * FROM FactionPlayers WHERE uuid=?;", resultSet -> {
            try {
                if (resultSet.next()){
                    net.jitse.simplefactions.factions.Player joinedPlayer = new net.jitse.simplefactions.factions.Player(
                            UUID.fromString(resultSet.getString("uuid")),
                            resultSet.getInt("power"),
                            resultSet.getTimestamp("lastseen"),
                            resultSet.getBoolean("sidebar")
                    );
                    Bukkit.getScheduler().runTask(this.plugin, () -> joinedPlayer.setLocation(this.plugin.getFactionsManager().getFaction(player.getLocation().getChunk())));
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
                    Bukkit.getScheduler().runTask(this.plugin, () -> joinedPlayer.setLocation(this.plugin.getFactionsManager().getFaction(player.getLocation().getChunk())));
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
