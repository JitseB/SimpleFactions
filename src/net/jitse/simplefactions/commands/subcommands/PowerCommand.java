package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.Member;
import net.jitse.simplefactions.factions.Role;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Jitse on 8-7-2017.
 */
public class PowerCommand extends SubCommand {

    public PowerCommand(Role role){
        super(role, "simplefactions.commands.power");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Chat.format(Settings.PLAYER_ONLY_COMMAND));
            return;
        }
        Player player = (Player) sender;
        net.jitse.simplefactions.factions.Player fplayer = SimpleFactions.getInstance().getFactionsManager().getFactionsPlayer(player);
        Member member = SimpleFactions.getInstance().getFactionsManager().getMember(player);
        player.sendMessage(Chat.format(Settings.POWER_MESSAGE
                .replace("{power}", String.valueOf((member == null ? fplayer.getPower() : member.getPower())))
                .replace("{maxpower}", String.valueOf(Settings.PLAYER_MAX_POWER))
        ));
    }
}
