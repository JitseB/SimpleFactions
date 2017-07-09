package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.Member;
import net.jitse.simplefactions.factions.Role;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
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
    public void onExecute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Chat.format(Settings.PLAYER_ONLY_COMMAND));
            return;
        }
        Player player = (Player) sender;
        if(args.length != 3){
            player.sendMessage(Chat.format(Settings.COMMAND_USAGE_MESSAGE.replace("{syntax}", "/faction role <user> <role: member/mod/owner>")));
            return;
        }
        Role role = Stream.of(Role.values()).filter(all -> all.toString().equals(args[1].toUpperCase())).findFirst().orElse(null);
        Member target = SimpleFactions.getInstance().getFactionsManager().getMember(player);
        if(role == null || target == null || target.getFaction() != SimpleFactions.getInstance().getFactionsManager().getFaction(player)){
            player.sendMessage(Chat.format(Settings.INVALID_COMMAND_USAGE));
            return;
        }
        // todo
    }
}
