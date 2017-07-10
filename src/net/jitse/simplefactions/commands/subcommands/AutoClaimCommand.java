package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.events.PlayerChangeChunkEvent;
import net.jitse.simplefactions.events.PlayerLeaveServerEvent;
import net.jitse.simplefactions.factions.Role;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Jitse on 10-7-2017.
 */
public class AutoClaimCommand extends SubCommand implements Listener {

    private static Set<UUID> claiming = new HashSet<>();

    public AutoClaimCommand(Role role){
        super(role, "simplefactions.commands.autoclaim");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Chat.format(Settings.PLAYER_ONLY_COMMAND));
            return;
        }
        Player player = (Player) sender;
        if(claiming.contains(player.getUniqueId())){
            player.sendMessage(Chat.format(Settings.AUTO_CLAIMING_DISABLED));
            claiming.remove(player.getUniqueId());
        } else{
            player.sendMessage(Chat.format(Settings.AUTO_CLAIMING_ENABLED));
            claiming.add(player.getUniqueId());
        }
    }

    public static Set<UUID> getClaiming(){
        return claiming;
    }

    @EventHandler
    public void onServerLeave(PlayerLeaveServerEvent event){
        if(claiming.contains(event.getPlayer().getUniqueId()))
            claiming.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onChunkMove(PlayerChangeChunkEvent event){
        if(claiming.contains(event.getPlayer().getUniqueId()))
            event.getPlayer().performCommand("faction claim");
    }
}
