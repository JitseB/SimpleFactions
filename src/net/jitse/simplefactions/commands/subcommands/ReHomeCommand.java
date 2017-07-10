package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.Faction;
import net.jitse.simplefactions.factions.Home;
import net.jitse.simplefactions.factions.Role;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import net.jitse.simplefactions.utilities.LocationSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Created by Jitse on 10-7-2017.
 */
public class ReHomeCommand extends SubCommand {

    public ReHomeCommand(Role role){
        super(role, "simplefactions.commands.rehome");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Chat.format(Settings.PLAYER_ONLY_COMMAND));
            return;
        }
        Player player = (Player) sender;
        Faction faction = SimpleFactions.getInstance().getFactionsManager().getFaction(player);
        String homeName = args.length == 2 ? args[1] : Settings.DEFAULT_FACTION_HOME;
        Optional<Home> homeOptional = faction.getHomes().stream().filter(home -> home.getName().equalsIgnoreCase(homeName)).findFirst();
        if(!homeOptional.isPresent()){
            player.sendMessage(Chat.format(Settings.HOME_DOES_NOT_EXIST.replace("{home}", homeName)));
            return;
        }
        Home home = homeOptional.get();
        home.setLocation(player.getLocation());
        SimpleFactions.getInstance().getMySql().execute("UPDATE FactionHomes SET location=? WHERE faction=? AND name=?;", LocationSerializer.serialize(home.getLocation()), faction.getName(), home.getName());
        player.sendMessage(Chat.format(Settings.RELOCATED_HOME.replace("{home}", home.getName())));
    }
}
