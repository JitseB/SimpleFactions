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
        if(args.length == 0){
            // Get map with permission settings.
            Chat.centeredMessage(sender, Chat.format("&8-----     &5&lFaction&r&5 Permission Settings:     &8-----"));
            Chat.centeredMessage(sender, Chat.format("&7Faction owners have all permission by default."));
            for(PermSetting setting : PermSetting.values()){
                TextComponent builder = new TextComponent();
                for(PermCategory category : PermCategory.values()){
                    TextComponent component;
                    if(faction.getSetting(category, setting)){
                        // Enabled for this category and setting.
                        component = new TextComponent("YES");
                        component.setBold(true);
                        component.setColor(ChatColor.GREEN);
                        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] { new TextComponent("Click to toggle setting") }));
                        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/faction perm " + category.toString() + " " + setting.toString() + " no getmap"));
                    } else{
                        // Disabled for this category and setting.
                        component = new TextComponent("NO");
                        component.setBold(true);
                        component.setColor(ChatColor.RED);
                        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] { new TextComponent("Click to toggle setting") }));
                        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/faction perm " + category.toString() + " " + setting.toString() + " yes getmap"));
                    }
                    builder.addExtra(component);
                }
                TextComponent name = new TextComponent(setting.toString());
                name.setColor(ChatColor.LIGHT_PURPLE);
                TextComponent description = new TextComponent(" : " + setting.getDescription());
                description.setColor(ChatColor.GRAY);
                name.addExtra(description);
                builder.addExtra(name);
                player.spigot().sendMessage(builder);
            }
        }
        if(args.length != 4 && args.length != 5){
            player.sendMessage(Chat.format(Settings.COMMAND_USAGE_MESSAGE.replace("{syntax}", "/faction perm [<category> <settings> <yes | no>]")));
            player.sendMessage(Chat.format("&cCategories: &7" + StringUtils.join(PermCategory.values(), ", ")));
            player.sendMessage(Chat.format("&cSettings: &7" + StringUtils.join(PermSetting.values(), ", ")));
            return;
        }
    }
}
