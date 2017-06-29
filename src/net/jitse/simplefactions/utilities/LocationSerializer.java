package net.jitse.simplefactions.utilities;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Created by Jitse on 24-6-2017.
 */
public class LocationSerializer {

    public static String serialize(Location location){
        return location.getWorld().getName() + ";" + location.getX() + ";" + location.getY() + ";" +
                location.getZ() + ";" + location.getYaw() + ";" + location.getPitch();
    }
    public static Location deserialize(String input){
        String[] info = input.split(";");

        return new Location(
                Bukkit.getWorld(info[0]),
                Double.parseDouble(info[1]),
                Double.parseDouble(info[2]),
                Double.parseDouble(info[3]),
                Float.parseFloat(info[4]),
                Float.parseFloat(info[5])
        );
    }
}
