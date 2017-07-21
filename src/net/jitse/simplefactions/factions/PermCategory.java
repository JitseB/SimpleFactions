package net.jitse.simplefactions.factions;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by Jitse on 17-7-2017.
 */
public enum PermCategory {
    MOD("Faction Moderators"),
    MEM("Faction Members"),
    ALL("Ally Factions"),
    NEU("Neutral Factions"),
    ENE("Enemy Factions");

    String description;

    PermCategory(String description){
        this.description = description;
    }

    public String getDescription(){
        return this.description;
    }

    public static Optional<PermCategory> fromString(String str) {
        return Stream.of(values()).filter(category -> category.toString().equalsIgnoreCase(str)).findFirst();
    }
}
