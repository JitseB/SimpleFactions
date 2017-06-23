package net.jitse.simplefactions.factions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Jitse on 23-6-2017.
 */
public class Member {

    private final UUID uuid;
    private Role role;

    public Member(UUID uuid, Role role){
        this.uuid = uuid;
        this.role = role;
    }

    public UUID getUUID(){
        return this.uuid;
    }

    public Role getRole(){
        return this.role;
    }

    public Player getPlayer() throws Exception {
        Player player = Bukkit.getPlayer(this.uuid);
        if(player == null)
            throw new Exception("Tried to use Member#getPlayer, but did not check whether the object == null.");
        return Bukkit.getPlayer(this.uuid);
    }
}
