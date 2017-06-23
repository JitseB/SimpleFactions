package net.jitse.simplefactions.events;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Jitse on 23-6-2017.
 */
public class PlayerChangeChunkEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Chunk from, to;

    public PlayerChangeChunkEvent(Player player, Chunk from, Chunk to){
        this.player = player;
        this.from = from;
        this.to = to;
    }

    public Player getPlayer(){
        return this.player;
    }

    public Chunk getFrom(){
        return this.from;
    }

    public Chunk getTo(){
        return this.to;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}