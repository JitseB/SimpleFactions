package net.jitse.simplefactions.factions;

import org.bukkit.Location;

/**
 * Created by Jitse on 25-6-2017.
 */
public class Home {

    private final String name;
    private final Location location;

    public Home(String name, Location location){
        this.location = location;
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public Location getLocation(){
        return this.location;
    }
}
