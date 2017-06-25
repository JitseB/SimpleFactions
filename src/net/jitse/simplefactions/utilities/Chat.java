package net.jitse.simplefactions.utilities;

import net.jitse.simplefactions.managers.Settings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * Created by Jitse on 23-6-2017.
 */
public class Chat {

    public static String format(String input){
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static void broadcast(String input){
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(Chat.format(Settings.COMMAND_PREFIX + input)));
        Logger.log(Logger.LogLevel.INFO, Chat.format(input));
    }
}
