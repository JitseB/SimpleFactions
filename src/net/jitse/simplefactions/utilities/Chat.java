package net.jitse.simplefactions.utilities;

import org.bukkit.ChatColor;

/**
 * Created by Jitse on 23-6-2017.
 */
public class Chat {

    public static String format(String input){
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}
