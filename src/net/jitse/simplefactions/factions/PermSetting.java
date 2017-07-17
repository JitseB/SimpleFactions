package net.jitse.simplefactions.factions;

/**
 * Created by Jitse on 17-7-2017.
 */
public enum PermSetting {
    BUILD("Be able to build."),
    PAINBUILD("Be able to build, but get damaged while doing so."),
    DOOR("Ability to use doors."),
    BUTTON("Ability to use buttons."),
    LEVER("Ability to use levers."),
    PRESSUREPLATES("Ability to use pressure plates.");

    String description;

    PermSetting(String description){
        this.description = description;
    }

    public String getDescription(){
        return this.description;
    }
}
