package net.jitse.simplefactions.managers;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.factions.Member;
import net.jitse.simplefactions.factions.Player;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
        team.setPrefix(Settings.OWN_FACTION_COLOR.toString() + member.getFaction().getTag() + ChatColor.RESET.toString());
        team.addEntry(name);
        member.getBukkitPlayer().setScoreboard(board);

        // Updating the scoreboards of the other online players.
        Bukkit.getOnlinePlayers().stream().filter(online -> !online.getUniqueId().equals(member.getUUID())).forEach(online -> {
            // Setting the nametag of member for every player online.
            Member onlineMember = SimpleFactions.getInstance().getFactionsManager().getMember(online);
            Scoreboard onlineScoreboard = online.getScoreboard();
            Team memberTeam = onlineScoreboard.getTeam(name) == null ? onlineScoreboard.registerNewTeam(name) : onlineScoreboard.getTeam(name);
            memberTeam.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            if(onlineMember == null)
                memberTeam.setPrefix(Settings.NEUTRAL_FACTION_COLOR.toString() + member.getFaction().getTag() + ChatColor.RESET.toString());
            else{
                if(onlineMember.getFaction() == member.getFaction())
                    memberTeam.setPrefix(Settings.OWN_FACTION_COLOR.toString() + member.getFaction().getTag() + ChatColor.RESET.toString());
                else if(onlineMember.getFaction().getAllies().contains(member.getFaction()))
                    memberTeam.setPrefix(Settings.ALLY_FACTION_COLOR.toString() + member.getFaction().getTag() + ChatColor.RESET.toString());
                else if(onlineMember.getFaction().getEnemies().contains(member.getFaction()))
                    memberTeam.setPrefix(Settings.ENEMY_FACTION_COLOR.toString() + member.getFaction().getTag() + ChatColor.RESET.toString());
                else memberTeam.setPrefix(Settings.NEUTRAL_FACTION_COLOR.toString() + member.getFaction().getTag() + ChatColor.RESET.toString());
            }
            memberTeam.addEntry(name);

            // Setting nametag of online player in the newly joined member's scoreboard.
            if(onlineMember == null) return;
            String onlineName = online.getName();
            Team onlineTeam = board.getTeam(onlineName) == null ? board.registerNewTeam(onlineName) : board.getTeam(onlineName);
            onlineTeam.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            if(member.getFaction() == onlineMember.getFaction())
                onlineTeam.setPrefix(Settings.OWN_FACTION_COLOR.toString() + onlineMember.getFaction().getTag() + ChatColor.RESET.toString());
            else if(member.getFaction().getAllies().contains(onlineMember.getFaction()))
                onlineTeam.setPrefix(Settings.ALLY_FACTION_COLOR.toString() + onlineMember.getFaction().getTag() + ChatColor.RESET.toString());
            else if(member.getFaction().getEnemies().contains(onlineMember.getFaction()))
                onlineTeam.setPrefix(Settings.ENEMY_FACTION_COLOR.toString() + onlineMember.getFaction().getTag() + ChatColor.RESET.toString());
            else onlineTeam.setPrefix(Settings.NEUTRAL_FACTION_COLOR.toString() + onlineMember.getFaction().getTag() + ChatColor.RESET.toString());
            onlineTeam.addEntry(onlineName);
        });
    }

    public void initTags(Player player) {
        Scoreboard board = player.getBukkitPlayer().getScoreboard();
        Bukkit.getOnlinePlayers().stream().filter(online -> !online.getUniqueId().equals(player.getUUID())).forEach(online -> {
            Member onlineMember = SimpleFactions.getInstance().getFactionsManager().getMember(online);
            if(onlineMember == null) return;
            String onlineName = online.getName();
            Team onlineTeam = board.getTeam(onlineName) == null ? board.registerNewTeam(onlineName) : board.getTeam(onlineName);
            onlineTeam.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            onlineTeam.setPrefix(Settings.NEUTRAL_FACTION_COLOR.toString() + onlineMember.getFaction().getTag() + ChatColor.RESET.toString());
            onlineTeam.addEntry(onlineName);
        });
        player.getBukkitPlayer().setScoreboard(board);
    }
}
