package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.Role;
import org.bukkit.command.CommandSender;

/**
 * Created by Jitse on 12-7-2017.
 */
public class FactionTopCommand extends SubCommand {

    public FactionTopCommand(Role role){
        super(role, "simplefactions.commands.factiontop");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        // todo with vault
    }
}
