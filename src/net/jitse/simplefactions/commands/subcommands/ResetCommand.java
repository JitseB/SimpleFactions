package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.Role;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

/**
 * Created by Jitse on 28-6-2017.
 */
public class ResetCommand extends SubCommand {

    public ResetCommand(){
        super(Role.OWNER, "simplefactions.admin.reset");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        SimpleFactions.getInstance().setJoinable(false);
        Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer(Chat.format(Settings.SERVER_NAME + "\n\n" + Settings.SYSTEM_RESET_KICK)));
        SimpleFactions.getInstance().getMySql().execute("TRUNCATE TABLE Factions;");
        SimpleFactions.getInstance().getMySql().execute("TRUNCATE TABLE FactionHomes;");
        SimpleFactions.getInstance().getMySql().execute("TRUNCATE TABLE FactionMembers;");
        SimpleFactions.getInstance().getMySql().execute("TRUNCATE TABLE FactionPlayers;");
        SimpleFactions.getInstance().getMySql().execute("TRUNCATE TABLE FactionRelations;");
        SimpleFactions.getInstance().getMySql().execute("TRUNCATE TABLE FactionTrusted;");
        Bukkit.getScheduler().runTaskLater(SimpleFactions.getInstance(), () -> {
            Chat.broadcast(Chat.format(Settings.COMMAND_PREFIX + Settings.SUCCESS_FULLY_RESET_SYSTEM));
            Bukkit.reload();
        }, 5);
    }
}
