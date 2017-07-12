package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.Role;
import org.bukkit.command.CommandSender;

/**
 * Created by Jitse on 12-7-2017.
 */
public class FlyCommand extends SubCommand {

    public FlyCommand(Role role){
        super(role, "simplefactions.commands.fly");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {

    }
}
