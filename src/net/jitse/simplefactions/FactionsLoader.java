package net.jitse.simplefactions;

/**
 * Created by Jitse on 23-6-2017.
 */
public class FactionsLoader {

    private SimpleFactions plugin;

    public FactionsLoader(SimpleFactions plugin){
        this.plugin = plugin;
    }

    public void load(Runnable finished){
        this.plugin.getMySql().select("SELECT * FROM Factions;", resultSet -> {

        });
        // fetch all data from database -> insert in-memory
        // after fetching is done -> finished.run();
    }
}
