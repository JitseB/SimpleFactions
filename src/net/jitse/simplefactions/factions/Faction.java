package net.jitse.simplefactions.factions;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.ChunkSerializer;
import net.jitse.simplefactions.utilities.LocationSerializer;
import org.bukkit.Chunk;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Jitse on 22-6-2017.
 */
public class Faction {

    private final String name;
    private final UUID creator;
    private final Timestamp founded;

    private Set<Member> members;
    private Set<Chunk> chunks;
    private Set<Faction> allies, enemies;
    private Set<Home> homes;
    private boolean open;

    public Faction(String name, UUID creator, Set<Member> members, Set<Chunk> chunks, Set<Faction> allies, Set<Faction> enemies, Set<Home> homes, boolean open, Timestamp founded){
        this.name = name;
        this.creator = creator;
        this.members = members;
        this.chunks = chunks;
        this.allies = allies;
        this.enemies = enemies;
        this.homes = homes;
        this.open = open;
        this.founded = founded;
    }

    public Timestamp getFounded(){
        return this.founded;
    }

    public int getPower() {
        int power = 0;
        for(Member member : this.members){
            power += member.getPower();
        }
        return power;
    }

    public int getMaxPower(){
        return members.size() * Settings.PLAYER_MAX_POWER;
    }

    public void setOpen(boolean open){
        this.open = open;
    }

    public boolean isOpen(){
        return this.open;
    }

    public UUID getCreator(){
        return this.creator;
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

    public void addMember(Member member, boolean updateSql, boolean updateScoreboards){
        this.members.add(member);
        if(updateScoreboards){
            SimpleFactions.getInstance().getFactionsTagManager().removeTag(member);
            SimpleFactions.getInstance().getFactionsTagManager().initTag(member);
        }
        if(updateSql)
            SimpleFactions.getInstance().getMySql().execute("INSERT INTO FactionMembers VALUES(?,?,?,?);", this.name, member.getUUID().toString(), member.getRole().toString(), new Timestamp(System.currentTimeMillis()));
    }

    public void addHome(Home home, boolean updateSql){
        this.homes.add(home);
        if(updateSql)
            SimpleFactions.getInstance().getMySql().execute("INSERT INTO FactionHomes VALUES(?,?,?);", this.name, home.getName(), LocationSerializer.serialize(home.getLocation()));
    }

    // Messy functions... Not sure how to do them in another way
    public void initSetEnemies(Faction faction){
        this.enemies.add(faction);
    }

    public void initSetAllies(Faction faction){
        this.allies.add(faction);
    }
}
