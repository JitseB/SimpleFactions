package net.jitse.simplefactions.utilities;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.managers.Settings;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Jitse on 8-7-2017.
 */
public class DelayTeleport {

    public static void teleport(Player player, Location location){
        Location check = player.getLocation().clone();
        new BukkitRunnable() {
            int i = Settings.TP_DELAY;
            @Override
            public void run() {
                if(player.getLocation().getX() != check.getX() || player.getLocation().getY() != check.getY() || player.getLocation().getZ() != check.getZ()){
                    player.sendMessage(Chat.format(Settings.MOVED_NO_TELEPORT));
                    this.cancel();
                    return;
                }
                if(i == 0){
                    player.teleport(location);
                    this.cancel();
                    return;
                }
                i--;
            }
        }.runTaskTimer(SimpleFactions.getInstance(), 0, 20);
    }
}
