package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.Faction;
import net.jitse.simplefactions.factions.Member;
import net.jitse.simplefactions.factions.Role;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.stream.Stream;

/**
 * Created by Jitse on 9-7-2017.
 */
public class RoleCommand extends SubCommand {

    public RoleCommand(Role role){
        super(role, "simplefactions.commands.role");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Chat.format(Settings.PLAYER_ONLY_COMMAND));
            return;
        }
        Player player = (Player) sender;
        Member member = SimpleFactions.getInstance().getFactionsManager().getMember(player);
        if(args.length != 3){
            player.sendMessage(Chat.format(Settings.COMMAND_USAGE_MESSAGE.replace("{syntax}", "/faction role <player> <role: member/mod/owner> (Player must be online)")));
            return;
        }
        Role role = Stream.of(Role.values()).filter(all -> all.toString().equals(args[2].toUpperCase())).findFirst().orElse(null);
        Player targetPlayer = Bukkit.getPlayer(args[1]);
        if(role == null || targetPlayer == null){
            player.sendMessage(Chat.format(Settings.INVALID_COMMAND_USAGE));
            return;
        }
        Member target = SimpleFactions.getInstance().getFactionsManager().getMember(targetPlayer);
        if(target == null || target.getFaction() != SimpleFactions.getInstance().getFactionsManager().getFaction(player)){
            player.sendMessage(Chat.format(Settings.INVALID_COMMAND_USAGE));
            return;
        }
        if(target.getBukkitPlayer() == player){
            player.sendMessage(Chat.format(Settings.CANNOT_CHANGE_OWN_RANK));
            return;
        }
        Faction faction = SimpleFactions.getInstance().getFactionsManager().getFaction(player);
        if(target.getUUID().equals(faction.getCreator())){
            player.sendMessage(Chat.format(Settings.CREATOR_RANK_CANNOT_CHANGE));
            return;
        }
        target.setRole(role);
        faction.getMembers().stream()
                .filter(fmember -> fmember.getBukkitOfflinePlayer().isOnline())
                .forEach(online -> online.getBukkitPlayer().sendMessage(Chat.format(Settings.ROLE_SET
                        .replace("{player}", ("[" + member.getRole().toString() + "] " + player.getName()))
                        .replace("{target}", ("[" + target.getRole() + "] " + target.getBukkitPlayer().getName())))
                ));
    }
}
