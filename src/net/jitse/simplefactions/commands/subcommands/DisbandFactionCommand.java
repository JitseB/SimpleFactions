package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.Role;
import org.bukkit.command.CommandSender;

/**
 * Created by Jitse on 25-6-2017.
 */
public class DisbandFactionCommand extends SubCommand {

    public DisbandFactionCommand(Role role){
        super(role);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {

    }
}
