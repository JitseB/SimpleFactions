package net.jitse.simplefactions.commands.subcommands;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.commands.SubCommand;
import net.jitse.simplefactions.factions.Faction;
import net.jitse.simplefactions.factions.PermCategory;
import net.jitse.simplefactions.factions.PermSetting;
import net.jitse.simplefactions.factions.Role;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Jitse on 15-7-2017.
 */
public class PermCommand extends SubCommand {

    public PermCommand(Role role){
        super(role, "simplfactions.commands.perm");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Chat.format(Settings.PLAYER_ONLY_COMMAND));
            return;
        }
        Player player = (Player) sender;
        Faction faction = SimpleFactions.getInstance().getFactionsManager().getFaction(player);
        if(args.length == 1){
            // Get map with permission settings.
            Chat.centeredMessage(sender, Chat.format("&8-----     &5&lFaction&r&5 Permission Settings:     &8-----"));
            Chat.centeredMessage(sender, Chat.format("&7Faction owners have all permission by default."));
            player.sendMessage(Chat.format("&e" + StringUtils.join(PermCategory.values(), " ")));
            for(PermSetting setting : PermSetting.values()){
                if(setting == PermSetting.PAINBUILD) continue; // Client request.
                TextComponent builder = new TextComponent();
                for(PermCategory category : PermCategory.values()){
                    TextComponent component;
                    if(setting == PermSetting.PRESSUREPLATES && category == PermCategory.ENE){
                        component = new TextComponent("NOO");
                        component.setColor(ChatColor.GRAY);
                        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] { new TextComponent(ChatColor.GRAY + "Unavailable, this setting can't be changed") }));
                    } else{
                        if(faction.getSetting(category, setting)){
                            // Enabled for this category and setting.
                            component = new TextComponent("YES");
                            component.setColor(ChatColor.GREEN);
                            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] { new TextComponent("Click to toggle this setting\n" + category.getDescription() + ": " + setting.getUpperCamelCaseString() + " to no") }));
                            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/faction perm " + category.toString() + " " + setting.toString() + " no getmap"));
                        } else{
                            // Disabled for this category and setting.
                            component = new TextComponent("NOO");
                            component.setColor(ChatColor.RED);
                            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] { new TextComponent("Click to toggle this setting\n" + category.getDescription() + ": " + setting.getUpperCamelCaseString() + " to yes") }));
                            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/faction perm " + category.toString() + " " + setting.toString() + " yes getmap"));
                        }
                    }
                    builder.addExtra(component);
                    TextComponent space = new TextComponent(" ");
                    builder.addExtra(space);
                }
                TextComponent name = new TextComponent(setting.getUpperCamelCaseString());
                name.setColor(ChatColor.YELLOW);
                TextComponent description = new TextComponent(": " + setting.getDescription());
                description.setColor(ChatColor.GRAY);
                name.addExtra(description);
                builder.addExtra(name);
                player.spigot().sendMessage(builder);
            }
            return;
        }
        if(args.length != 4 && args.length != 5){
            player.sendMessage(Chat.format(Settings.COMMAND_USAGE_MESSAGE.replace("{syntax}", "/faction perm [<category> <settings> <yes | no>]")));
            player.sendMessage(Chat.format("&cCategories: &7" + StringUtils.join(PermCategory.values(), ", ")));
            player.sendMessage(Chat.format("&cSettings: &7" + StringUtils.join(PermSetting.values(), ", ")));
            return;
        }

        // Set the permission setting.
        PermCategory category = PermCategory.fromString(args[1]).orElse(null);
        PermSetting setting = PermSetting.fromString(args[2]).orElse(null);
        if(category == null || setting == null || (!args[3].equalsIgnoreCase("yes") && !args[3].equalsIgnoreCase("no"))){
            player.sendMessage(Chat.format(Settings.INVALID_COMMAND_USAGE));
            return;
        }
        boolean yes = args[3].equalsIgnoreCase("yes") ? true : false;
        faction.setSetting(category, setting, yes);
        if(args.length == 5 && args[4].equals("getmap"))
            player.performCommand("faction perm"); // To get the map again.
        else {
            player.sendMessage(Chat.format(Settings.CHANGED_SETTING
                    .replace("{category}", category.toString().toLowerCase())
                    .replace("{setting}", setting.toString().toLowerCase())
                    .replace("{value}", args[3].toLowerCase()))
            );
        }
    }
}
