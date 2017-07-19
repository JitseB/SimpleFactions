package net.jitse.simplefactions.managers;

import net.jitse.simplefactions.SimpleFactions;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by Jitse on 23-6-2017.
 */
public class Settings {

    private static final FileConfiguration CONFIG = SimpleFactions.getInstance().getConfig();

    public static final String COMMAND_PREFIX = CONFIG.getString("CommandPrefix");
    public static final String SERVER_NAME = CONFIG.getString("ServerName");
    public static final String SCOREBOARD_NAME = CONFIG.getString("ScoreboardName"); // Max 32 chars.
    public static final int PLAYER_MAX_POWER = CONFIG.getInt("PlayerMaxPower");
    public static final int MAX_LINE_CLAIM = CONFIG.getInt("MaxLineClaim");
    public static final int MAX_RADIUS_CLAIM = CONFIG.getInt("MaxRadiusClaim");
    public static final int TP_DELAY = CONFIG.getInt("TpDelay");
    public static final String DEFAULT_FACTION_HOME = CONFIG.getString("DefaultFactionHome");
    public static final ChatColor NEUTRAL_FACTION_COLOR = ChatColor.valueOf(CONFIG.getString("NeutralFactionColor"));
    public static final ChatColor OWN_FACTION_COLOR = ChatColor.valueOf(CONFIG.getString("OwnFactionColor"));
    public static final ChatColor ENEMY_FACTION_COLOR = ChatColor.valueOf(CONFIG.getString("EnemyFactionColor"));
    public static final ChatColor ALLY_FACTION_COLOR = ChatColor.valueOf(CONFIG.getString("AllyFactionColor"));
    public static final ChatColor OWN_FACTION_COLOR_CHAT_CHAT_ACCENT = ChatColor.valueOf(CONFIG.getString("OwnFactionColorChatAccent"));
    public static final ChatColor ALLY_FACTION_COLOR_CHAT_ACCENT = ChatColor.valueOf(CONFIG.getString("AllyFactionColorChatAccent"));
    public static final int MAX_MEMBERS = CONFIG.getInt("FactionMaxMembers");
    public static final int INVITE_EXPIRE_TIME = CONFIG.getInt("FactionInviteExpireTime");
    public static final int MAX_PROXY_PLAYERS = CONFIG.getInt("MaxProxyPlayers");
    public static final String DATE_NOTATION = CONFIG.getString("DateNotation");
    public static final String FACTION_TAG = CONFIG.getString("FactionTag"); // brackets can only be removed or replaced with a single character!
    public static final int RELATION_EXPIRE_MINUTES = CONFIG.getInt("RelationExpireTime");
    public static final int POWER_LOST_ON_DEATH = CONFIG.getInt("PowerLostOnDeath");
    public static final double PAINBUILD_DAMAGE = CONFIG.getDouble("PainBuildDamage");
    public static final String WILDERNESS_NAME = CONFIG.getString("WildernessName");

