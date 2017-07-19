package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.Faction;
import net.jitse.simplefactions.factions.Home;
import net.jitse.simplefactions.factions.Role;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Created by Jitse on 25-6-2017.
 */
public class SetHomeCommand extends SubCommand {

    public SetHomeCommand(Role role){
        super(role, "simplefactions.command.sethome");
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
        if(!faction.hasClaimedChunk(player.getLocation().getChunk())){
            player.sendMessage(Chat.format(Settings.SETHOME_ONLY_ON_OWN_LAND));
            return;
        }
        String homeName = args.length == 2 ? args[1] : Settings.DEFAULT_FACTION_HOME;
        Optional<Home> homeOptional = faction.getHomes().stream().filter(home -> home.getName().equalsIgnoreCase(homeName)).findFirst();
        if(homeOptional.isPresent()){
            player.sendMessage(Chat.format(Settings.HOME_ALREADY_EXISTS.replace("{home}", homeOptional.get().getName())));
            TextComponent message = new TextComponent(Settings.RE_HOME_MESSAGE);
            message.setBold(true);
            message.setColor(ChatColor.RED);
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{ new TextComponent("Click to relocated " + homeName) }));
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/faction rehome " + homeName));
            player.spigot().sendMessage(message);
            return;
        }
        faction.addHome(new Home(homeName, player.getLocation()), true);
        player.sendMessage(Chat.format(Settings.CREATED_HOME.replace("{home}", homeName)));
    }
}
