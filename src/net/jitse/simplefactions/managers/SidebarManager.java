package net.jitse.simplefactions.managers;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.factions.Member;
import net.jitse.simplefactions.factions.Player;
import net.jitse.simplefactions.utilities.Chat;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by Jitse on 10-7-2017.
 */
public class SidebarManager {

    @SuppressWarnings("deprecation")
    public void set(Member member){
        Scoreboard scoreboard = member.getBukkitPlayer().getScoreboard();
        if(scoreboard.getObjective("sidebar") != null) scoreboard.getObjective("sidebar").unregister();
        Objective sidebar = scoreboard.registerNewObjective("sidebar", "dummy");
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
        sidebar.setDisplayName(Chat.format(Settings.SCOREBOARD_NAME));

        Team dateTeam = scoreboard.registerNewTeam("#sf-date");
        OfflinePlayer datePlayer = Bukkit.getOfflinePlayer(Chat.format("&7"));
        dateTeam.addEntry(datePlayer.getName());
        dateTeam.setSuffix(DateTimeFormatter.ofPattern("MM/dd/yy").format(LocalDate.now()));
        sidebar.getScore(datePlayer).setScore(10);

        sidebar.getScore(Chat.format("&r")).setScore(9);

        sidebar.getScore(Chat.format("&fFaction:")).setScore(8);

        Team factionTeam = scoreboard.registerNewTeam("#sf-faction");
        OfflinePlayer factionPlayer = Bukkit.getOfflinePlayer(Chat.format("&r&c"));
        factionTeam.addEntry(factionPlayer.getName());
        factionTeam.setSuffix(member.getFaction().getName());
        sidebar.getScore(factionPlayer).setScore(7);

        sidebar.getScore(Chat.format("&fPower:")).setScore(6);

        Team powerTeam = scoreboard.registerNewTeam("#sf-power");
        OfflinePlayer powerPlayer = Bukkit.getOfflinePlayer(Chat.format("&r&r&f/&c"));
        powerTeam.addEntry(powerPlayer.getName());
        powerTeam.setPrefix(Chat.format("&c" + member.getFaction().getClaimedChunks().size() + "&f/&c" + member.getFaction().getPower()));
        powerTeam.setSuffix(String.valueOf(member.getFaction().getMaxPower()));
        sidebar.getScore(powerPlayer).setScore(5);

        sidebar.getScore(Chat.format("&fOnline:")).setScore(4);

        Team onlineTeam = scoreboard.registerNewTeam("#sf-online");
        OfflinePlayer onlinePlayer = Bukkit.getOfflinePlayer(Chat.format("&r&f/&c"));
        onlineTeam.addEntry(onlinePlayer.getName());
        onlineTeam.setPrefix(Chat.format("&c" + member.getFaction().getOnlineMembers()));
        onlineTeam.setSuffix(Chat.format(String.valueOf(member.getFaction().getMembers().size())));
        sidebar.getScore(onlinePlayer).setScore(3);

        sidebar.getScore(Chat.format("&r&r")).setScore(2);

        Team networkTeam = scoreboard.registerNewTeam("#sf-network");
        OfflinePlayer networkPlayer = Bukkit.getOfflinePlayer(Chat.format("&f/&c"));
        networkTeam.addEntry(networkPlayer.getName());
        networkTeam.setPrefix(Chat.format("&fNetwork: &c" + String.valueOf(SimpleFactions.getInstance().getServerDataManager().getCachedPlayerCount("ALL"))));
        networkTeam.setSuffix(Chat.format(String.valueOf(Settings.MAX_PROXY_PLAYERS)));
        sidebar.getScore(networkPlayer).setScore(1);

        Team ipTeam = scoreboard.registerNewTeam("#sf-ip");
        OfflinePlayer ipPlayer = Bukkit.getOfflinePlayer(Chat.format("&7decimate"));
        ipTeam.addEntry(ipPlayer.getName());
        ipTeam.setPrefix(Chat.format("&7play."));
        ipTeam.setSuffix(Chat.format("&7.com"));
        sidebar.getScore(ipPlayer).setScore(0);
    }

    @SuppressWarnings("deprecation")
    public void set(Player player){
        Scoreboard scoreboard = player.getBukkitPlayer().getScoreboard();
        if(scoreboard.getObjective("sidebar") != null) scoreboard.getObjective("sidebar").unregister();
        Objective sidebar = scoreboard.registerNewObjective("sidebar", "dummy");
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
        sidebar.setDisplayName(Chat.format(Settings.SCOREBOARD_NAME));

        Team dateTeam = scoreboard.registerNewTeam("#sf-date");
        OfflinePlayer datePlayer = Bukkit.getOfflinePlayer(Chat.format("&7"));
        dateTeam.addEntry(datePlayer.getName());
        dateTeam.setSuffix(DateTimeFormatter.ofPattern("MM/dd/yy").format(LocalDate.now()));
        sidebar.getScore(datePlayer).setScore(8);

        sidebar.getScore(Chat.format("&r")).setScore(7);

        sidebar.getScore(Chat.format("Hey! New here?")).setScore(6);
        sidebar.getScore(Chat.format("Please use")).setScore(5);
        sidebar.getScore(Chat.format("&e/faction help")).setScore(4);
        sidebar.getScore(Chat.format("to get started!")).setScore(3);

        sidebar.getScore(Chat.format("&r&r")).setScore(2);

        Team networkTeam = scoreboard.registerNewTeam("#sf-network");
        OfflinePlayer networkPlayer = Bukkit.getOfflinePlayer(Chat.format("&f/&c"));
        networkTeam.addEntry(networkPlayer.getName());
        networkTeam.setPrefix(Chat.format("&fNetwork: &c" + String.valueOf(SimpleFactions.getInstance().getServerDataManager().getCachedPlayerCount("ALL"))));
        networkTeam.setSuffix(Chat.format(String.valueOf(Settings.MAX_PROXY_PLAYERS)));
        sidebar.getScore(networkPlayer).setScore(1);

        Team ipTeam = scoreboard.registerNewTeam("#sf-ip");
        OfflinePlayer ipPlayer = Bukkit.getOfflinePlayer(Chat.format("&7decimate"));
        ipTeam.addEntry(ipPlayer.getName());
        ipTeam.setPrefix(Chat.format("&7play."));
        ipTeam.setSuffix(Chat.format("&7.com"));
        sidebar.getScore(ipPlayer).setScore(0);
    }
}
