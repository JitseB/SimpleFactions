package net.jitse.simplefactions.factions;

import net.jitse.simplefactions.SimpleFactions;

import java.util.UUID;

/**
 * Created by Jitse on 13-7-2017.
 */
public class Partner {

    private UUID uuid;
    private Faction faction;

    public Partner(String data){
        if(data.length() == 36) this.uuid = UUID.fromString(data);
        else faction = SimpleFactions.getInstance().getFactionsManager().getFaction(data);
    }

    public Object getData(){
        return uuid == null ? faction : uuid;
    }

    public String getInfo(){
        return uuid == null ? faction.getName() : uuid.toString();
    }
}
