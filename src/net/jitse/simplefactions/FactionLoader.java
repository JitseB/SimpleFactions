package net.jitse.simplefactions;

/**
 * Created by Jitse on 23-6-2017.
 */
public class FactionLoader {

    public void load(Runnable finished){
        //fetch all data from database -> insert in-memory

        finished.run();
    }
}