    public static final String FACTION_ALREADY_STATE = CONFIG.getString("Messages.FactionAlreadyState");
    public static final String SETHOME_ONLY_ON_OWN_LAND = CONFIG.getString("Messages.SetHomeOnlyOnOwnLand");
    public static final String CLAIMED_RADIUS_OF_CHUNKS = CONFIG.getString("Messages.ClaimedRadiusOfChunks");
    public static final String CHANGED_SETTING = CONFIG.getString("Messages.ChangedSetting");
    public static final String NOW_IN_CHANNEL = CONFIG.getString("Messages.NowInChannel");
    public static final String ALREADY_IN_CHANNEL = CONFIG.getString("Messages.AlreadyInChannel");
    public static final String CHUNK_NOT_CLAIMED = CONFIG.getString("Messages.ChunkNotClaimed");
    public static final String NOT_ENOUGH_POWER = CONFIG.getString("Messages.NotEnoughPower");
    public static final String REVOKED_ACCESS_TO_PARTNER = CONFIG.getString("Messages.RevokedAccessToPartner");
    public static final String NOT_VALID_PARTNER = CONFIG.getString("Messages.NotValidPartner");
    public static final String LAND_CLAIMED = CONFIG.getString("Messages.LandClaimed");
    public static final String ADDED_PARTNER = CONFIG.getString("Messages.AddedPartner");
    public static final String CAN_ALREADY_BUILD = CONFIG.getString("Messages.CanAlreadyBuild");
    public static final String COOLDOWN_MESSAGE = CONFIG.getString("Messages.CooldownMessage");
    public static final String TOGGLED_SIDEBAR = CONFIG.getString("Messages.ToggledSidebar");
    public static final String INCOMING_NEUTRAL_REQUEST_CLICK = CONFIG.getString("Messages.IncomingNeutralRequestClick");
    public static final String NOW_NEUTRAL = CONFIG.getString("Messages.NowNeutral");
    public static final String INCOMING_NEUTRAL_REQUEST = CONFIG.getString("Messages.IncomingNeutralRequest");
    public static final String NEUTRAL_REQUEST_SENT = CONFIG.getString("Messages.NeutralRequestSent");
    public static final String ALREADY_NEUTRAL = CONFIG.getString("Messages.AlreadyNeutral");
    public static final String ALREADY_SENT_REQUEST_TO_FACTION = CONFIG.getString("Messages.AlreadySentRequestToFaction");
    public static final String RELATION_REQUEST_EXPIRED_TO = CONFIG.getString("Messages.RelationRequestExpiredToReciever");
    public static final String RELATION_REQUEST_EXPIRED = CONFIG.getString("Messages.RelationRequestExpiredToSender");
    public static final String INCOMING_ALLY_REQUEST = CONFIG.getString("Messages.IncomingAllyRequest");
    public static final String INCOMING_ALLY_REQUEST_CLICK = CONFIG.getString("Messages.IncomingAllyRequestClick");
    public static final String ALLY_REQUEST_SENT = CONFIG.getString("Messages.AllyRequestSent");
    public static final String ALREADY_ALLIES = CONFIG.getString("Messages.AlreadyAllies");
    public static final String NOW_ALLIES = CONFIG.getString("Messages.NowAllies");
    public static final String ALREADY_ENEMIES = CONFIG.getString("Messages.AlreadyEnemies");
    public static final String NOW_ENEMIES = CONFIG.getString("Messages.NowEnemies");
    public static final String FACTION_ALREADY_OWNS_LAND = CONFIG.getString("Messages.FactionAlreadyOwnsThisLand");
    public static final String AUTO_CLAIMING_ENABLED = CONFIG.getString("Messages.AutoClaimEnabled");
    public static final String AUTO_CLAIMING_DISABLED = CONFIG.getString("Messages.AutoClaimDisabled");
    public static final String CANNOT_JOIN_FULL = CONFIG.getString("Messages.CannotJoinFullFaction");
    public static final String CREATOR_RANK_CANNOT_KICK = CONFIG.getString("Messages.CreatorCannotKick");
    public static final String REMOVED_FROM_FACTION = CONFIG.getString("Messages.RemovedFromFaction");
    public static final String MEMBER_REMOVED = CONFIG.getString("Messages.MemberRemoved");
    public static final String PLAYER_ALREADY_GOT_INVITE = CONFIG.getString("Messages.PlayerAlreadyGotInvited");
    public static final String INVITE_EXPIRED_TO_SENDER = CONFIG.getString("Messages.InviteExpiredToSender");
    public static final String INVITE_EXPIRED_TO_RECIEVER = CONFIG.getString("Messages.InviteExpiredToReciever");
    public static final String INCOMING_INVITE = CONFIG.getString("Messages.IncomingInvite");
    public static final String CONFIRM_INVITE = CONFIG.getString("Messages.ConfirmInviteClick");
    public static final String INVITE_SENT = CONFIG.getString("Messages.InviteSent");
    public static final String CANNOT_CHANGE_OWN_RANK = CONFIG.getString("Messages.CannotChangeOwnRank");
    public static final String CREATOR_RANK_CANNOT_CHANGE = CONFIG.getString("Messages.CreatorRankCannotChange");
    public static final String ROLE_SET = CONFIG.getString("Messages.RoleSet");
    public static final String REMOVED_HOME = CONFIG.getString("Messages.RemovedHome");
    public static final String RELOCATED_HOME = CONFIG.getString("Messages.RelocatedHome");
    public static final String RE_HOME_MESSAGE = CONFIG.getString("Messages.RelocateHomeClick");
    public static final String UPDATED_FACTION_OPEN = CONFIG.getString("Messages.UpdatedFactionOpen");
    public static final String FACTION_NOW_OPEN = CONFIG.getString("Messages.FactionNowOpen");
    public static final String LEFT_FACTION_MESSAGE = CONFIG.getString("Messages.LeftFactionMessage");
    public static final String LEFT_FACTION = CONFIG.getString("Messages.LeftFaction");
    public static final String LEAVE_EQUALS_DISBAND = CONFIG.getString("Messages.LeaveEqualsDisband");
    public static final String LEAVE_EQUALS_DISBAND_CLICK_HERE = CONFIG.getString("Messages.LeaveEqualsDisbandClick");
    public static final String PLAYER_JOINED_FACTION = CONFIG.getString("Messages.PlayerJoinedFaction");
    public static final String FACTION_NOT_OPEN = CONFIG.getString("Messages.FactionNotOpen");
    public static final String POWER_MESSAGE = CONFIG.getString("Messages.PowerMessage");
    public static final String FACTION_NOT_EXISTS = CONFIG.getString("Messages.FactionDoesNotExist");
    public static final String TELEPORTING_TO_HOME = CONFIG.getString("Messages.TeleportingToHome");
    public static final String MOVED_NO_TELEPORT = CONFIG.getString("Messages.MovedNoTp");
    public static final String HOME_DOES_NOT_EXIST = CONFIG.getString("Messages.HomeDoesNotExist");
    public static final String CREATED_HOME = CONFIG.getString("Messages.CreatedHome");
    public static final String HOME_ALREADY_EXISTS = CONFIG.getString("Messages.HomeAlreadyExists");
    public static final String FACTION_DISBAND = CONFIG.getString("Messages.FactionDisbanded");
    public static final String CLAIMED_LINE_OF_CHUNKS = CONFIG.getString("Messages.ClaimedLineOfChunks");
    public static final String NOW_IN = CONFIG.getString("Messages.NowInMessage");
    public static final String ENTERING_LAND = CONFIG.getString("Messages.EnteringLand");
    public static final String CHUNK_ALREADY_CLAIMED = CONFIG.getString("Messages.ChunkAlreadyClaimed");
    public static final String CLAIMED_CHUNK = CONFIG.getString("Messages.ClaimedChunk");
    public static final String FACTION_ALREADY_EXISTS = CONFIG.getString("Messages.FactionNameAlreadyInUse");
    public static final String FACTION_NAME_TOO_LONG = CONFIG.getString("Messages.FactionNameTooLong");
    public static final String SUCCESS_FULLY_RESET_SYSTEM = "&aSystem has been reset successfully.";
    public static final String SYSTEM_RESET_KICK = CONFIG.getString("Messages.FactionResetKickMessage");
    public static final String FATAL_LOAD_KICK = CONFIG.getString("Messages.FatalLoadKick");
    public static final String COMMAND_USAGE_MESSAGE = CONFIG.getString("Messages.CommandUsage");
    public static final String INVALID_COMMAND_USAGE = CONFIG.getString("Messages.InvalidCommandUsage");
    public static final String PLAYER_ONLY_COMMAND = CONFIG.getString("Messages.PlayerOnlyCommand");
    public static final String ALREADY_IN_FACTION = CONFIG.getString("Messages.AlreadyInFaction");
    public static final String NOT_IN_FACTION = CONFIG.getString("Messages.NotInFaction");
    public static final String LOADING_KICK_MESSAGE = CONFIG.getString("Messages.StillLoadingKickMessage");
    public static final String CREATED_FACTION_BROADCAST = CONFIG.getString("Messages.CreatedFactionBroadcast");
    public static final String NO_PERMISSION_COMMAND = CONFIG.getString("Messages.NoPermissionForCommand");
    public static final String NO_ROLE_PERMISSION_COMMAND = CONFIG.getString("Messages.NoRolePermissionForCommand");
}
