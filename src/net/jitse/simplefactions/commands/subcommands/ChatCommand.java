package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.ChatChannel;
import net.jitse.simplefactions.factions.Role;
import net.jitse.simplefactions.listeners.PlayerListener;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Jitse on 17-7-2017.
 */
public class ChatCommand extends SubCommand {

    public ChatCommand(Role role){
        super(role, "simplefactions.commands.chat");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Chat.format(Settings.PLAYER_ONLY_COMMAND));
            return;
        }
        Player player = (Player) sender;
        if(args.length != 2){
            player.sendMessage(Chat.format(Settings.COMMAND_USAGE_MESSAGE.replace("{syntax}", "/faction chat <public | faction | allies>")));
            return;
        }
        ChatChannel chatChannel = ChatChannel.fromString(args[1]).orElse(null);
        if(chatChannel == null){
            player.sendMessage(Chat.format(Settings.INVALID_COMMAND_USAGE));
            return;
        }
        if(chatChannel == ChatChannel.PUBLIC){
            if(PlayerListener.getPlayerChatChannelMap().containsKey(player.getUniqueId())) {
                PlayerListener.getPlayerChatChannelMap().remove(player.getUniqueId());
                player.sendMessage(Chat.format(Settings.NOW_IN_CHANNEL.replace("{channel}", chatChannel.toString().toLowerCase())));
            }
            else{
                player.sendMessage(Chat.format(Settings.ALREADY_IN_CHANNEL.replace("{channel}", chatChannel.toString().toLowerCase())));
            }
        } else{
            if(PlayerListener.getPlayerChatChannelMap().containsKey(player.getUniqueId()))
                PlayerListener.getPlayerChatChannelMap().remove(player.getUniqueId());
            switch (chatChannel){
                case FACTION:
                    PlayerListener.getPlayerChatChannelMap().put(player.getUniqueId(), chatChannel);
                    break;
                case ALLIES:
                    PlayerListener.getPlayerChatChannelMap().put(player.getUniqueId(), chatChannel);
                    break;
            }
            player.sendMessage(Chat.format(Settings.NOW_IN_CHANNEL.replace("{channel}", chatChannel.toString().toLowerCase())));
        }
    }
}
