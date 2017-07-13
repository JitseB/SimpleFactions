package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.Faction;
import net.jitse.simplefactions.factions.Role;
import net.jitse.simplefactions.utilities.Chat;
import org.bukkit.command.CommandSender;

import java.util.*;

/**
 * Created by Jitse on 12-7-2017.
 */
public class FactionTopCommand extends SubCommand {

    public FactionTopCommand(Role role){
        super(role, "simplefactions.commands.factiontop");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        Chat.centeredMessage(sender, Chat.format("&8-----     &5&lServer&r&5 Top Factions:     &8-----"));
        HashMap<String, Double> map  = new HashMap<>();
        for(Faction faction : SimpleFactions.getInstance().getFactionsManager().getFactions()){
            map.put(faction.getName(), faction.getBalance());
        }
        List<String> top10 = topNKeys(map, 10);
        int n = 1;
        for(String fname : top10){
            Faction faction = SimpleFactions.getInstance().getFactionsManager().getFaction(fname);
            sender.sendMessage(Chat.format("&7#" + n + " &f" + fname + " &8: &e" + faction.getBalance()));
            n++;
        }
    }

    public static List<String> topNKeys(final HashMap<String, Double> map, int n) {
        PriorityQueue<String> topN = new PriorityQueue<>(n, Comparator.comparingDouble(map::get));
        for(String key:map.keySet()){
            if (topN.size() < n)
                topN.add(key);
            else if (map.get(topN.peek()) < map.get(key)) {
                topN.poll();
                topN.add(key);
            }
        }
        return (List) Arrays.asList(topN.toArray());
    }
}
