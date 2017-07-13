package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.Faction;
import net.jitse.simplefactions.factions.Partner;
import net.jitse.simplefactions.factions.Role;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Jitse on 13-7-2017.
 */
public class RevokeCommand extends SubCommand {

    public RevokeCommand(Role role){
        super(role, "simplefactions.commands.revoke");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Chat.format(Settings.PLAYER_ONLY_COMMAND));
            return;
        }
        Player player = (Player) sender;
        Faction faction = SimpleFactions.getInstance().getFactionsManager().getFaction(player);
        if(args.length != 3){
            player.sendMessage(Chat.format(Settings.COMMAND_USAGE_MESSAGE.replace("{syntax}", "/faction revoke <player | faction> <name>")));
            return;
        }
        Chunk chunk = player.getLocation().getChunk();
        if(args[1].equalsIgnoreCase("player")){
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[2]);
            if(target == null){
                player.sendMessage(Chat.format(Settings.INVALID_COMMAND_USAGE));
                return;
            }
            Partner requested = null;
            for(Partner partner : faction.getPartners(chunk)){
                if(!(partner.getData() instanceof UUID)) continue;
                if(partner.getData().equals(target.getUniqueId())){
                    requested = partner;
                    break;
                }
            }
            if(requested == null){
                player.sendMessage(Chat.format(Settings.NOT_VALID_PARTNER));
                return;
            }
            faction.removePartner(requested, true);
            player.sendMessage(Chat.format(Settings.REVOKED_ACCESS_TO_PARTNER.replace("{partner}", target.getName())));
        }
        else if(args[1].equalsIgnoreCase("faction")){
            Faction target = SimpleFactions.getInstance().getFactionsManager().getFaction(args[2]);
            if(target == null){
                player.sendMessage(Chat.format(Settings.INVALID_COMMAND_USAGE));
                return;
            }
            Partner requested = null;
            for(Partner partner : faction.getPartners(chunk)){
                if(!(partner.getData() instanceof Faction)) continue;
                if(((Faction) partner.getData()).getName().equals(target.getName())){
                    requested = partner;
                    break;
                }
            }
            if(requested == null){
                player.sendMessage(Chat.format(Settings.NOT_VALID_PARTNER));
                return;
            }
            faction.removePartner(requested, true);
            player.sendMessage(Chat.format(Settings.REVOKED_ACCESS_TO_PARTNER.replace("{partner}", target.getName())));
        }
        else player.sendMessage(Chat.format(Settings.INVALID_COMMAND_USAGE));
    }
}
