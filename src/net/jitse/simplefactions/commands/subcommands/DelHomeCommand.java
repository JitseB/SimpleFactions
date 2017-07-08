package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.Role;
import org.bukkit.command.CommandSender;

/**
 * Created by Jitse on 8-7-2017.
 */
public class DelHomeCommand extends SubCommand {

    public DelHomeCommand(Role role){
        super(role, "simplefactions.commands.delhome");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        sender.sendMessage("Todo");
    }
}
