package net.jitse.simplefactions.utilities;

import net.jitse.simplefactions.factions.PermCategory;
import net.jitse.simplefactions.factions.PermSetting;

import java.util.*;

/**
 * Created by Jitse on 17-7-2017.
 */
public class PermSerializer {

    public static Map<PermCategory, List<PermSetting>> deserialize(String input){
        Map<PermCategory, List<PermSetting>> map = new HashMap<>();
        for(String element : input.split(";")){
            if(element == null || element.equals("")) continue;
            PermCategory category = PermCategory.valueOf(element.substring(0, 2));
            List<String> list = Arrays.asList((element.substring(2, element.length()).split(":")));
            List<PermSetting> settings = new ArrayList<>();
            list.forEach(item -> settings.add(PermSetting.valueOf(item)));
            map.put(category, settings);
        }
        return map;
    }

    public static String serialize(Map<PermCategory, List<PermSetting>> input){
        StringBuilder builder = new StringBuilder();
        for(Map.Entry<PermCategory, List<PermSetting>> entry : input.entrySet()){
            String item = "";
            for(PermSetting setting : entry.getValue()) item += setting.toString();
            builder.append(entry.getKey().toString() + item + ";");
        }
        return builder.toString();
    }
}
