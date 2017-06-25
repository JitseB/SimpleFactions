package net.jitse.simplefactions.managers;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.events.FactionCreatedEvent;
import net.jitse.simplefactions.factions.Faction;
import net.jitse.simplefactions.factions.Member;
import net.jitse.simplefactions.factions.Role;
import net.jitse.simplefactions.utilities.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Jitse on 23-6-2017.
 */
public class FactionsManager {

    private final SimpleFactions plugin;

    private Set<Faction> factions;

    public FactionsManager(SimpleFactions plugin){
        this.plugin = plugin;
    }

    public Faction getFaction(Player player){
        Faction result = null;
        for(Faction faction : this.factions){
            Optional<Member> memberOptional = faction.getMembers().stream().filter(member -> member.getPlayer().getUniqueId().equals(player.getUniqueId())).findFirst();
            if(memberOptional.isPresent()){
                result = faction;
                continue;
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
        else return faction.getMembers().stream().filter(member -> member.getPlayer().getUniqueId().equals(player.getUniqueId())).findFirst().get();
    }

    public void createFaction(String name, Player creator, boolean open){
        this.plugin.getMySql().execute("INSERT INTO Factions VALUES(?,?,?,?,?,?,NULL);",
                name, creator.getUniqueId().toString(), new Timestamp(System.currentTimeMillis()), Settings.PLAYER_MAX_POWER, 0, open
        );
        this.plugin.getMySql().execute("INSERT INTO FactionMembers VALUES(?,?,?,?);",
                name, creator.getUniqueId().toString(), Role.OWNER.toString(), new Timestamp(System.currentTimeMillis())
        );
        Chat.broadcast(Settings.CREATED_FACTION_BROADCAST.replace("{player}", creator.getName()).replace("{faction}", name));
        Set<Member> members = new HashSet<>();
        members.add(new Member(creator.getUniqueId(), new Timestamp(System.currentTimeMillis()), Role.OWNER));
        Bukkit.getPluginManager().callEvent(new FactionCreatedEvent(new Faction(name, creator.getUniqueId(), members, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>())));
    }
}
