package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.Role;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Jitse on 13-7-2017.
 */
public class SidebarToggleCommand extends SubCommand {

    private List<UUID> cooldown = new ArrayList<>();

    public SidebarToggleCommand(){
        super(Role.MEMBER, "simplefactions.commands.sidebartoggle");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Chat.format(Settings.PLAYER_ONLY_COMMAND));
            return;
        }
        Player player = (Player) sender;
        if(cooldown.contains(player.getUniqueId())){
            player.sendMessage(Chat.format(Settings.COOLDOWN_MESSAGE));
            return;
        }
        net.jitse.simplefactions.factions.Player fplayer = SimpleFactions.getInstance().getFactionsManager().getFactionsPlayer(player);
        fplayer.setSidebar(!fplayer.wantsSidebar());
        player.sendMessage(Chat.format(Settings.TOGGLED_SIDEBAR));
        UUID uuid = player.getUniqueId();
        cooldown.add(uuid);
        Bukkit.getScheduler().runTaskLaterAsynchronously(SimpleFactions.getInstance(), () -> cooldown.remove(uuid), 100);
    }
}
