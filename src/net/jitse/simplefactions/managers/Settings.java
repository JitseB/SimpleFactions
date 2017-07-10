package net.jitse.simplefactions.managers;

import org.bukkit.ChatColor;

/**
 * Created by Jitse on 23-6-2017.
 */
public class Settings {

    public static final String COMMAND_PREFIX = "&7[&cDecimate&fPVP&7]&f ";
    public static final String SERVER_NAME = "&c&lD&6&lE&e&lC&a&lI&b&lM&9&lA&5&lT&c&lE &f&lPVP&r";
    public static final String SCOREBOARD_NAME = "&cDecimate &fPVP"; // Max 32 chars.
    public static final int PLAYER_MAX_POWER = 100;
    public static final int MAX_LINE_CLAIM = 5;
    public static final int TP_DELAY = 5;
    public static final String DEFAULT_FACTION_HOME = "Faction";
    public static final ChatColor NEUTRAL_FACTION_COLOR = ChatColor.WHITE;
    public static final ChatColor OWN_FACTION_COLOR = ChatColor.GREEN;
    public static final ChatColor ENEMY_FACTION_COLOR = ChatColor.RED;
    public static final ChatColor ALLY_FACTION_COLOR = ChatColor.LIGHT_PURPLE;
    public static final int MAX_MEMBERS = 50;
    public static final int INVITE_EXPIRE_TIME = 60;

