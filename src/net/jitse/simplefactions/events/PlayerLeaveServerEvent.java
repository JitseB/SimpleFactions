package net.jitse.simplefactions.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Jitse on 25-6-2017.
 */
public class PlayerLeaveServerEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;

    public PlayerLeaveServerEvent(Player player){
        this.player = player;
    }

    public Player getPlayer(){
        return this.player;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
