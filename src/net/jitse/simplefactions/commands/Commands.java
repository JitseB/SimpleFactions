package net.jitse.simplefactions.commands;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.commands.subcommands.*;
import net.jitse.simplefactions.factions.Member;
import net.jitse.simplefactions.factions.Role;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Jitse on 25-6-2017.
 */
public enum Commands {

    // Commands which require to be in a faction.
    LEAVE_FACTION(new LeaveFactionCommand(Role.MEMBER), true),
    DISBAND_FACTION(new DisbandFactionCommand(Role.OWNER), true),
    SET_HOME(new SetHomeCommand(Role.MEMBER), true),
    HOME(new HomeCommand(Role.MEMBER), true),
    DELETE_HOME(new DelHomeCommand(Role.MOD), true),
    INVITE(new InviteCommand(Role.MOD), true),
    CLAIM_LAND(new ClaimCommand(Role.MOD), true),
    ACCESS(new AccessCommand(Role.MOD), true),
    KICK_MEMBER(new KickMemberCommand(Role.MOD), true),
    ENEMY(new EnemyCommand(Role.OWNER), true),
    REVOKE(new RevokeCommand(Role.MOD), true),
    NEUTRAL(new NeutralCommand(Role.OWNER), true),
    ALLY(new AllyCommand(Role.OWNER), true),
    FLY(new FlyCommand(Role.MEMBER), true),
    RE_HOME(new ReHomeCommand(Role.MOD), true),
    OPEN(new OpenCommand(Role.MOD), true),
    ROLE(new RoleCommand(Role.OWNER), true),
    AUTO_CLAIM(new AutoClaimCommand(Role.MOD), true),
    CHAT(new ChatCommand(Role.MEMBER), true),
    PERM(new PermCommand(Role.OWNER), true),

    // Commands which don't require to be in a faction.
    CREATE_FACTION(new CreateFactionCommand(), false),
    JOIN(new JoinFactionCommand(), false),
    FACTION_TOP(new FactionTopCommand(), false),
    FACTION_MAP(new FactionMapCommand(), false),
    SHOW(new ShowCommand(), false),
    SIDEBAR_TOGGLE(new SidebarToggleCommand(), false),
    POWER(new PowerCommand(), false),
    RESET_SYSTEM(new ResetCommand(), false);

    private SubCommand subCommand;
    private boolean factionNeeded;

    Commands(SubCommand subCommand, boolean factionNeeded){
        this.subCommand = subCommand;
        this.factionNeeded = factionNeeded;
    }

    public SubCommand getSubCommand(){
        return this.subCommand;
    }

    public void execute(CommandSender sender, String[] args){
        if(!(sender instanceof Player)) this.subCommand.perform(sender, args);
        else{
            Player player = (Player) sender;
            if(!player.hasPermission(this.subCommand.getPermission())){
                player.sendMessage(Chat.format(Settings.NO_PERMISSION_COMMAND.replace("{permission}", this.subCommand.getPermission())));
                return;
            }
            Member member = SimpleFactions.getInstance().getFactionsManager().getMember(player);
            if(member == null && factionNeeded){
                player.sendMessage(Chat.format(Settings.NOT_IN_FACTION));
                return;
            }
            if((member == null || member.getRole().ordinal() < this.subCommand.getRole().ordinal()) && factionNeeded){
                player.sendMessage(Chat.format(Settings.NO_ROLE_PERMISSION_COMMAND.replace("{role}", this.subCommand.getRole().toString().toLowerCase())));
                return;
            } else this.subCommand.perform(sender, args);
        }
    }
}
