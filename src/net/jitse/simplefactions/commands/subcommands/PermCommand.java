package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.Role;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Jitse on 15-7-2017.
 */
public class PermCommand extends SubCommand {

    public PermCommand(Role role){
        super(role, "simplfactions.commands.perm");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Chat.format(Settings.PLAYER_ONLY_COMMAND));
            return;
        }
        Player player = (Player) sender;
    }

    enum PermCategory {

    }
}
