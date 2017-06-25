package net.jitse.simplefactions;

import net.jitse.simplefactions.factions.*;
import net.jitse.simplefactions.utilities.Locations;
import net.jitse.simplefactions.utilities.Logger;

import java.sql.SQLException;
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

    // Messy function, but won't have to create a delayed timer for the finished runnable... Any other ideas?
    // It actually needs a async operations chain, but I'm lazy af.
    public void load(Runnable finished){
        Set<Faction> factions = new HashSet<>();
        // Fetch faction profiles
        this.plugin.getMySql().select("SELECT * FROM Factions;", factionSet -> {
            try{
                while (factionSet.next()){
                    factions.add(new Faction(factionSet.getString("name"), UUID.fromString(factionSet.getString("creator")),
                            new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>())
                    );
                }
            } catch (SQLException exception){
                Logger.log(Logger.LogLevel.ERROR, "An SQL error occured while fetching all faction profiles from the database.");
                exception.printStackTrace();
            }

            // Fetch faction homes
            this.plugin.getMySql().select("SELECT * FROM FactionHomes;", homeSet -> {
                try{
                    while (homeSet.next()){
                        Faction faction = this.plugin.getFactionsManager().getFaction(homeSet.getString("faction"));
                        faction.initAddHome(new Home(homeSet.getString("name"), Locations.deserialize(homeSet.getString("location"))));
                    }
                } catch (SQLException exception){
                    Logger.log(Logger.LogLevel.ERROR, "An SQL error occured while fetching all faction homes from the database.");
                    exception.printStackTrace();
                }

                // Fetch faction members
                this.plugin.getMySql().select("SELECT * FROM FactionMembers;", memberSet -> {
                    try{
                        while (memberSet.next()){
                            Faction faction = this.plugin.getFactionsManager().getFaction(memberSet.getString("faction"));
                            faction.initAddMember(new Member(UUID.fromString(memberSet.getString("uuid")), memberSet.getTimestamp("joinedfaction"), Role.valueOf(memberSet.getString("role"))));
                        }
                    } catch (SQLException exception){
                        Logger.log(Logger.LogLevel.ERROR, "An SQL error occured while fetching all faction members from the database.");
                        exception.printStackTrace();
                    }

                    // Fetch faction relations
                    this.plugin.getMySql().select("SELECT * FROM FactionRelations;", relationSet -> {
                        try{
                            while (relationSet.next()){
                                Faction factionOne = this.plugin.getFactionsManager().getFaction(relationSet.getString("faction-one"));
                                Faction factionTwo = this.plugin.getFactionsManager().getFaction(relationSet.getString("faction-two"));
                                Relation relation = Relation.valueOf(relationSet.getString("relation"));
                                switch (relation){
                                    case ALLIES:
                                        factionOne.initSetAllies(factionTwo);
                                        factionTwo.initSetAllies(factionOne);
                                        break;
                                    case ENEMIES:
                                        factionOne.initSetEnemies(factionTwo);
                                        factionTwo.initSetEnemies(factionOne);
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
