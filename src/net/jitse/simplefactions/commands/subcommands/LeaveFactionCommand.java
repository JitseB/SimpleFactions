package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.Faction;
import net.jitse.simplefactions.factions.Member;
import net.jitse.simplefactions.factions.Role;
import net.jitse.simplefactions.listeners.PlayerListener;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Jitse on 9-7-2017.
 */
public class LeaveFactionCommand extends SubCommand {

    public LeaveFactionCommand(Role role){
        super(role, "simplefactions.commands.leave");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Chat.format(Settings.PLAYER_ONLY_COMMAND));
            return;
        }
        Player player = (Player) sender;
        Faction faction = SimpleFactions.getInstance().getFactionsManager().getFaction(player);
        Member member = SimpleFactions.getInstance().getFactionsManager().getMember(player);
        if(faction.getCreator().equals(player.getUniqueId())){
            player.sendMessage(Chat.format(Settings.LEAVE_EQUALS_DISBAND));
            TextComponent message = new TextComponent(Settings.LEAVE_EQUALS_DISBAND_CLICK_HERE);
            message.setBold(true);
            message.setColor(ChatColor.RED);
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{ new TextComponent("Click to disband " + faction.getName()) }));
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/faction disband"));
            player.spigot().sendMessage(message);
        } else{
            faction.removeMember(member);
            if(PlayerListener.getPlayerChatChannelMap().containsKey(member.getUUID()))
                PlayerListener.getPlayerChatChannelMap().remove(member.getUUID());
            SimpleFactions.getInstance().getFactionsManager().getFactionsPlayer(player).setPower(member.getPower());
            player.sendMessage(Chat.format(Settings.LEFT_FACTION_MESSAGE));
            faction.getMembers().stream()
                    .filter(fmember -> fmember.getBukkitOfflinePlayer().isOnline())
                    .forEach(online -> online.getBukkitPlayer()
                            .sendMessage(Chat.format(Settings.LEFT_FACTION
                                    .replace("{player}", player.getName())
                                    .replace("{faction}", faction.getName())
                                    .replace("{ownfactioncolor}", Settings.OWN_FACTION_COLOR.toString())))
                    );
        }
    }
}
