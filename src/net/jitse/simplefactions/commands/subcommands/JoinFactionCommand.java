package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.Faction;
import net.jitse.simplefactions.factions.Member;
import net.jitse.simplefactions.factions.Role;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Timestamp;

/**
 * Created by Jitse on 8-7-2017.
 */
public class JoinFactionCommand extends SubCommand {

    public JoinFactionCommand(Role role) {
        super(role, "simplefactions.commands.join");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Chat.format(Settings.PLAYER_ONLY_COMMAND));
            return;
        }
        Player player = (Player) sender;
        Faction faction = SimpleFactions.getInstance().getFactionsManager().getFaction(player);
        net.jitse.simplefactions.factions.Player fplayer = SimpleFactions.getInstance().getFactionsManager().getFactionsPlayer(player);
        if(faction != null){
            player.sendMessage(Chat.format(Settings.ALREADY_IN_FACTION));
            return;
        }
        if(args.length != 2){
            player.sendMessage(Chat.format(Settings.COMMAND_USAGE_MESSAGE.replace("{syntax}", "/faction join <name>")));
            return;
        }
        Faction toJoin = SimpleFactions.getInstance().getFactionsManager().getFaction(args[1]);
        if(!toJoin.isOpen() && (!InviteCommand.getPending().containsKey(fplayer) || !InviteCommand.getPending().get(fplayer).equals(toJoin))){
            player.sendMessage(Chat.format(Settings.FACTION_NOT_OPEN));
            return;
        }
        if(InviteCommand.getPending().containsKey(fplayer)) InviteCommand.getPending().remove(fplayer);
        toJoin.addMember(new Member(player.getUniqueId(), new Timestamp(System.currentTimeMillis()), Role.MEMBER, SimpleFactions.getInstance().getFactionsManager().getFactionsPlayer(player)), true, true);
        toJoin.getMembers().stream().filter(member -> member.getBukkitOfflinePlayer().isOnline()).forEach(online -> online.getBukkitPlayer().sendMessage(Chat.format(Settings.PLAYER_JOINED_FACTION.replace("{ownfactioncolor}", Settings.OWN_FACTION_COLOR.toString()).replace("{player}", player.getName()).replace("{faction}", toJoin.getName()))));
    }
}
