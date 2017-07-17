package net.jitse.simplefactions.factions;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by Jitse on 17-7-2017.
 */
public enum PermSetting {
    BUILD("Ability to build."),
    PAINBUILD("Get damaged while building."),
    DOOR("Ability to use doors."),
    BUTTON("Ability to use buttons."),
    LEVER("Ability to use levers."),
    PRESSUREPLATES("Usage of pressure plates.");

    String description;

    PermSetting(String description){
        this.description = description;
    }

    public String getDescription(){
        return this.description;
    }

    public static Optional<PermSetting> fromString(String str) {
        return Stream.of(values()).filter(setting -> setting.toString().equalsIgnoreCase(str)).findFirst();
    }
}
