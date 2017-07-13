package net.jitse.simplefactions.managers;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.factions.Faction;
import net.jitse.simplefactions.factions.Partner;
import net.jitse.simplefactions.utilities.ChunkSerializer;

import java.sql.SQLException;

/**
 * Created by Jitse on 13-7-2017.
 */
public class TrustedManager {

    private final SimpleFactions plugin;

    public TrustedManager(SimpleFactions plugin){
        this.plugin = plugin;
    }

    public void load(Faction faction){
        plugin.getMySql().select("SELECT * FROM FactionTrusted WHERE faction=?;", resultSet -> {
            try{
                while(resultSet.next()){
                    faction.addPartner(ChunkSerializer.fromString(resultSet.getString("chunk")), new Partner(resultSet.getString("partner")), false);
                }
            } catch (SQLException exception){
                exception.printStackTrace();
            }
        }, faction.getName());
    }
}
