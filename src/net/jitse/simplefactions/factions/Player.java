package net.jitse.simplefactions.factions;

import net.jitse.simplefactions.utilities.Logger;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Created by Jitse on 26-6-2017.
 */
public class Player {

    private final UUID uuid;
    private int kills, deaths, power;
    private Timestamp lastseen;

    public Player(UUID uuid, int kills, int deaths, int power, Timestamp lastseen){
        this.uuid = uuid;
        this.kills = kills;
        this.deaths = deaths;
        this.power = power;
        this.lastseen = lastseen;
    }

    public UUID getUUID(){
        return this.uuid;
    }

    public int getKills(){
        return this.kills;
    }

    public int getDeaths(){
        return this.deaths;
    }

    public int getPower(){
        return this.power;
    }

    public Timestamp getLastseen(){
        return this.lastseen;
    }

    public org.bukkit.entity.Player getBukkitPlayer() {
        org.bukkit.entity.Player player = Bukkit.getPlayer(this.uuid);
        if(player == null)
            Logger.log(Logger.LogLevel.ERROR, "Tried to use Player#getBukkitPlayer without null check, player is not online.");
        return player;
    }

    public OfflinePlayer getBukkitOfflinePlayer() {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(this.uuid);
        if(offlinePlayer == null)
            Logger.log(Logger.LogLevel.ERROR, "Tried to use Player#getBukkitOfflinePlayer without null check, player never logged on.");
        return offlinePlayer;
    }
}
