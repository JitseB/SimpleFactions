package net.jitse.simplefactions.utilities;

import org.bukkit.Bukkit;

/**
 * Created by Jitse on 23-6-2017.
 */
public class Logger {

    public static void log(LogLevel logType, String message) {
        switch (logType) {
            case ERROR:
                Bukkit.getConsoleSender().sendMessage(Chat.format("&fSimple-Factions &7: &cError &7: &f" + message));
                break;
            case WARNING:
                Bukkit.getConsoleSender().sendMessage(Chat.format("&fSimple-Factions &7: &eWarning &7: &f" + message));
                break;
            case SUCCESS:
                Bukkit.getConsoleSender().sendMessage(Chat.format("&fSimple-Factions &7: &aSuccess &7: &f" + message));
                break;
        }
    }

    public enum LogLevel { ERROR, WARNING, SUCCESS }
}
