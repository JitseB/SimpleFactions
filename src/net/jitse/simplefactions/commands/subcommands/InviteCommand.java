package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.Faction;
import net.jitse.simplefactions.factions.Player;
import net.jitse.simplefactions.factions.Role;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jitse on 10-7-2017.
 */
public class InviteCommand extends SubCommand {

    private static Map<Player, Faction> pending = new HashMap<>();

    public InviteCommand(Role role){
        super(role, "simplefactions.commands.invite");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if(!(sender instanceof org.bukkit.entity.Player)){
            sender.sendMessage(Chat.format(Settings.PLAYER_ONLY_COMMAND));
            return;
        }
        org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
        if(args.length != 2){
            player.sendMessage(Chat.format(Settings.COMMAND_USAGE_MESSAGE.replace("{syntax}", "/faction invite <player> (Player must be online)")));
            return;
        }
        org.bukkit.entity.Player target = Bukkit.getPlayer(args[1]);
        if(target == null || !target.isOnline()){
            player.sendMessage(Chat.format(Settings.INVALID_COMMAND_USAGE));
            return;
        }
        Faction faction = SimpleFactions.getInstance().getFactionsManager().getFaction(player);
        Player inviteTo = SimpleFactions.getInstance().getFactionsManager().getFactionsPlayer(target);
        if(pending.containsKey(inviteTo)){
            player.sendMessage(Chat.format(Settings.PLAYER_ALREADY_GOT_INVITE.replace("{player}", target.getName())));
            return;
        }
        pending.put(SimpleFactions.getInstance().getFactionsManager().getFactionsPlayer(target), faction);
        player.sendMessage(Chat.format(Settings.INVITE_SEND.replace("{target}", target.getName()).replace("{expiretime}", String.valueOf(Settings.INVITE_EXPIRE_TIME))));
        target.sendMessage(Chat.format(Settings.INCOMING_INVITE.replace("{from}", player.getName()).replace("{faction}", faction.getName()).replace("{expiretime}", String.valueOf(Settings.INVITE_EXPIRE_TIME))));
        TextComponent message = new TextComponent(Settings.CONFIRM_INVITE);
        message.setBold(true);
        message.setColor(ChatColor.GREEN);
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{ new TextComponent("Click to join " + faction.getName()) }));
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/faction join " + faction.getName()));
        target.spigot().sendMessage(message);
        new BukkitRunnable(){
            @Override
            public void run() {
                if(pending.containsKey(inviteTo)){
                    if(target.isOnline()) target.sendMessage(Chat.format(Settings.INVITE_EXPIRED_TO_RECIEVER.replace("{faction}", faction.getName())));
                    if(player.isOnline()) player.sendMessage(Chat.format(Settings.INVITE_EXPIRED_TO_SENDER.replace("{player}", target.getName())));
                    pending.remove(SimpleFactions.getInstance().getFactionsManager().getFactionsPlayer(target));
                }
            }
        }.runTaskLater(SimpleFactions.getInstance(), 20 * Settings.INVITE_EXPIRE_TIME);
    }

    public static Map<Player, Faction> getPending(){
        return pending;
    }
}
