package net.jitse.simplefactions.factions;

import org.bukkit.Chunk;

import java.util.Set;

/**
 * Created by Jitse on 22-6-2017.
 */
public class Faction {

    private Set<Member> members;
    private Set<Chunk> chunks;

    public Faction(Set<Member> members, Set<Chunk> chunks){
        this.members = members;
        this.chunks = chunks;
    }

    public Set<Chunk> getClaimedChunks(){
        return this.chunks;
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