    public static final String FACTION_ALREADY_OWNS_LAND = "&cYour faction already owns this land.";
    public static final String AUTO_CLAIMING_ENABLED = "&aAuto-Claiming is now enabled, you can start walking around now.";
    public static final String AUTO_CLAIMING_DISABLED = "&cAuto-Claiming is now disabled.";
    public static final String CANNOT_JOIN_FULL = "That faction is already at its limit of {maxplayers} players, so you can't join it.";
    public static final String CREATOR_RANK_CANNOT_KICK = "You can't kick the creator from the faction!";
    public static final String REMOVED_FROM_FACTION = "&cYou have been removed from &f&o{faction}&r&c.";
    public static final String MEMBER_REMOVED = "&fYou removed &7&o{target}&r &ffrom &7&o{faction}&r&f.";
    public static final String PLAYER_ALREADY_GOT_INVITE = "&7&o{player}&r &calready got a pending invitation to a faction!";
    public static final String INVITE_EXPIRED_TO_SENDER = "&fYour invitation to &7&o{player}&r &fhas expired.";
    public static final String INVITE_EXPIRED_TO_RECIEVER = "&cYour invitation to &f&o{faction}&r &chas expired!";
    public static final String INCOMING_INVITE = "&aIncoming invitation from &7&o{from}&r &afor the faction &e&o{faction}&r&a. The invitation will automatically expire in &7&o{expiretime}&r&a seconds.";
    public static final String CONFIRM_INVITE = "Click here to accept the invite!";
    public static final String INVITE_SEND = "&fAn invitation has been sent to &7&o{target}&r&f. The invitation will automatically expire after &7&o{expiretime}&r&f seconds.";
    public static final String CANNOT_CHANGE_OWN_RANK = "&cOops! &fYou can't change your own rank!";
    public static final String CREATOR_RANK_CANNOT_CHANGE = "&cYou can't change the rank of the creator of the faction!";
    public static final String ROLE_SET = "&7&o{player}&r &fupdated &7&o{target}&r&f's rank.";
    public static final String REMOVED_HOME = "&fHome &7&o{home}&r &fhas been removed.";
    public static final String RELOCATED_HOME = "&fHome &7&o{home}&r &fhas been relocated to your current location.";
    public static final String RE_HOME_MESSAGE = "Click here to relocate this home to your current location!";
    public static final String UPDATED_FACTION_OPEN = "&fYour faction is now &e&o{newstate}&r&f.";
    public static final String FACTION_NOW_OPEN = "&7&o{faction}&r &fno longer requires an invitation to join!";
    public static final String LEFT_FACTION_MESSAGE = "&fYou left the faction.";
    public static final String LEFT_FACTION = "&7&o{player}&r &fleft {ownfactioncolor}&o{faction}&r&f.";
    public static final String LEAVE_EQUALS_DISBAND = "&cYou are the creator of this faction, leaving it will result in disbanding the faction.";
    public static final String LEAVE_EQUALS_DISBAND_CLICK_HERE = "Click here to continue and disband the faction.";
    public static final String PLAYER_JOINED_FACTION = "&7&o{player}&r &fjoined {ownfactioncolor}&o{faction}&r&f.";
    public static final String FACTION_NOT_OPEN = "&cThat faction is currently not open nor do you have a pending invitation.";
    public static final String POWER_MESSAGE = "&fYou currently have &7&o{power}&r&7/&o{maxpower}&r&f power.";
    public static final String FACTION_NOT_EXISTS = "&cFaction &7&o{faction}&r &cdoes not exist!";
    public static final String TELEPORTING_TO_HOME = "&fTeleporting to &7&o{home}&r &fin &7&o{time}&r &fseconds. Don't move.";
    public static final String MOVED_NO_TELEPORT = "&cOops! &fYou moved location, teleportation has cancelled.";
    public static final String HOME_DOES_NOT_EXIST = "&fA home with the name &7&o{home}&r &fdoes not exist, but you can set it with &e&o/faction sethome [name]&r&f.";
    public static final String CREATED_HOME = "&fYou successfully created a new home called &7&o{home}&r&f.";
    public static final String HOME_ALREADY_EXISTS = "&cA home with the name &f&o{home}&r &calready exists. You can delete it with &e&o/faction delhome <name>&r&c.";
    public static final String FACTION_DISBAND = "&fFaction &c&o{faction}&r &fhas been disbanded by &7&o{by}&r&f.";
    public static final String CLAIMED_LINE_OF_CHUNKS = "&aYou claimed &7&o{amount}&r&a chunks in your current facing direction (&7&o{pole}&r&a).";
    public static final String NOW_IN = "&8Now in &c&o{land}&r&8.";
    public static final String ENTERING_LAND = "&8Now entering &c&o{land}&r&8.";
    public static final String WILDERNESS_NAME = "&r&a&oWilderness";
    public static final String CHUNK_ALREADY_CLAIMED = "&cThis land has already been claimed by &7&o{faction}&r&c.";
    public static final String CLAIMED_CHUNK = "&aSuccessfully claimed land! &fYou can now start building on it.";
    public static final String FACTION_ALREADY_EXISTS = "&cFaction &f&o{name} &r&calready exists, please choose a different name for your faction.";
    public static final String FACTION_NAME_TOO_LONG = "&cThe given faction name is too long, please choose one with a maximum of 9 characters.";
    public static final String SUCCESS_FULLY_RESET_SYSTEM = "&aSystem has been reset successfully.";
    public static final String SYSTEM_RESET_KICK = "&cResetting all factions...&r\n&cPlease check back later for our new season!";
    public static final String FATAL_LOAD_KICK = "&cSomething went wrong while loading your profile.";
    public static final String COMMAND_USAGE_MESSAGE = "&cUsage: &f{syntax}";
    public static final String INVALID_COMMAND_USAGE = "&cInvalid command usage.";
    public static final String PLAYER_ONLY_COMMAND = "&cSorry, but this command can only be used by players.";
    public static final String ALREADY_IN_FACTION = "&cYou can only use this command when you're not already in one. Use &e&o/faction leave &r&cto remove yourself.";
    public static final String NOT_IN_FACTION = "&cYou can only use this command when you're in a faction.";
    public static final String LOADING_KICK_MESSAGE = "&cServer is still loading all factions...";
    public static final String CREATED_FACTION_BROADCAST = "&7&o{player} &r&fcreated faction &c&o{faction}&r&f.";
    public static final String NO_PERMISSION_COMMAND = "&fSeems like you don't have permission to execute this command, you need to have access to &7&o{permission}&f.";
    public static final String NO_ROLE_PERMISSION_COMMAND = "&fSeems like you don't have the correct role for this command, you need to be at least &7&o{role}&f.";
}
