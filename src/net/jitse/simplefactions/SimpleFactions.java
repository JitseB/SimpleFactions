package net.jitse.simplefactions;

import net.jitse.simplefactions.listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Jitse on 22-6-2017.
 */
public class SimpleFactions extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }
}
