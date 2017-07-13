package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.Faction;
import net.jitse.simplefactions.factions.Role;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by Jitse on 13-7-2017.
 */
public class FactionMapCommand extends SubCommand {

    public FactionMapCommand(Role role){
        super(role, "simplefactions.commands.factionmap");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Chat.format(Settings.PLAYER_ONLY_COMMAND));
            return;
        }
        Player player = (Player) sender;
        Faction faction = SimpleFactions.getInstance().getFactionsManager().getFaction(player);

        Chat.centeredMessage(sender, Chat.format("&8-----     &5&lMap&r&5 (Hover to view faction names):     &8-----"));

        Map<Faction, String> symbols = new HashMap<>();
        Queue<String> availableSymbols = new LinkedList<>();
        availableSymbols.add("#");
        availableSymbols.add("%");
        availableSymbols.add("@");
        availableSymbols.add("$");

        List<TextComponent> list = new ArrayList<>();
        for (int i = -4; i <= 4; i++) {
            TextComponent line = new TextComponent();
            for (int j = -15; j <= 15; j++) {
                if(i == 0 && j == 0){
                    TextComponent component = new TextComponent("+");
                    component.setColor(net.md_5.bungee.api.ChatColor.DARK_PURPLE);
                    TextComponent you = new TextComponent("Your location");
                    you.setColor(net.md_5.bungee.api.ChatColor.DARK_PURPLE);
                    component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] { you }));
                    line.addExtra(component);
                    continue;
                }
                Chunk current = player.getLocation().getChunk();
                Chunk check = player.getWorld().getChunkAt(current.getX() + i, current.getZ() + j);
                Faction fcheck = SimpleFactions.getInstance().getFactionsManager().getFaction(check);
                net.md_5.bungee.api.ChatColor color = Settings.NEUTRAL_FACTION_COLOR.asBungee();
                if(faction != null){
                    if(faction == fcheck) color = Settings.OWN_FACTION_COLOR.asBungee();
                    else if(faction.getEnemies().contains(fcheck)) color = Settings.ENEMY_FACTION_COLOR.asBungee();
                    else if(faction.getAllies().contains(fcheck)) color = Settings.ALLY_FACTION_COLOR.asBungee();
                }
                if(fcheck != null){
                    if(symbols.containsKey(fcheck)){
                        TextComponent component = new TextComponent(symbols.get(fcheck));
                        component.setColor(color);
                        TextComponent fname = new TextComponent(fcheck.getName());
                        fname.setColor(color);
                        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] { fname }));
                        line.addExtra(component);
                    } else{
                        if(availableSymbols.peek() == null) {
                            TextComponent component = new TextComponent("+");
                            component.setColor(color);
                            TextComponent fname = new TextComponent(fcheck.getName());
                            fname.setColor(color);
                            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] { fname }));
                            line.addExtra(component);
                        }
                        else{
                            String symbol = availableSymbols.poll();
                            symbols.put(fcheck, symbol);
                            TextComponent component = new TextComponent(symbol);
                            component.setColor(color);
                            TextComponent fname = new TextComponent(fcheck.getName());
                            fname.setColor(color);
                            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] { fname }));
                            line.addExtra(component);
                        }
                    }
                }
                else {
                    TextComponent nothing = new TextComponent("+");
                    nothing.setColor(net.md_5.bungee.api.ChatColor.GRAY);
                    line.addExtra(nothing);
                }
            }
            list.add(line);
        }
        for(TextComponent line : list) {
            TextComponent spaced = new TextComponent("                ");
            spaced.addExtra(line);
            player.spigot().sendMessage(spaced);
        }
    }
}
