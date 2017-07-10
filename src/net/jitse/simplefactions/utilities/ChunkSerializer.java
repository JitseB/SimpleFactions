package net.jitse.simplefactions.utilities;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jitse on 25-6-2017.
 */
public class ChunkSerializer {

    public static String serialize(Set<Chunk> set){
        StringBuilder result = new StringBuilder();
        if(set.size() == 1) set.forEach(chunk -> result.append(chunk.getWorld().getName() + ":" + String.valueOf(chunk.getX()) + ":" + String.valueOf(chunk.getZ())));
        else set.forEach(chunk -> result.append(chunk.getWorld().getName() + ":" + String.valueOf(chunk.getX()) + ":" + String.valueOf(chunk.getZ()) + ";"));
        return result.toString();
    }

    public static Set<Chunk> deserialize(String input){
        Set<Chunk> chunks = new HashSet<>();
        if(input == null) return chunks;
        if(input.contains(";")){
            for(String element : input.split(";")){
                if(!element.contains(":") || element.split(":").length != 3)
                    continue;
                String[] chunkInfo = element.split(":");
                chunks.add(Bukkit.getWorld(chunkInfo[0]).getChunkAt(Integer.valueOf(chunkInfo[1]), Integer.valueOf(chunkInfo[2])));
            }
        } else{
            String[] chunkInfo = input.split(":");
            chunks.add(Bukkit.getWorld(chunkInfo[0]).getChunkAt(Integer.valueOf(chunkInfo[1]), Integer.valueOf(chunkInfo[2])));
        }
        return chunks;
    }
}
