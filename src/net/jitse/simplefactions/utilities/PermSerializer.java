package net.jitse.simplefactions.utilities;

import net.jitse.simplefactions.factions.PermCategory;
import net.jitse.simplefactions.factions.PermSetting;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * Created by Jitse on 17-7-2017.
 */
public class PermSerializer {

    public static Map<PermCategory, ArrayList<PermSetting>> deserialize(String input){
        Map<PermCategory, ArrayList<PermSetting>> map = new HashMap<>();
        for(String element : input.split(";")){
            if(element == null || element.equals("")) continue;
            PermCategory category = PermCategory.valueOf(element.substring(0, 3));
            List<String> list = Arrays.asList((element.substring(3, element.length()).split(":")));
            ArrayList<PermSetting> settings = new ArrayList<>();
            for (String item: list){
                if(item == null || item.equals("")) continue;
                settings.add(PermSetting.valueOf(item));
            }
            map.put(category, settings);
        }
        return map;
    }

    public static String serialize(Map<PermCategory, ArrayList<PermSetting>> input){
        StringBuilder builder = new StringBuilder();
        for(Map.Entry<PermCategory, ArrayList<PermSetting>> entry : input.entrySet()){
            String item = StringUtils.join(entry.getValue(), ":");
            builder.append(entry.getKey().toString() + item + ";");
        }
        return builder.toString();
    }
}
