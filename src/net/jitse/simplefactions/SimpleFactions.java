package net.jitse.simplefactions;

import net.jitse.simplefactions.commands.Commands;
import net.jitse.simplefactions.commands.FactionsCommand;
import net.jitse.simplefactions.factions.Player;
import net.jitse.simplefactions.listeners.PlayerListener;
import net.jitse.simplefactions.listeners.WorldListener;
import net.jitse.simplefactions.managers.FactionsTagManager;
import net.jitse.simplefactions.managers.FactionsManager;
import net.jitse.simplefactions.managers.SidebarManager;
import net.jitse.simplefactions.mysql.MySql;
import net.jitse.simplefactions.utilities.Logger;
import net.jitse.simplefactions.utilities.ServerData;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jitse on 22-6-2017.
 */
public class SimpleFactions extends JavaPlugin {

    /*
     * Todo lists:
     * - Fix relations loader (dupe bug)
     * - Add the relation commands
     * - Add PlayerListeners for handling all permissions
     * - Add chat channels (Public/Allies/Faction)
     * - Add the power system handling
     * */

    private static SimpleFactions plugin;

    private final FactionsManager factionsManager = new FactionsManager(this);
    private final FactionsTagManager factionsTagManager = new FactionsTagManager();
    private final SidebarManager sidebarManager = new SidebarManager();

    private MySql mysql;
    private ServerData serverDataManager;

    private Set<Player> players = new HashSet<>();

    private boolean joinable = false;

    @Override
    public void onEnable() {
        plugin = this;
        serverDataManager = new ServerData(this);

        this.mysql = new MySql("localhost", 3306, "root", "password", "projects");

        getCommand("factions").setExecutor(new FactionsCommand());

        this.mysql.createTable("Factions", "name VARCHAR(16), creator VARCHAR(36), created TIMESTAMP, `max-power` INT, balance INT, open TINYINT(1), claimed MEDIUMTEXT");
        this.mysql.createTable("FactionHomes", "faction VARCHAR(16), name VARCHAR(16), location TEXT");
        this.mysql.createTable("FactionMembers", "faction VARCHAR(16), uuid VARCHAR(36), role VARCHAR(6), joinedfaction TIMESTAMP");
        this.mysql.createTable("FactionPlayers", "uuid VARCHAR(36), lastseen TIMESTAMP, power INT, kills INT, deaths INT");
        this.mysql.createTable("FactionRelations", "`faction-one` VARCHAR(16), `faction-two` VARCHAR(16), relation VARCHAR(7)");

        PlayerListener playerListener = new PlayerListener(this);
        Bukkit.getPluginManager().registerEvents(playerListener, this);
        Bukkit.getPluginManager().registerEvents(new WorldListener(), this);
        Arrays.stream(Commands.values()).forEach(command -> Bukkit.getPluginManager().registerEvents(command.getSubCommand(), this));

        new FactionsLoader(this).load(() -> {
            Bukkit.getScheduler().runTask(this, () -> Bukkit.getOnlinePlayers().forEach(playerListener::handlePlayerJoin));
            Logger.log(Logger.LogLevel.INFO, "Plugin loaded, ready for duty!");
            this.joinable = true;
        });
    }

    @Override
    public void onDisable() {
        Logger.log(Logger.LogLevel.WARNING, "Reloading is not recommended. Database connection might flip out when reloading too often. Restart the server instead.");
        this.mysql.close();
    }

    public static SimpleFactions getInstance(){
        return plugin;
    }

    public void addPlayer(Player player){
        this.players.add(player);
    }

    public void removePlayer(Player player){
        this.players.remove(player);
    }

    public Set<Player> getPlayers(){
        return this.players;
    }

    public void setJoinable(boolean b){
        this.joinable = b;
    }

    public boolean isJoinable(){
        return this.joinable;
    }

    public MySql getMySql(){
        return this.mysql;
    }

    public SidebarManager getSidebarManager(){
        return this.sidebarManager;
    }

    public ServerData getServerDataManager(){
        return this.serverDataManager;
    }

    public FactionsManager getFactionsManager(){
        return this.factionsManager;
    }

    public FactionsTagManager getFactionsTagManager(){
        return this.factionsTagManager;
    }
}
