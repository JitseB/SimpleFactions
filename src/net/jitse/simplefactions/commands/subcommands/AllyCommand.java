package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.Faction;
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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jitse on 12-7-2017.
 */
public class AllyCommand extends SubCommand {

    // Requests are saved in this pending Map<TO, FROM>.
    private Map<Faction, Faction> pending = new HashMap<>();

    public AllyCommand(Role role){
        super(role, "simplefactions.commands.ally");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Chat.format(Settings.PLAYER_ONLY_COMMAND));
            return;
        }
        Player player = (Player) sender;
        Faction faction = SimpleFactions.getInstance().getFactionsManager().getFaction(player);
        if(args.length != 2){
            player.sendMessage(Chat.format(Settings.COMMAND_USAGE_MESSAGE.replace("{syntax}", "/faction enemy <faction>")));
            return;
        }
        Faction target = SimpleFactions.getInstance().getFactionsManager().getFaction(args[1]);
        if(target == null){
            player.sendMessage(Chat.format(Settings.FACTION_NOT_EXISTS.replace("{faction}", args[1])));
        }
        if(target.equals(faction)){
            player.sendMessage(Chat.format(Settings.INVALID_COMMAND_USAGE));
            return;
        }
        if(faction.getAllies().contains(target)){
            player.sendMessage(Chat.format(Settings.ALREADY_ALLIES.replace("{ally}", target.getName())));
            return;
        }
        for(Map.Entry<Faction, Faction> entry : pending.entrySet()){
            if(entry.getValue().equals(faction) && entry.getKey().equals(target)){
                // Abort request, already sent one.
                player.sendMessage(Chat.format(Settings.ALREADY_SENT_REQUEST_TO_FACTION.replace("{type}", "allies").replace("{target}", target.getName())));
                return;
            }
        }
        if(pending.containsKey(faction) && pending.get(faction).equals(target)){
            // Accept the request
            if(faction.getEnemies().contains(target)) faction.setNeutral(target, true);
            if(target.getEnemies().contains(faction)) target.setNeutral(faction, true);
            faction.setAllies(target, true);
            target.setAllies(faction, true);
            faction.getMembers().stream().filter(member -> member.getBukkitOfflinePlayer().isOnline()).forEach(online -> online.getBukkitPlayer().sendMessage(Chat.format(Settings.NOW_ALLIES.replace("{ally}", target.getName()))));
            target.getMembers().stream().filter(member -> member.getBukkitOfflinePlayer().isOnline()).forEach(online -> online.getBukkitPlayer().sendMessage(Chat.format(Settings.NOW_ALLIES.replace("{ally}", faction.getName()))));
            pending.remove(faction);
        }
        else{
            // Send out new request
            pending.put(target, faction);
            player.sendMessage(Chat.format(Settings.ALLY_REQUEST_SENT.replace("{ally}", target.getName()).replace("{role}", getRole().toString()).replace("{minutes}", String.valueOf(Settings.RELATION_EXPIRE_MINUTES))));
            target.getMembers().stream().filter(member -> member.getBukkitOfflinePlayer().isOnline() && member.getRole().ordinal() >= getRole().ordinal()).forEach(member -> {
                member.getBukkitPlayer().sendMessage(Chat.format(Settings.INCOMING_ALLY_REQUEST.replace("{ally}", faction.getName()).replace("{minutes}", String.valueOf(Settings.RELATION_EXPIRE_MINUTES))));
                TextComponent message = new TextComponent(Settings.INCOMING_ALLY_REQUEST_CLICK);
                message.setBold(true);
                message.setColor(ChatColor.GREEN);
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{ new TextComponent("Click to become allies with " + faction.getName()) }));
                message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/faction ally " + faction.getName()));
                member.getBukkitPlayer().spigot().sendMessage(message);
            });
            new BukkitRunnable(){
                @Override
                public void run() {
                    if(pending.containsKey(target)){
                        pending.remove(target);
                        player.sendMessage(Chat.format(Settings.RELATION_REQUEST_EXPIRED.replace("{target}", target.getName()).replace("{type}", "allies")));
                        target.getMembers().stream().filter(member -> member.getBukkitOfflinePlayer().isOnline() && member.getRole().ordinal() >= getRole().ordinal()).forEach(member -> member.getBukkitPlayer().sendMessage(Chat.format(Settings.RELATION_REQUEST_EXPIRED_TO.replace("{type}", "allies").replace("{from}", faction.getName()))));
                    }
                }
            }.runTaskLater(SimpleFactions.getInstance(), 20 * 60 * Settings.RELATION_EXPIRE_MINUTES);
        }
    }
}
