package net.jitse.simplefactions.managers;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.events.FactionCreatedEvent;
import net.jitse.simplefactions.factions.Faction;
import net.jitse.simplefactions.factions.Member;
import net.jitse.simplefactions.factions.Role;
import net.jitse.simplefactions.utilities.Chat;
import net.jitse.simplefactions.utilities.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Jitse on 23-6-2017.
 */
public class FactionsManager {

    private final SimpleFactions plugin;

    private Set<Faction> factions = new HashSet<>();

    public FactionsManager(SimpleFactions plugin){
        this.plugin = plugin;
    }

    public Faction getFaction(Player player){
        Faction result = null;
        for(Faction faction : this.factions){
            Optional<Member> memberOptional = faction.getMembers().stream().filter(member -> member.getUUID().equals(player.getUniqueId())).findFirst();
            if(memberOptional.isPresent()){
                result = faction;
                break;
            }
        }
        return result;
    }

    public Set<Faction> getFactions(){
        return this.factions;
    }

    public void disbandFaction(Faction faction){
        SimpleFactions.getInstance().getMySql().execute("DELETE FROM Factions WHERE creator=?;", faction.getCreator().toString());
        SimpleFactions.getInstance().getMySql().execute("DELETE FROM FactionMembers WHERE faction=?;", faction.getName());
        SimpleFactions.getInstance().getMySql().execute("DELETE FROM FactionHomes WHERE faction=?;", faction.getName());
        SimpleFactions.getInstance().getMySql().execute("DELETE FROM FactionTrusted WHERE faction=?;", faction.getName());
        SimpleFactions.getInstance().getMySql().execute("DELETE FROM FactionRelations WHERE `faction-one`=? OR `faction-two`=?;", faction.getName(), faction.getName());
        faction.getMembers().stream().filter(member -> member.getBukkitOfflinePlayer().isOnline()).forEach(member -> {
            Scoreboard scoreboard = member.getBukkitPlayer().getScoreboard();
            faction.getEnemies().forEach(enemyFaction -> {
                enemyFaction.getMembers().stream().filter(enemyMember -> enemyMember.getBukkitOfflinePlayer().isOnline()).forEach(onlineEnemyMember -> {
                    if(scoreboard.getTeam(onlineEnemyMember.getBukkitPlayer().getName()) != null)
                        scoreboard.getTeam(onlineEnemyMember.getBukkitPlayer().getName()).setPrefix(Settings.NEUTRAL_FACTION_COLOR + enemyFaction.getTag());
                });
            });
            faction.getAllies().forEach(allyFaction -> {
                allyFaction.getMembers().stream().filter(allyMember -> allyMember.getBukkitOfflinePlayer().isOnline()).forEach(onlineAllyMember -> {
                    if(scoreboard.getTeam(onlineAllyMember.getBukkitPlayer().getName()) != null)
                        scoreboard.getTeam(onlineAllyMember.getBukkitPlayer().getName()).setPrefix(Settings.NEUTRAL_FACTION_COLOR + allyFaction.getTag());
                });
            });
            SimpleFactions.getInstance().getFactionsManager().getFactionsPlayer(member.getBukkitPlayer()).setPower(member.getPower());
            SimpleFactions.getInstance().getFactionsTagManager().removeTag(member);
        });
        this.factions.remove(faction);
    }

    public Faction getFaction(Chunk chunk){
        Faction result = null;
        for(Faction faction : this.factions){
            Optional<Chunk> chunkOptional = faction.getClaimedChunks().stream().filter(claimed -> claimed.getX() == chunk.getX() && claimed.getZ() == chunk.getZ()).findFirst();
            if(chunkOptional.isPresent()){
                result = faction;
                break;
            }
        }
        return result;
    }

    public Faction getFaction(String name){
        Optional<Faction> factionOptional = this.factions.stream().filter(faction -> faction.getName().equalsIgnoreCase(name)).findFirst();
        if(factionOptional.isPresent()) return factionOptional.get();
        return null;
    }

    public void init(Set<Faction> factions){
        this.factions = factions;
    }

    public Member getMember(Player player){
        Faction faction = getFaction(player);
        if(faction == null) return null;
        else return faction.getMembers().stream().filter(member -> member.getUUID().equals(player.getUniqueId())).findFirst().get();
    }

    public net.jitse.simplefactions.factions.Player getFactionsPlayer(Player player){
        return this.plugin.getPlayers().stream().filter(fplayer -> fplayer.getUUID().equals(player.getUniqueId())).findFirst().get();
    }

    public net.jitse.simplefactions.factions.Player getFactionsOfflinePlayer(OfflinePlayer player){
        return this.plugin.getPlayers().stream().filter(fplayer -> fplayer.getUUID().equals(player.getUniqueId())).findFirst().get();
    }

    public void createFaction(String name, Player creator, boolean open){
        this.plugin.getMySql().execute("INSERT INTO Factions VALUES(?,?,?,?,?,?,NULL);",
                name, creator.getUniqueId().toString(), new Timestamp(System.currentTimeMillis()), Settings.PLAYER_MAX_POWER, 0, open
        );
        Chat.broadcast(Settings.CREATED_FACTION_BROADCAST.replace("{player}", creator.getName()).replace("{faction}", name));
        Set<Member> members = new HashSet<>();
        Member member = new Member(creator.getUniqueId(), new Timestamp(System.currentTimeMillis()), Role.OWNER, this.getFactionsPlayer(creator));
        Faction createdFaction = new Faction(name, creator.getUniqueId(), members, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), open, new Timestamp(System.currentTimeMillis()));
        createdFaction.addMember(member, true, false);
        this.factions.add(createdFaction);
        Bukkit.getPluginManager().callEvent(new FactionCreatedEvent(createdFaction));
        this.plugin.getFactionsTagManager().removeTag(this.getFactionsPlayer(creator));
        this.plugin.getFactionsTagManager().initTag(member);
    }
}
