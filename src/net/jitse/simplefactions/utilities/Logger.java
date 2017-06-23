package net.jitse.simplefactions.utilities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * Created by Jitse on 23-6-2017.
 */
public class Logger {

    public static void log(LogLevel logType, String message) {
        switch (logType) {
            case ERROR:
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error : " + ChatColor.WHITE + message);
                break;
            case WARNING:
                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Warning : " + ChatColor.WHITE + message);
                break;
            case SUCCESS:
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Success : " + ChatColor.WHITE + message);
                break;
        }
    }

    public enum LogLevel { ERROR, WARNING, SUCCESS }
}
