package net.jitse.simplefactions;

import net.jitse.simplefactions.listeners.PlayerListener;
import net.jitse.simplefactions.managers.FactionsManager;
import net.jitse.simplefactions.mysql.MySql;
import net.jitse.simplefactions.utilities.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Jitse on 22-6-2017.
 */
public class SimpleFactions extends JavaPlugin {

    private static SimpleFactions plugin;

    private MySql mysql;
    private FactionsManager factionsManager;

    private boolean joinable = false;

    @Override
    public void onEnable() {
        plugin = this;

        this.mysql = new MySql("localhost", 3306, "root", "password", "projects");

        this.mysql.createTable("Factions", "name VARCHAR(16), creator VARCHAR(36), created TIMESTAMP, max-power INT, open TINYINT(1), claimed TEXT");
        this.mysql.createTable("FactionHomes", "faction VARCHAR(16), name VARCHAR(16), location TEXT");
        this.mysql.createTable("FactionMembers", "faction VARCHAR(16), member VARCHAR(36), role VARCHAR(6), lastseen TIMESTAMP, joinedfaction TIMESTAMP, power INT, kills INT, deaths INT");
        this.mysql.createTable("FactionRelations", "faction-one VARCHAR(16), faction-two VARCHAR(16), relation VARCHAR(7)");

        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);

        new FactionsLoader(this).load(() -> {
            Logger.log(Logger.LogLevel.INFO, "Plugin loaded, ready for duty!");
            this.joinable = true;
        });
    }

    @Override
    public void onDisable() {
        this.mysql.close();
    }

    public static SimpleFactions getInstance(){
        return plugin;
    }

    public boolean isJoinable(){
        return this.joinable;
    }

    public MySql getMySql(){
        return this.mysql;
    }

    public FactionsManager getFactionsManager(){
        return this.factionsManager;
    }
}