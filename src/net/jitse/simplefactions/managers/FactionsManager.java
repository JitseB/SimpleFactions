package net.jitse.simplefactions.managers;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.factions.Faction;
import net.jitse.simplefactions.factions.Member;
import net.jitse.simplefactions.utilities.Chat;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
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

    public Member getMember(Player player){
        Faction faction = getFaction(player);
        if(faction == null) return null;
        else return faction.getMembers().stream().filter(member -> member.getPlayer().getUniqueId().equals(player.getUniqueId())).findFirst().get();
    }

    public void createFaction(String name, Player creator, boolean open){
        this.plugin.getMySql().execute("INSERT INTO Factions (?,?,?,?,?);",
                name, creator.getUniqueId().toString(), new Timestamp(System.currentTimeMillis()), Settings.PLAYER_MAX_POWER, open
        );
        Chat.broadcast(Settings.CREATED_FACTION_BROADCAST.replace("{player}", creator.getName()).replace("{faction}", name));
    }
}
