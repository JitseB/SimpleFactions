package net.jitse.simplefactions.factions;

import net.jitse.simplefactions.SimpleFactions;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Created by Jitse on 23-6-2017.
 */
public class Member extends Player {

    private final UUID uuid;
    private final Timestamp joinedFaction;

    private Role role;

    public Member(UUID uuid, Timestamp joinedFaction, Role role, int kills, int deaths, boolean sidebar, int power, Timestamp lastseen){
        super(uuid, kills, deaths, power, lastseen, sidebar);

        this.uuid = uuid;
        this.joinedFaction = joinedFaction;
        this.role = role;
    }

    public Member(UUID uuid, Timestamp joinedFaction, Role role, Player fplayer){
        super(fplayer.getUUID(), fplayer.getKills(), fplayer.getDeaths(), fplayer.getPower(), fplayer.getLastseen(), fplayer.wantsSidebar());

        this.uuid = uuid;
        this.joinedFaction = joinedFaction;
        this.role = role;
    }

    public Faction getFaction(){
        return SimpleFactions.getInstance().getFactionsManager().getFaction(this.getBukkitPlayer());
    }

    public void setRole(Role role){
        this.role = role;
        SimpleFactions.getInstance().getMySql().execute("UPDATE FactionMembers SET role=? WHERE uuid=?;", role.toString(), this.uuid.toString());
    }

    public Timestamp getJoinedFaction(){
        return this.joinedFaction;
    }

    public UUID getUUID(){
        return this.uuid;
    }

    public Role getRole(){
        return this.role;
    }
}
