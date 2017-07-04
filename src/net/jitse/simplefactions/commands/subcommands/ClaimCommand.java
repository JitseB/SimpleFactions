package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.Faction;
import net.jitse.simplefactions.factions.Role;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Jitse on 30-6-2017.
 */
public class ClaimCommand extends SubCommand {

    public ClaimCommand(Role role){
        super(role, "simplefactions.command.claim");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Chat.format(Settings.PLAYER_ONLY_COMMAND));
            return;
        }
        Player player = (Player) sender;
        Faction faction = SimpleFactions.getInstance().getFactionsManager().getFaction(player);
        if(faction == null){
            player.sendMessage(Chat.format(Settings.COMMAND_PREFIX + Settings.NOT_IN_FACTION));
            return;
        }

        // TODO : Add line claim command with limit

        Chunk chunk = player.getLocation().getChunk();
        Faction chunkFaction = SimpleFactions.getInstance().getFactionsManager().getFaction(chunk);
        if(chunkFaction != null){
            player.sendMessage(Chat.format(Settings.CHUNK_ALREADY_CLAIMED.replace("{faction}", chunkFaction.getName())));
            return;
        }

        faction.claimChunk(true, chunk);
        player.sendMessage(Chat.format(Settings.COMMAND_PREFIX + Settings.CLAIMED_CHUNK));
    }
}
