package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.Faction;
import net.jitse.simplefactions.factions.Home;
import net.jitse.simplefactions.factions.Role;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import net.jitse.simplefactions.utilities.DelayTeleport;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Created by Jitse on 8-7-2017.
 */
public class HomeCommand extends SubCommand {

    public HomeCommand(Role role){
        super(role, "simplefactions.command.home");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
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
        String homeName = args.length == 2 ? args[1] : Settings.DEFAULT_FACTION_HOME;
        Optional<Home> homeOptional = faction.getHomes().stream().filter(home -> home.getName().equalsIgnoreCase(homeName)).findAny();
        if(!homeOptional.isPresent()){
            player.sendMessage(Chat.format(Settings.HOME_DOES_NOT_EXIST.replace("{home}", homeName)));
            return;
        }
        player.sendMessage(Chat.format(Settings.TELEPORTING_TO_HOME.replace("{home}", homeOptional.get().getName()).replace("{time}", String.valueOf(Settings.TP_DELAY))));
        DelayTeleport.teleport(player, homeOptional.get().getLocation());
    }
}
