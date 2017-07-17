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

    public void createTeams(org.bukkit.entity.Player player, Runnable runnable){
        Bukkit.getScheduler().runTaskAsynchronously(SimpleFactions.getInstance(), () -> {
            try{
                Scoreboard scoreboard = player.getScoreboard();
                scoreboard.registerNewTeam("#sf-date");
                scoreboard.registerNewTeam("#sf-faction");
                scoreboard.registerNewTeam("#sf-power");
                scoreboard.registerNewTeam("#sf-online");
                scoreboard.registerNewTeam("#sf-network");
                scoreboard.registerNewTeam("#sf-ip");
                scoreboard.registerNewObjective("sidebar", "dummy");
                scoreboard.getObjective("sidebar").setDisplaySlot(DisplaySlot.SIDEBAR);
                scoreboard.getObjective("sidebar").setDisplayName(Chat.format(Settings.SCOREBOARD_NAME));
                runnable.run();
            } catch (Exception ignored) {}
        });
    }

    @SuppressWarnings("deprecation")
    public void set(Member member){
        if(!member.wantsSidebar()) return;
        Bukkit.getScheduler().runTaskAsynchronously(SimpleFactions.getInstance(), () -> {
            try{
                Scoreboard scoreboard = member.getBukkitPlayer().getScoreboard();
                Objective sidebar = scoreboard.getObjective("sidebar");

                scoreboard.resetScores(Chat.format("&r"));
                scoreboard.resetScores(Chat.format("Hey! New here?"));
                scoreboard.resetScores(Chat.format("Please use"));
                scoreboard.resetScores(Chat.format("&e/faction help"));
                scoreboard.resetScores(Chat.format("to get started!"));

                sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
                sidebar.setDisplayName(Chat.format(Settings.SCOREBOARD_NAME));

                Team dateTeam = scoreboard.getTeam("#sf-date");
                OfflinePlayer datePlayer = Bukkit.getOfflinePlayer(Chat.format("&7"));
                dateTeam.addEntry(datePlayer.getName());
                dateTeam.setSuffix(DateTimeFormatter.ofPattern(Settings.DATE_NOTATION).format(LocalDate.now()));
                sidebar.getScore(datePlayer).setScore(10);

                sidebar.getScore(Chat.format("&r")).setScore(9);

                sidebar.getScore(Chat.format("&fFaction:")).setScore(8);

                Team factionTeam = scoreboard.getTeam("#sf-faction");
                OfflinePlayer factionPlayer = Bukkit.getOfflinePlayer(Chat.format("&r&c"));
                factionTeam.addEntry(factionPlayer.getName());
                factionTeam.setSuffix(member.getFaction().getName());
                sidebar.getScore(factionPlayer).setScore(7);

                sidebar.getScore(Chat.format("&fPower:")).setScore(6);

                Team powerTeam = scoreboard.getTeam("#sf-power");
                OfflinePlayer powerPlayer = Bukkit.getOfflinePlayer(Chat.format("&r&r&f/&c"));
                powerTeam.addEntry(powerPlayer.getName());
                powerTeam.setPrefix(Chat.format("&c" + member.getFaction().getClaimedChunks().size() + "&f/&c" + member.getFaction().getTotalPower()));
                powerTeam.setSuffix(String.valueOf(member.getFaction().getMaxPower()));
                sidebar.getScore(powerPlayer).setScore(5);

                sidebar.getScore(Chat.format("&fOnline:")).setScore(4);

                Team onlineTeam = scoreboard.getTeam("#sf-online");
                OfflinePlayer onlinePlayer = Bukkit.getOfflinePlayer(Chat.format("&r&f/&c"));
                onlineTeam.addEntry(onlinePlayer.getName());
                onlineTeam.setPrefix(Chat.format("&c" + member.getFaction().getOnlineMembers()));
                onlineTeam.setSuffix(Chat.format(String.valueOf(member.getFaction().getMembers().size())));
                sidebar.getScore(onlinePlayer).setScore(3);

                sidebar.getScore(Chat.format("&r&r")).setScore(2);

                Team networkTeam = scoreboard.getTeam("#sf-network");
                OfflinePlayer networkPlayer = Bukkit.getOfflinePlayer(Chat.format("&f/&c"));
                networkTeam.addEntry(networkPlayer.getName());
                networkTeam.setPrefix(Chat.format("&fNetwork: &c" + String.valueOf(SimpleFactions.getInstance().getServerDataManager().getCachedPlayerCount("ALL"))));
                networkTeam.setSuffix(Chat.format(String.valueOf(Settings.MAX_PROXY_PLAYERS)));
                sidebar.getScore(networkPlayer).setScore(1);

                Team ipTeam = scoreboard.getTeam("#sf-ip") == null ? scoreboard.registerNewTeam("#sf-ip") : scoreboard.getTeam("#sf-ip");
                OfflinePlayer ipPlayer = Bukkit.getOfflinePlayer(Chat.format("&7decimatepvp"));
                ipTeam.addEntry(ipPlayer.getName());
                ipTeam.setPrefix(Chat.format("&7play."));
                ipTeam.setSuffix(Chat.format("&7.com"));
                sidebar.getScore(ipPlayer).setScore(0);
            } catch(NullPointerException ignored){ ignored.printStackTrace(); } // State occurs while leaving the server during the loading of the sidebar.
        });
    }

    @SuppressWarnings("deprecation")
    public void set(Player player){
        if(!player.wantsSidebar()) return;
        Bukkit.getScheduler().runTaskAsynchronously(SimpleFactions.getInstance(), () -> {
            try{
                Scoreboard scoreboard = player.getBukkitPlayer().getScoreboard();
                Objective sidebar = scoreboard.getObjective("sidebar");

                scoreboard.resetScores(Bukkit.getOfflinePlayer(Chat.format("&r&c")));
                scoreboard.resetScores(Bukkit.getOfflinePlayer(Chat.format("&r&r&f/&c")));
                scoreboard.resetScores(Bukkit.getOfflinePlayer(Chat.format("&r&f/&c")));
                scoreboard.resetScores(Chat.format("&fOnline:"));
                scoreboard.resetScores(Chat.format("&fPower:"));
                scoreboard.resetScores(Chat.format("&fFaction:"));
                scoreboard.getTeam("#sf-faction").getEntries().forEach(entry -> scoreboard.getTeam("#sf-faction").removeEntry(entry));
                scoreboard.getTeam("#sf-power").getEntries().forEach(entry -> scoreboard.getTeam("#sf-power").removeEntry(entry));
                scoreboard.getTeam("#sf-online").getEntries().forEach(entry -> scoreboard.getTeam("#sf-online").removeEntry(entry));

                if(scoreboard.getTeam("#sf-player") == null) scoreboard.registerNewTeam("#sf-player");

                Team dateTeam = scoreboard.getTeam("#sf-date");
                OfflinePlayer datePlayer = Bukkit.getOfflinePlayer(Chat.format("&7"));
                dateTeam.addEntry(datePlayer.getName());
                dateTeam.setSuffix(DateTimeFormatter.ofPattern(Settings.DATE_NOTATION).format(LocalDate.now()));
                sidebar.getScore(datePlayer).setScore(8);

                sidebar.getScore(Chat.format("&r")).setScore(7);

                sidebar.getScore(Chat.format("Hey! New here?")).setScore(6);
                sidebar.getScore(Chat.format("Please use")).setScore(5);
                sidebar.getScore(Chat.format("&e/faction help")).setScore(4);
                sidebar.getScore(Chat.format("to get started!")).setScore(3);

                sidebar.getScore(Chat.format("&r&r")).setScore(2);

                Team networkTeam = scoreboard.getTeam("#sf-network");
                OfflinePlayer networkPlayer = Bukkit.getOfflinePlayer(Chat.format("&f/&c"));
                networkTeam.addEntry(networkPlayer.getName());
                networkTeam.setPrefix(Chat.format("&fNetwork: &c" + String.valueOf(SimpleFactions.getInstance().getServerDataManager().getCachedPlayerCount("ALL"))));
                networkTeam.setSuffix(Chat.format(String.valueOf(Settings.MAX_PROXY_PLAYERS)));
                sidebar.getScore(networkPlayer).setScore(1);

                Team ipTeam = scoreboard.getTeam("#sf-ip");
                OfflinePlayer ipPlayer = Bukkit.getOfflinePlayer(Chat.format("&7decimatepvp"));
                ipTeam.addEntry(ipPlayer.getName());
                ipTeam.setPrefix(Chat.format("&7play."));
                ipTeam.setSuffix(Chat.format("&7.com"));
                sidebar.getScore(ipPlayer).setScore(0);
            } catch(NullPointerException ignored){} // State occurs while leaving the server during the loading of the sidebar.
        });
    }

    public void startRunnables(SimpleFactions plugin){
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> plugin.getServerDataManager().requestPlayerCount("ALL"), 0, 10);
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            for(org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()){
                try{
                    Player fplayer = plugin.getFactionsManager().getFactionsPlayer(player);
                    Member member = plugin.getFactionsManager().getMember(player);
                    Scoreboard scoreboard = player.getScoreboard();

                    if(plugin.getFactionsManager().getFactionsPlayer(player) == null || !fplayer.wantsSidebar()) continue;
                    if(scoreboard.getObjective("sidebar") == null) this.createTeams(player, null);
                    if(member == null){
                        if(scoreboard.getTeam("#sf-player") == null) this.set(fplayer);
                        else{
                            scoreboard.getTeam("#sf-date").setSuffix(DateTimeFormatter.ofPattern(Settings.DATE_NOTATION).format(LocalDate.now()));
                            scoreboard.getTeam("#sf-network").setPrefix(Chat.format("&fNetwork: &c" + String.valueOf(SimpleFactions.getInstance().getServerDataManager().getCachedPlayerCount("ALL"))));
                            scoreboard.getTeam("#sf-network").setSuffix(Chat.format(String.valueOf(Settings.MAX_PROXY_PLAYERS)));
                        }
                    } else{
                        if(scoreboard.getTeam("#sf-player") != null){
                            this.set(member);
                            scoreboard.getTeam("#sf-player").unregister();
                        } else{
                            scoreboard.getTeam("#sf-faction").setSuffix(member.getFaction().getName());
                            scoreboard.getTeam("#sf-power").setPrefix(Chat.format("&c" + member.getFaction().getClaimedChunks().size() + "&f/&c" + member.getFaction().getTotalPower()));
                            scoreboard.getTeam("#sf-power").setSuffix(String.valueOf(member.getFaction().getMaxPower()));
                            scoreboard.getTeam("#sf-online").setPrefix(Chat.format("&c" + member.getFaction().getOnlineMembers()));
                            scoreboard.getTeam("#sf-online").setSuffix(Chat.format(String.valueOf(member.getFaction().getMembers().size())));
                        }
                    }
                } catch (Exception ignored) {
                    continue; // Just skip when getting an error.
                }
            }
        }, 0, 10);
    }
}
