package net.jitse.simplefactions;

import net.jitse.simplefactions.listeners.PlayerListener;
import net.jitse.simplefactions.utilities.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Jitse on 22-6-2017.
 */
public class SimpleFactions extends JavaPlugin {

    private boolean joinable = false;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);

        new FactionLoader().load(() -> {
            Logger.log(Logger.LogLevel.SUCCESS, "Plugin loaded, ready for duty!");
            joinable = true;
        });
    }

    public boolean isJoinable(){
        return this.joinable;
    }
}
