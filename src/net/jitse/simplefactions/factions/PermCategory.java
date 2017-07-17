package net.jitse.simplefactions.factions;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by Jitse on 17-7-2017.
 */
public enum PermCategory {
    MOD, MEM, ALL, NEU, ENE;

    public static Optional<PermCategory> fromString(String str) {
        return Stream.of(values()).filter(category -> category.toString().equalsIgnoreCase(str)).findFirst();
    }
}
