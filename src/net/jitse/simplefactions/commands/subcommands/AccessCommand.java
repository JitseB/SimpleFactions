package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.Role;
import org.bukkit.command.CommandSender;

/**
 * Created by Jitse on 13-7-2017.
 */
public class AccessCommand extends SubCommand {

    public AccessCommand(Role role){
        super(role, "simplefactions.commands.access");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {

    }
}
