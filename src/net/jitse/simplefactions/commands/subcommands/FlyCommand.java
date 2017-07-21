package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.*;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import net.jitse.simplefactions.utilities.RelationState;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Jitse on 12-7-2017.
 */
public class FlyCommand extends SubCommand {

    public FlyCommand(){
        super(Role.MEMBER, "simplefactions.commands.fly");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Chat.format(Settings.PLAYER_ONLY_COMMAND));
            return;
        }
        Player player = (Player) sender;
        Chunk currentChunk = player.getLocation().getChunk();
        Faction playerFaction = SimpleFactions.getInstance().getFactionsManager().getFaction(player);
        Faction faction = SimpleFactions.getInstance().getFactionsManager().getFaction(currentChunk);
        if(faction == null){
            player.sendMessage(Chat.format(Settings.FLY_ONLY_ON_TRUSTED_LAND));
            return;
        }
        boolean allowedFlight = false;
        if(playerFaction != null && faction.equals(playerFaction)){
            Role role = SimpleFactions.getInstance().getFactionsManager().getMember(player).getRole();
            switch (role){
                case MEMBER:
                    if(faction.getSetting(PermCategory.MEM, PermSetting.FLY))
                        allowedFlight = true;
                    break;
                case MOD:
                    if(faction.getSetting(PermCategory.MOD, PermSetting.FLY))
                        allowedFlight = true;
                    break;
                case OWNER:
                    allowedFlight = true;
                    break;
            }
        } else{
            if(playerFaction == null && faction.getSetting(PermCategory.NEU, PermSetting.FLY)){
                allowedFlight = true;
            } else{
                RelationState relation = faction.getAllies().contains(playerFaction) ? RelationState.ALLIES : (faction.getEnemies().contains(playerFaction) ? RelationState.ENEMIES : null);
                if(relation == null){
                    if(relation == null && faction.getSetting(PermCategory.NEU, PermSetting.FLY))
                        allowedFlight = true;
                } else{
                    switch (relation){
                        case ALLIES:
                            if(relation == null && faction.getSetting(PermCategory.ALL, PermSetting.FLY))
                                allowedFlight = true;
                            break;
                        case ENEMIES:
                            if(relation == null && faction.getSetting(PermCategory.ENE, PermSetting.FLY))
                                allowedFlight = true;
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        if(!allowedFlight && playerFaction != null){
            boolean partners = false;
            for(Partner partner : faction.getPartners(currentChunk)){
                if(partner.getData() instanceof Faction && partner.getData().equals(playerFaction))
                    partners = true;
                if(partner.getData() instanceof UUID && partner.getData().equals(player.getUniqueId()))
                    partners = true;
            }
            if(partners && faction.getSetting(PermCategory.MEM, PermSetting.FLY))
                allowedFlight = true;
        }
        if(!allowedFlight){
            player.sendMessage(Chat.format(Settings.FLY_ONLY_ON_TRUSTED_LAND));
            return;
        }
        // Player is allowed on this chunk to fly.
        player.setAllowFlight(!player.getAllowFlight()); // Toggle flight.
        if(player.getAllowFlight()) player.sendMessage(Chat.format(Settings.ENABLED_FLIGHT));
        else player.sendMessage(Chat.format(Settings.DISABLED_FLIGHT));
    }
}
