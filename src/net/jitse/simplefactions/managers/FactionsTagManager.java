package net.jitse.simplefactions.managers;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.factions.Member;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * Created by Jitse on 29-6-2017.
 */
public class FactionsTagManager {

    public void removeTag(net.jitse.simplefactions.factions.Player player) {
        if(SimpleFactions.getInstance().getFactionsManager().getFaction(player.getBukkitPlayer()) == null)
            return;
        // Removing the team from everyone's scoreboard.
        Bukkit.getOnlinePlayers().forEach(online -> {
            Scoreboard board = online.getScoreboard();
            String name = player.getBukkitPlayer().getName();
            Team team = board.getTeam(name) == null ? board.registerNewTeam(name) : board.getTeam(name);
            team.removeEntry(name);
            if(team.getEntries().size() <= 0) team.unregister();
            online.setScoreboard(board);
        });
    }

    public void initTag(Member member) {
        // Setting it for Member#getBukkitPlayer.
        Scoreboard board = member.getBukkitPlayer().getScoreboard();
        String name = member.getBukkitPlayer().getName();
        Team team = board.getTeam(name) == null ? board.registerNewTeam(name) : board.getTeam(name);
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        team.setPrefix(member.getFaction().getTag());
        team.addEntry(name);
        member.getBukkitPlayer().setScoreboard(board);

        // Updating the scoreboards of the other online players.
        Bukkit.getOnlinePlayers().stream().filter(online -> !online.getUniqueId().equals(member.getUUID())).forEach(online -> {
            Member onlineMember = SimpleFactions.getInstance().getFactionsManager().getMember(online);
            if(onlineMember == null) return;

            // Setting the nametag for every player online.
            Scoreboard onlineScoreboard = online.getScoreboard();
            Team team2 = onlineScoreboard.getTeam(name) == null ? onlineScoreboard.registerNewTeam(name) : onlineScoreboard.getTeam(name);
            team2.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            team2.setPrefix(member.getFaction().getTag());
            team2.addEntry(name);

            // Setting nametag of online player in the newly joined member's scoreboard.
            String onlineName = online.getName();
            Team onlineTeam = board.getTeam(onlineName) == null ? board.registerNewTeam(onlineName) : board.getTeam(onlineName);
            onlineTeam.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            onlineTeam.setPrefix(onlineMember.getFaction().getTag());
            onlineTeam.addEntry(onlineName);
        });
    }
}
