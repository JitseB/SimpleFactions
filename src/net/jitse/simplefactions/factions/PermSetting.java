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
    FLY("Access to /faction fly."),
    PRESSUREPLATES("Pressure plate usage.");

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

    public String getUpperCamelCaseString(){
        return this.toString().substring(0, 1).toUpperCase() + this.toString().substring(1, this.toString().length()).toLowerCase();
    }
}
