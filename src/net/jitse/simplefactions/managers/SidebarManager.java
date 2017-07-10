package net.jitse.simplefactions.managers;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.factions.Member;
import net.jitse.simplefactions.factions.Player;
import net.jitse.simplefactions.utilities.Chat;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

/**
 * Created by Jitse on 10-7-2017.
 */
public class SidebarManager {

    public void set(Player player){
        Member member = SimpleFactions.getInstance().getFactionsManager().getMember(player.getBukkitPlayer());
        Scoreboard scoreboard = player.getBukkitPlayer().getScoreboard();
        if(scoreboard.getObjective(DisplaySlot.SIDEBAR) != null)
            scoreboard.getObjective(DisplaySlot.SIDEBAR).unregister();
        Objective sidebar = scoreboard.registerNewObjective("sidebar", "dummy");
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
        sidebar.setDisplayName(Chat.format(Settings.SCOREBOARD_NAME));

        // Todo
    }
}
