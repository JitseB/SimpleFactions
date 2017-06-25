package net.jitse.simplefactions.managers;

import net.jitse.simplefactions.factions.Faction;
import net.jitse.simplefactions.factions.Member;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.Set;

/**
 * Created by Jitse on 23-6-2017.
 */
public class FactionsManager {

    private Set<Faction> factions;

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
}
