package net.jitse.simplefactions.factions;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.ChunkSerializer;
import net.jitse.simplefactions.utilities.LocationSerializer;
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
    private Map<Chunk, List<Partner>> partnerMap = new HashMap<>();
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
        this.partnerMap = new HashMap<>();
    }

    public double getBalance(){
        double total = 0;
        for(Member member : this.members){
            total += SimpleFactions.getInstance().getEconomy().getBalance(member.getBukkitOfflinePlayer());
        }
        return total;
    }

    public Map<Chunk, List<Partner>> getPartners(){
        return this.partnerMap;
    }

    public List<Partner> getPartners(Chunk chunk){
        return this.partnerMap.get(chunk);
    }

    public void addPartner(Chunk chunk, Partner partner, boolean updateSql){
        List<Partner> temp;
        if(this.partnerMap.containsKey(chunk)){
            temp = this.partnerMap.get(chunk);
            this.partnerMap.remove(chunk);
        } else temp = new ArrayList<>();
        temp.add(partner);
        this.partnerMap.put(chunk, temp);
        if(updateSql)
            SimpleFactions.getInstance().getMySql().execute("INSERT INTO FactionTrusted VALUES(?,?,?);", this.name, partner.getInfo(), ChunkSerializer.toString(chunk));
    }

    public void removePartner(Partner partner, boolean updateSql){
        for(Map.Entry<Chunk, List<Partner>> entry : this.partnerMap.entrySet()){
            if(entry.getValue().contains(partner)) this.partnerMap.remove(entry.getKey());
        }
        if(updateSql)
            SimpleFactions.getInstance().getMySql().execute("DELETE FROM FactionTrusted WHERE partner=?;", partner.getInfo());
    }

    public Timestamp getFounded(){
        return this.founded;
    }

    public int getTotalPower() {
        int total = 0;
        for(Member member : this.members)
            total += member.getPower();
        return total;
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
