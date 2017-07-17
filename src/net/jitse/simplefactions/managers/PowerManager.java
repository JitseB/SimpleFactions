package net.jitse.simplefactions.managers;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.factions.Member;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by Jitse on 10-7-2017.
 */
public class PowerManager {

    public void startRunnables(SimpleFactions plugin){
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            for(Player player : Bukkit.getOnlinePlayers()){
                try{
                    net.jitse.simplefactions.factions.Player fplayer = plugin.getFactionsManager().getFactionsPlayer(player);
                    Member member = plugin.getFactionsManager().getMember(player);
                    if(fplayer == null) continue;
                    if(member == null){
                        if(fplayer.getPower() >= Settings.PLAYER_MAX_POWER) continue;
                        int oldPower = fplayer.getPower();
                        fplayer.setPower(oldPower + 1);
                    } else{
                        if(member.getPower() >= Settings.PLAYER_MAX_POWER) continue;
                        int oldPower = member.getPower();
                        member.setPower(oldPower + 1);
                    }
                } catch (Exception ignored){ continue; } // Ignored because of async stuff.
            }
        }, 0, 3 * 60 * 20);
    }

}
