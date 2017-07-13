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

import java.util.Optional;

/**
 * Created by Jitse on 10-7-2017.
 */
public class KickMemberCommand extends SubCommand {

    public KickMemberCommand(Role role){
        super(role, "simplefactions.commands.kick");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Chat.format(Settings.PLAYER_ONLY_COMMAND));
            return;
        }
        Player player = (Player) sender;
        Faction faction = SimpleFactions.getInstance().getFactionsManager().getFaction(player);
        if(args.length != 2){
            player.sendMessage(Chat.format(Settings.COMMAND_USAGE_MESSAGE.replace("{syntax}", "/faction kick <player>")));
            return;
        }
        Optional<Member> memberOptional = faction.getMembers().stream().filter(member -> member.getBukkitOfflinePlayer().getName().equalsIgnoreCase(args[1])).findFirst();
        if(!memberOptional.isPresent() || memberOptional.get().getUUID().equals(player.getUniqueId())){
            player.sendMessage(Chat.format(Settings.INVALID_COMMAND_USAGE));
            return;
        }
        if(memberOptional.get().getUUID().equals(faction.getCreator())){
            player.sendMessage(Chat.format(Settings.CREATOR_RANK_CANNOT_KICK));
            return;
        }
        player.sendMessage(Chat.format(Settings.MEMBER_REMOVED.replace("{target}", memberOptional.get().getBukkitOfflinePlayer().getName()).replace("{faction}", faction.getName())));
        if(memberOptional.get().getBukkitOfflinePlayer().isOnline()){
            memberOptional.get().getBukkitPlayer().sendMessage(Chat.format(Settings.REMOVED_FROM_FACTION.replace("{faction}", faction.getName())));
            SimpleFactions.getInstance().getFactionsTagManager().removeTag(memberOptional.get());
            SimpleFactions.getInstance().getFactionsTagManager().resetTags(SimpleFactions.getInstance().getFactionsManager().getFactionsPlayer(memberOptional.get().getBukkitPlayer()));
            SimpleFactions.getInstance().getFactionsTagManager().initTags(SimpleFactions.getInstance().getFactionsManager().getFactionsPlayer(memberOptional.get().getBukkitPlayer()));
        }
        SimpleFactions.getInstance().getFactionsManager().getFactionsPlayer(memberOptional.get().getBukkitPlayer()).setPower(memberOptional.get().getPower());
        faction.getMembers().remove(memberOptional.get());
    }
}
