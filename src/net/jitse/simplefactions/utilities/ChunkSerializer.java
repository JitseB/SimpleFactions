package net.jitse.simplefactions.utilities;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jitse on 25-6-2017.
 */
public class ChunkSerializer {

    public static String serialize(Set<Chunk> set){
        StringBuilder result = new StringBuilder();
        set.forEach(chunk -> result.append(chunk.getWorld() + ";" + chunk.getX() + ";" + chunk.getZ() + "|"));
        return result.toString();
    }

    public static Set<Chunk> fromString(String input){
        Set<Chunk> chunks = new HashSet<>();
        for(String element : input.split("|")){
            if(element == null || element == "") continue; // End of serialized item.
            String[] chunkInfo = element.split(";");
            chunks.add(Bukkit.getWorld(chunkInfo[0]).getChunkAt(Integer.valueOf(chunkInfo[1]), Integer.valueOf(chunkInfo[2])));
        }
        return chunks;
    }
}
