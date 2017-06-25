package net.jitse.simplefactions.events;

import net.jitse.simplefactions.factions.Faction;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Jitse on 25-6-2017.
 */
public class FactionCreatedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Faction faction;

    public FactionCreatedEvent(Faction faction){
        this.faction = faction;
    }

    public Faction getFaction(){
        return this.faction;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
