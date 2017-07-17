package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.Faction;
import net.jitse.simplefactions.factions.Role;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Date;

/**
 * Created by Jitse on 8-7-2017.
 */
public class ShowCommand extends SubCommand {

    public ShowCommand(){
        super(Role.MEMBER, "simplefactions.commands.show");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(args.length == 1){
            if(!(sender instanceof Player)){
                sender.sendMessage(Chat.format(Settings.PLAYER_ONLY_COMMAND));
                return;
            }
            Faction faction = SimpleFactions.getInstance().getFactionsManager().getFaction((Player) sender);
            if(faction == null){
                sender.sendMessage(Chat.format(Settings.COMMAND_USAGE_MESSAGE.replace("{syntax}", "/faction show [name]")));
                return;
            }
            sendInfo(sender, faction);
            return;
        }
        Faction faction = SimpleFactions.getInstance().getFactionsManager().getFaction(args[1]);
        if(faction == null){
            sender.sendMessage(Chat.format(Settings.FACTION_NOT_EXISTS.replace("{faction}", args[1])));
            return;
        }
        sendInfo(sender, faction);
    }

    private void sendInfo(CommandSender sender, Faction faction){
        Chat.centeredMessage(sender, Chat.format("&8-----     &5&l" + faction.getName() + "&r&5 Faction Info:     &8-----"));
        sender.sendMessage(Chat.format("&fJoining: &7" + (faction.isOpen() ? "&aNo invitation required" : "&cInvitation required") + "."));
        sender.sendMessage(Chat.format("&fLand / Power / Maxpower: &7" + faction.getClaimedChunks().size() + "/" + faction.getTotalPower() + "/" + faction.getMaxPower()));
        sender.sendMessage(Chat.format("&fFounded: &7" + new Date(faction.getFounded().getTime()).toString()));
        sender.sendMessage(Chat.format("&fBalance: &7" + faction.getTotalBalance()));
        StringBuilder alliesBuilder = new StringBuilder();
        if(faction.getAllies().size() == 0) alliesBuilder.append("-");
        else faction.getAllies().forEach(ally -> alliesBuilder.append(ally.getName()));
        sender.sendMessage(Chat.format("&fAllies (&7" + faction.getAllies().size() + "/∞&f): " + alliesBuilder.toString()));
        StringBuilder membersBuilder = new StringBuilder();
        faction.getMembers().forEach(member -> membersBuilder.append((member.getBukkitOfflinePlayer().isOnline() ? "&a" : "&c") + member.getBukkitOfflinePlayer().getName() + " "));
        sender.sendMessage(Chat.format("&fMembers (&7" +
                faction.getMembers().stream().filter(member -> member.getBukkitOfflinePlayer().isOnline()).count() + "/" + faction.getMembers().size() + "&f): " +
                membersBuilder.toString())
        );
    }
}
