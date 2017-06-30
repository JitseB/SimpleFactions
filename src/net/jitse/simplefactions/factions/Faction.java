package net.jitse.simplefactions.factions;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.ChunkSerializer;
import org.bukkit.Chunk;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Jitse on 22-6-2017.
 */
public class Faction {

    private final String name;
    private final UUID creator;

    private Set<Member> members;
    private Set<Chunk> chunks;
    private Set<Faction> allies, enemies;
    private Set<Home> homes;
    private int power, maxPower;

    public Faction(String name, UUID creator, Set<Member> members, Set<Chunk> chunks, Set<Faction> allies, Set<Faction> enemies, Set<Home> homes){
        this.name = name;
        this.creator = creator;
        this.members = members;
        this.chunks = chunks;
        this.allies = allies;
        this.enemies = enemies;
        this.homes = homes;
        this.maxPower = members.size() * Settings.PLAYER_MAX_POWER;
    }

    public String getTag(){
        return "[" + this.getName() + "] ";
    }

    public String getName(){
        return this.name;
    }

    public Set<Home> getHomes(){
        return this.homes;
    }

    public Set<Chunk> getClaimedChunks(){
        return this.chunks;
    }

    public Set<Member> getMembers(){
        return this.members;
    }

    public Set<Faction> getAllies(){
        return this.allies;
    }

    public Set<Faction> getEnemies(){
        return this.enemies;
    }

    public void claimChunk(boolean updateSql, Chunk... chunk){
        this.chunks.addAll(Arrays.asList(chunk));
        if(updateSql)
            SimpleFactions.getInstance().getMySql().execute("UPDATE Factions SET claimed=? WHERE name=?;", ChunkSerializer.serialize(this.chunks), this.name);
    }

    public void addMember(Member member, boolean updateSql){
        this.members.add(member);
        if(updateSql)
            SimpleFactions.getInstance().getMySql().execute("INSERT INTO FactionMembers VALUES(?,?,?,?);", this.name, member.getUUID().toString(), member.getRole().toString(), new Timestamp(System.currentTimeMillis()));
    }

    // Messy functions... Not sure how to do them in another way
    public void initAddHome(Home home){
        this.homes.add(home);
    }

    public void initSetEnemies(Faction faction){
        this.enemies.add(faction);
    }

    public void initSetAllies(Faction faction){
        this.allies.add(faction);
    }
}
