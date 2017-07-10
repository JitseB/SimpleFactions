package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.Role;
import org.bukkit.command.CommandSender;

/**
 * Created by Jitse on 10-7-2017.
 */
public class AutoClaimCommand extends SubCommand {

    public AutoClaimCommand(Role role){
        super(role, "simplefactions.commands.autoclaim");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {

    }
}
