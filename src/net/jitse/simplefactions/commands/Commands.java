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

    CREATE_FACTION(new CreateFactionCommand(Role.MEMBER), false),
    LEAVE_FACTION(new LeaveFactionCommand(Role.MEMBER), true),
    DISBAND_FACTION(new DisbandFactionCommand(Role.OWNER), true),
    SET_HOME(new SetHomeCommand(Role.MEMBER), true),
    HOME(new HomeCommand(Role.MEMBER), true),
    DELETE_HOME(new DelHomeCommand(Role.MOD), true),
    INVITE(new InviteCommand(Role.MOD), true),
    CLAIM_LAND(new ClaimCommand(Role.MOD), true),
    JOIN(new JoinFactionCommand(Role.MEMBER), false),
    FACTION_TOP(new FactionTopCommand(Role.MEMBER), false), // todo
    ACCESS(new AccessCommand(Role.MOD), true), // todo
    KICK_MEMBER(new KickMemberCommand(Role.MOD), true),
    ENEMY(new EnemyCommand(Role.OWNER), true),
    SIDEBAR_TOGGLE(new SidebarToggleCommand(Role.MEMBER), false),
    NEUTRAL(new NeutralCommand(Role.OWNER), true), // todo
    ALLY(new AllyCommand(Role.OWNER), true), // todo
    SHOW(new ShowCommand(Role.MEMBER), false),
    FLY(new FlyCommand(Role.MEMBER), true), // todo
    RE_HOME(new ReHomeCommand(Role.MOD), true),
    OPEN(new OpenCommand(Role.MOD), true),
    ROLE(new RoleCommand(Role.OWNER), true),
    AUTO_CLAIM(new AutoClaimCommand(Role.MOD), true),
    POWER(new PowerCommand(Role.MEMBER), false),
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
        if(!(sender instanceof Player)) this.subCommand.onExecute(sender, args);
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
            } else this.subCommand.onExecute(sender, args);
        }
    }
}
