package net.jitse.simplefactions.factions;

import org.bukkit.Chunk;

import java.util.Set;

/**
 * Created by Jitse on 22-6-2017.
 */
public class Faction {

    private Set<Member> members;
    private Set<Chunk> chunks;
    private Set<Faction> allies, enemies;

    public Faction(Set<Member> members, Set<Chunk> chunks, Set<Faction> allies, Set<Faction> enemies){
        this.members = members;
        this.chunks = chunks;
        this.allies = allies;
        this.enemies = enemies;
    }

    public Set<Chunk> getClaimedChunks(){
        return this.chunks;
    }

    public Set<Member> getMembers(){
        return this.members;
    }

    public Set<Faction> getAllies(){
        return this.allies;
    }

    public Set<Faction> getEnemies(){
        return this.enemies;
    }

    public void claimChunk(Chunk chunk){
        this.chunks.add(chunk);
        //update mysql
    }

    public void addMember(Member member){
        this.members.add(member);
        //update mysql
    }
}
