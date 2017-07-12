package net.jitse.simplefactions.factions;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.ChunkSerializer;
import net.jitse.simplefactions.utilities.LocationSerializer;
import net.jitse.simplefactions.utilities.Logger;
import net.jitse.simplefactions.utilities.RelationState;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.scoreboard.Scoreboard;

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

    public long getOnlineMembers(){
        return this.members.stream().filter(member -> member.getBukkitOfflinePlayer().isOnline()).count();
    }

    public int getMaxPower(){
        return members.size() * Settings.PLAYER_MAX_POWER;
    }

    public void setOpen(boolean open){
        this.open = open;
        SimpleFactions.getInstance().getMySql().execute("UPDATE Factions SET open=? WHERE name=?;", open, this.name);
    }

    public boolean isOpen(){
        return this.open;
    }

    public UUID getCreator(){
        return this.creator;
    }

    public String getTag(){
        return Settings.FACTION_TAG.replace("{faction}", this.getName()) + " ";
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

    public void removeMember(Member member){
        SimpleFactions.getInstance().getFactionsTagManager().removeTag(member);
        this.members.remove(member);
        SimpleFactions.getInstance().getMySql().execute("DELETE FROM FactionMembers WHERE uuid=?;", member.getUUID().toString());
    }

    public void addHome(Home home, boolean updateSql){
        this.homes.add(home);
        if(updateSql)
            SimpleFactions.getInstance().getMySql().execute("INSERT INTO FactionHomes VALUES(?,?,?);", this.name, home.getName(), LocationSerializer.serialize(home.getLocation()));
    }

    public void setEnemy(Faction faction, boolean updateSql){
        this.enemies.add(faction);
        if(updateSql)
            SimpleFactions.getInstance().getMySql().execute("INSERT INTO FactionRelations VALUES(?,?,?);", this.getName(), faction.getName(), RelationState.ENEMIES.toString());
        for(Member member : this.members){
            if(!member.getBukkitOfflinePlayer().isOnline()) continue;
            faction.getMembers().stream().filter(factionMember -> factionMember.getBukkitOfflinePlayer().isOnline()).forEach(onlineFactionMember -> {
                Scoreboard scoreboard = onlineFactionMember.getBukkitPlayer().getScoreboard();
                if(scoreboard.getTeam(member.getBukkitPlayer().getName()) != null)
                    scoreboard.getTeam(member.getBukkitPlayer().getName()).setPrefix(Settings.ENEMY_FACTION_COLOR + this.getTag() + ChatColor.RESET);
            });
        }
        for(Member member : faction.getMembers()){
            if(!member.getBukkitOfflinePlayer().isOnline()) continue;
            this.getMembers().stream().filter(factionMember -> factionMember.getBukkitOfflinePlayer().isOnline()).forEach(onlineFactionMember -> {
                Scoreboard scoreboard = onlineFactionMember.getBukkitPlayer().getScoreboard();
                if(scoreboard.getTeam(member.getBukkitPlayer().getName()) != null)
                    scoreboard.getTeam(member.getBukkitPlayer().getName()).setPrefix(Settings.ENEMY_FACTION_COLOR + faction.getTag() + ChatColor.RESET);
            });
        }
    }

    public void setNeutral(Faction faction, boolean updateSql, boolean updateTeams){
        if(this.enemies.contains(faction)) this.enemies.remove(faction);
        if(this.allies.contains(faction)) this.allies.remove(faction);
        if(updateSql)
            SimpleFactions.getInstance().getMySql().execute("DELETE FROM FactionRelations WHERE `faction-one`=? AND `faction-two`=?;", this.getName(), faction.getName());
        if(updateTeams){
            for(Member member : this.members){
                if(!member.getBukkitOfflinePlayer().isOnline()) continue;
                faction.getMembers().stream().filter(factionMember -> factionMember.getBukkitOfflinePlayer().isOnline()).forEach(onlineFactionMember -> {
                    Scoreboard scoreboard = onlineFactionMember.getBukkitPlayer().getScoreboard();
                    if(scoreboard.getTeam(member.getBukkitPlayer().getName()) != null)
                        scoreboard.getTeam(member.getBukkitPlayer().getName()).setPrefix(Settings.NEUTRAL_FACTION_COLOR + this.getTag() + ChatColor.RESET);
                });
            }
            for(Member member : faction.getMembers()){
                if(!member.getBukkitOfflinePlayer().isOnline()) continue;
                this.getMembers().stream().filter(factionMember -> factionMember.getBukkitOfflinePlayer().isOnline()).forEach(onlineFactionMember -> {
                    Scoreboard scoreboard = onlineFactionMember.getBukkitPlayer().getScoreboard();
                    if(scoreboard.getTeam(member.getBukkitPlayer().getName()) != null)
                        scoreboard.getTeam(member.getBukkitPlayer().getName()).setPrefix(Settings.NEUTRAL_FACTION_COLOR + faction.getTag() + ChatColor.RESET);
                });
            }
        }
    }

    public void setAllies(Faction faction, boolean updateSql){
        this.allies.add(faction);
        if(updateSql)
            SimpleFactions.getInstance().getMySql().execute("INSERT INTO FactionRelations VALUES(?,?,?);", this.getName(), faction.getName(), RelationState.ALLIES.toString());
        for(Member member : this.members){
            if(!member.getBukkitOfflinePlayer().isOnline()) continue;
            faction.getMembers().stream().filter(factionMember -> factionMember.getBukkitOfflinePlayer().isOnline()).forEach(onlineFactionMember -> {
                Scoreboard scoreboard = onlineFactionMember.getBukkitPlayer().getScoreboard();
                if(scoreboard.getTeam(member.getBukkitPlayer().getName()) != null)
                    scoreboard.getTeam(member.getBukkitPlayer().getName()).setPrefix(Settings.ALLY_FACTION_COLOR + this.getTag() + ChatColor.RESET);
            });
        }
        for(Member member : faction.getMembers()){
            if(!member.getBukkitOfflinePlayer().isOnline()) continue;
            this.getMembers().stream().filter(factionMember -> factionMember.getBukkitOfflinePlayer().isOnline()).forEach(onlineFactionMember -> {
                Scoreboard scoreboard = onlineFactionMember.getBukkitPlayer().getScoreboard();
                if(scoreboard.getTeam(member.getBukkitPlayer().getName()) != null)
                    scoreboard.getTeam(member.getBukkitPlayer().getName()).setPrefix(Settings.ALLY_FACTION_COLOR + faction.getTag() + ChatColor.RESET);
            });
        }
    }
}
