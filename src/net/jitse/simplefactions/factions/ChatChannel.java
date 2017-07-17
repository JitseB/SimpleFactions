package net.jitse.simplefactions.factions;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by Jitse on 17-7-2017.
 */
public enum ChatChannel {
    ALL, FACTION, ALLIES;

    public static Optional<ChatChannel> fromString(String str) {
        return Stream.of(values()).filter(channel -> channel.toString().equalsIgnoreCase(str)).findFirst();
    }
}
