package net.jitse.simplefactions;

import net.jitse.simplefactions.factions.*;
import net.jitse.simplefactions.utilities.ChunkSerializer;
import net.jitse.simplefactions.utilities.LocationSerializer;
import net.jitse.simplefactions.utilities.Logger;
import net.jitse.simplefactions.utilities.RelationState;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Jitse on 23-6-2017.
 */
public class FactionsLoader {

    private SimpleFactions plugin;

    public FactionsLoader(SimpleFactions plugin){
        this.plugin = plugin;
    }

    // This actually needs an async operations chain.
    public void load(Runnable finished){
        Set<Faction> factions = new HashSet<>();
        // Fetch faction profiles.
        this.plugin.getMySql().select("SELECT * FROM Factions;", factionSet -> {
            try{
                while (factionSet.next()){
                    factions.add(new Faction(factionSet.getString("name"), UUID.fromString(factionSet.getString("creator")),
                            new HashSet<>(), ChunkSerializer.deserialize(factionSet.getString("claimed")), new HashSet<>(),
                            new HashSet<>(), new HashSet<>(), factionSet.getBoolean("open"), factionSet.getTimestamp("created"))
                    );
                }
            } catch (SQLException exception){
                Logger.log(Logger.LogLevel.ERROR, "An SQL error occured while fetching all faction profiles from the database.");
                exception.printStackTrace();
            }

            // For the next few functions, updating it once again later (with more data).
            this.plugin.getFactionsManager().init(factions);

            // Fetch faction homes
            this.plugin.getMySql().select("SELECT * FROM FactionHomes;", homeSet -> {
                try{
                    while (homeSet.next()){
                        Faction faction = this.plugin.getFactionsManager().getFaction(homeSet.getString("faction"));
                        faction.addHome(new Home(homeSet.getString("name"), LocationSerializer.deserialize(homeSet.getString("location"))), false);
                    }
                } catch (SQLException exception){
                    Logger.log(Logger.LogLevel.ERROR, "An SQL error occured while fetching all faction homes from the database.");
                    exception.printStackTrace();
                }

                // Fetch faction members.
                this.plugin.getMySql().select("SELECT * FROM FactionMembers;", memberSet -> {
                    try{
                        while (memberSet.next()){
                            Faction faction = this.plugin.getFactionsManager().getFaction(memberSet.getString("faction"));
                            if(faction == null) {
                                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(memberSet.getString("uuid")));
                                Logger.log(Logger.LogLevel.ERROR, "A player with " + (offlinePlayer == null ? ("uuid: " + memberSet.getString("uuid")) : ("name: " + offlinePlayer.getName())) + " is in a faction which doesn't exist!");
                                continue;
                            }
                            Role role = Role.valueOf(memberSet.getString("role"));
                            Timestamp joinedFaction = memberSet.getTimestamp("joinedfaction");
                            this.plugin.getMySql().select("SELECT * FROM FactionPlayers WHERE uuid=?;", playerSet -> {
                                try{
                                    if(!playerSet.next()) return;
                                    faction.addMember(
                                            new Member(
                                                    UUID.fromString(playerSet.getString("uuid")), joinedFaction, role,
                                                    playerSet.getInt("kills"), playerSet.getInt("deaths"),
                                                    playerSet.getInt("power"), playerSet.getTimestamp("lastseen")
                                            ), false, false
                                    );
                                } catch (SQLException exception){
                                    Logger.log(Logger.LogLevel.ERROR, "An SQL error occured while fetching a player profile from the database.");
                                    exception.printStackTrace();
                                }
                            }, memberSet.getString("uuid"));
                        }
                    } catch (SQLException exception){
                        Logger.log(Logger.LogLevel.ERROR, "An SQL error occured while fetching all faction members from the database.");
                        exception.printStackTrace();
                    }

                    // Fetch faction relations.
                    this.plugin.getMySql().select("SELECT * FROM FactionRelations;", relationSet -> {
                        try{
                            while (relationSet.next()){
                                Faction faction = this.plugin.getFactionsManager().getFaction(relationSet.getString("faction-one"));
                                Faction target = this.plugin.getFactionsManager().getFaction(relationSet.getString("faction-two"));
                                RelationState relation = RelationState.valueOf(relationSet.getString("relation"));
                                switch (relation){
                                    case ALLIES:
                                        faction.initSetAllies(target);
                                        break;
                                    case ENEMIES:
                                        faction.setEnemy(target, false);
                                        break;
                                }
                            }
                        } catch (SQLException exception){
                            Logger.log(Logger.LogLevel.ERROR, "An SQL error occured while fetching all faction relations from the database.");
                            exception.printStackTrace();
                        }

                        this.plugin.getFactionsManager().init(factions);

                        // And finally, execute the runnable.
                        finished.run();
                    });
                });
            });
        });
    }
}
