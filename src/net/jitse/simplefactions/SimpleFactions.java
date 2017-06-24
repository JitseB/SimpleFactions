package net.jitse.simplefactions;

import net.jitse.simplefactions.listeners.PlayerListener;
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

    private boolean joinable = false;

    @Override
    public void onEnable() {
        plugin = this;

        this.mysql = new MySql("localhost", 3306, "root", "password", "projects");

        this.mysql.createTable("Factions", "");

        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);

        new FactionsLoader(this).load(() -> {
            Logger.log(Logger.LogLevel.SUCCESS, "Plugin loaded, ready for duty!");
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
}
