package net.jitse.simplefactions.managers;

/**
 * Created by Jitse on 23-6-2017.
 */
public class Settings {

    public static final String COMMAND_PREFIX = "&7[&cDecimate&fPVP&7]&f ";
    public static final String SERVER_NAME = "&c&lD&6&lE&e&lC&a&lI&b&lM&9&lA&5&lT&c&lE &f&lPVP&r";
    public static final int PLAYER_MAX_POWER = 100;

    public static final String COMMAND_USAGE_MESSAGE = "&cUsage: &f{syntax}";
    public static final String INVALID_COMMAND_USAGE = "&cInvalid command usage.";
    public static final String PLAYER_ONLY_COMMAND = "&cSorry, but this command can only be used by players.";
    public static final String ADMIN_ONLY_COMMAND = "&cSorry, but you need to be an administrator to use that command.";
    public static final String ALREADY_IN_FACTION = "&cYou can only use this command when you're not already in one. Use &e&o/faction &r&cto remove yourself.";
    public static final String NOT_IN_FACTION = "&cYou can only use this command when you're in a faction.";
    public static final String LOADING_KICK_MESSAGE = "&cServer is still loading all factions...";
    public static final String CREATED_FACTION_BROADCAST = "&7&o{player} &r&fcreated faction &c&o{faction}&r&f.";
    public static final String NO_PERMISSION_FOR_COMMAND = "&fSeems like you don't have the correct role for this command, you need to be at least &7&o{role}&f.";
}
