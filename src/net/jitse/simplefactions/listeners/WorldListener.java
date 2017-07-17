package net.jitse.simplefactions.listeners;

import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.factions.*;
import net.jitse.simplefactions.managers.Settings;
import net.jitse.simplefactions.utilities.Chat;
import net.jitse.simplefactions.utilities.Logger;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;
import java.util.UUID;

/**
 * Created by Jitse on 23-6-2017.
 */
public class WorldListener implements Listener {

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerBlockBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        Chunk chunk = event.getBlock().getChunk();
        Faction fplayer = SimpleFactions.getInstance().getFactionsManager().getFaction(player);
        Faction fchunk = SimpleFactions.getInstance().getFactionsManager().getFaction(chunk);
        if(fchunk != null){
            // Permission settings logic.
            if(fplayer == null){
                if(fchunk.getSetting(PermCategory.NEU, PermSetting.BUILD)) return;
                else if(fchunk.getSetting(PermCategory.NEU, PermSetting.PAINBUILD)){
                    player.damage(Settings.PAINBUILD_DAMAGE);
                    return;
                } else event.setCancelled(true);
            }
            else if(fchunk.equals(fplayer)){
                Role role = SimpleFactions.getInstance().getFactionsManager().getMember(player).getRole();
                switch (role){
                    case MEMBER:
                        if(fplayer.getSetting(PermCategory.MEM, PermSetting.BUILD)) return;
                        else if(fplayer.getSetting(PermCategory.MEM, PermSetting.PAINBUILD)){
                            player.damage(Settings.PAINBUILD_DAMAGE);
                            return;
                        } else {
                            event.setCancelled(true);
                            sendLandAlreadyClaimedMessage(player, fchunk);
                            return;
                        }
                    case MOD:
                        if(fplayer.getSetting(PermCategory.MOD, PermSetting.BUILD)) return;
                        else if(fplayer.getSetting(PermCategory.MOD, PermSetting.PAINBUILD)){
                            player.damage(Settings.PAINBUILD_DAMAGE);
                            return;
                        } else {
                            event.setCancelled(true);
                            sendLandAlreadyClaimedMessage(player, fchunk);
                            return;
                        }
                    default:
                        return; // Owner role.
                }
            }
            else{
                if((fchunk.getAllies().contains(fplayer) && fchunk.getSetting(PermCategory.ALL, PermSetting.BUILD))
                        || (fchunk.getEnemies().contains(fplayer) && fchunk.getSetting(PermCategory.ENE, PermSetting.BUILD))
                        || (!fchunk.getAllies().contains(fplayer) && !fchunk.getEnemies().contains(fplayer) && fchunk.getSetting(PermCategory.NEU, PermSetting.BUILD))){
                    return;
                }
                else if((fchunk.getAllies().contains(fplayer) && fchunk.getSetting(PermCategory.ALL, PermSetting.PAINBUILD))
                        || (fchunk.getEnemies().contains(fplayer) && fchunk.getSetting(PermCategory.ENE, PermSetting.PAINBUILD))
                        || (!fchunk.getAllies().contains(fplayer) && !fchunk.getEnemies().contains(fplayer) && fchunk.getSetting(PermCategory.NEU, PermSetting.PAINBUILD))){
                    player.damage(Settings.PAINBUILD_DAMAGE);
                    return;
                } else event.setCancelled(true);
            }
            // Partner logic.
            List<Partner> partnerList = fchunk.getPartners(chunk);
            if(partnerList == null){
                if(fchunk.equals(fplayer)) event.setCancelled(false);
                else {
                    event.setCancelled(true);
                    sendLandAlreadyClaimedMessage(player, fchunk);
                }
                return;
            } else{
                for(Partner partner : partnerList){
                    if(!(partner.getData() instanceof UUID)) continue;
                    if(partner.getData().equals(player.getUniqueId()))
                        return; // Allowed standalone partner.
                }
                if(fplayer != null){
                    for(Partner partner : partnerList){
                        if(!(partner.getData() instanceof Faction)) continue;
                        if(((Faction) partner.getData()).getName().equals(fplayer.getName()))
                            return; // Allowed faction partner.
                    }
                    if(!fchunk.equals(fplayer)) {
                        event.setCancelled(true);
                        sendLandAlreadyClaimedMessage(player, fchunk);
                    }
                    return;
                }
            }
            event.setCancelled(true);
            sendLandAlreadyClaimedMessage(player, fchunk);
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerBlockPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();
        Chunk chunk = event.getBlock().getChunk();
        Faction fplayer = SimpleFactions.getInstance().getFactionsManager().getFaction(player);
        Faction fchunk = SimpleFactions.getInstance().getFactionsManager().getFaction(chunk);
        if(fchunk != null){
            // Permission settings logic.
            if(fplayer == null){
                if(fchunk.getSetting(PermCategory.NEU, PermSetting.BUILD)) return;
                else if(fchunk.getSetting(PermCategory.NEU, PermSetting.PAINBUILD)){
                    player.damage(Settings.PAINBUILD_DAMAGE);
                    return;
                } else event.setCancelled(true);
            }
            else if(fchunk.equals(fplayer)){
                Role role = SimpleFactions.getInstance().getFactionsManager().getMember(player).getRole();
                switch (role){
                    case MEMBER:
                        if(fplayer.getSetting(PermCategory.MEM, PermSetting.BUILD)) return;
                        else if(fplayer.getSetting(PermCategory.MEM, PermSetting.PAINBUILD)){
                            player.damage(Settings.PAINBUILD_DAMAGE);
                            return;
                        } else {
                            event.setCancelled(true);
                            sendLandAlreadyClaimedMessage(player, fchunk);
                            return;
                        }
                    case MOD:
                        if(fplayer.getSetting(PermCategory.MOD, PermSetting.BUILD)) return;
                        else if(fplayer.getSetting(PermCategory.MOD, PermSetting.PAINBUILD)){
                            player.damage(Settings.PAINBUILD_DAMAGE);
                            return;
                        } else {
                            event.setCancelled(true);
                            sendLandAlreadyClaimedMessage(player, fchunk);
                            return;
                        }
                    default:
                        return; // Owner role.
                }
            }
            else{
                if((fchunk.getAllies().contains(fplayer) && fchunk.getSetting(PermCategory.ALL, PermSetting.BUILD))
                        || (fchunk.getEnemies().contains(fplayer) && fchunk.getSetting(PermCategory.ENE, PermSetting.BUILD))
                        || (!fchunk.getAllies().contains(fplayer) && !fchunk.getEnemies().contains(fplayer) && fchunk.getSetting(PermCategory.NEU, PermSetting.BUILD))){
                    return;
                }
                else if((fchunk.getAllies().contains(fplayer) && fchunk.getSetting(PermCategory.ALL, PermSetting.PAINBUILD))
                        || (fchunk.getEnemies().contains(fplayer) && fchunk.getSetting(PermCategory.ENE, PermSetting.PAINBUILD))
                        || (!fchunk.getAllies().contains(fplayer) && !fchunk.getEnemies().contains(fplayer) && fchunk.getSetting(PermCategory.NEU, PermSetting.PAINBUILD))){
                    player.damage(Settings.PAINBUILD_DAMAGE);
                    return;
                } else event.setCancelled(true);
            }
            // Partner logic.
            List<Partner> partnerList = fchunk.getPartners(chunk);
            if(partnerList == null){
                if(fchunk.equals(fplayer)) event.setCancelled(false);
                else {
                    event.setCancelled(true);
                    sendLandAlreadyClaimedMessage(player, fchunk);
                }
                return;
            } else{
                for(Partner partner : partnerList){
                    if(!(partner.getData() instanceof UUID)) continue;
                    if(partner.getData().equals(player.getUniqueId()))
                        return; // Allowed standalone partner.
                }
                if(!fchunk.equals(fplayer)) {
                    for(Partner partner : partnerList){
                        if(!(partner.getData() instanceof Faction)) continue;
                        if(((Faction) partner.getData()).getName().equals(fplayer.getName()))
                            return; // Allowed faction partner.
                    }
                    if(fchunk != fplayer) {
                        event.setCancelled(true);
                        sendLandAlreadyClaimedMessage(player, fchunk);
                    }
                    return;
                }
            }
            event.setCancelled(true);
            sendLandAlreadyClaimedMessage(player, fchunk);
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event){
        boolean approved = false;
        if(event.getAction() == Action.PHYSICAL && (event.getClickedBlock().getType() == Material.GOLD_PLATE || event.getClickedBlock().getType() == Material.IRON_PLATE
                || event.getClickedBlock().getType() == Material.STONE_PLATE || event.getClickedBlock().getType() == Material.WOOD_PLATE)){
            approved = true;
        }
        if(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK || approved){
            Player player = event.getPlayer();
            Chunk chunk = event.getClickedBlock().getChunk();
            Faction fplayer = SimpleFactions.getInstance().getFactionsManager().getFaction(player);
            Faction fchunk = SimpleFactions.getInstance().getFactionsManager().getFaction(chunk);

            // Guarantee function. heh.
            if(fchunk != null && fplayer != null){
                if(fchunk.equals(fplayer)){
                    Role role = SimpleFactions.getInstance().getFactionsManager().getMember(player).getRole();
                    switch (role){
                        case MEMBER:
                            if(fplayer.getSetting(PermCategory.MEM, PermSetting.BUILD)) return;
                            else {
                                event.setCancelled(true);
                                sendLandAlreadyClaimedMessage(player, fchunk);
                                return;
                            }
                        case MOD:
                            if(fplayer.getSetting(PermCategory.MOD, PermSetting.BUILD)) return;
                            else {
                                event.setCancelled(true);
                                sendLandAlreadyClaimedMessage(player, fchunk);
                                return;
                            }
                        default:
                            return; // Owner role.
                    }
                }
                else if(fchunk.getAllies().contains(fplayer) && fchunk.getSetting(PermCategory.ALL, PermSetting.BUILD)) return;
                else if(fchunk.getEnemies().contains(fplayer) && fchunk.getSetting(PermCategory.ENE, PermSetting.BUILD)) return;
                else if(!fchunk.getAllies().contains(fplayer) && !fchunk.getEnemies().contains(fplayer) && fchunk.getSetting(PermCategory.NEU, PermSetting.BUILD)) return;
            }

            if(fchunk != null){
                Block block = event.getClickedBlock();
                if(block.getType() == Material.WOOD_DOOR || block.getType() == Material.ACACIA_DOOR
                        || block.getType() == Material.BIRCH_DOOR || block.getType() == Material.DARK_OAK_DOOR
                        || block.getType() == Material.JUNGLE_DOOR || block.getType() == Material.SPRUCE_DOOR
                        || block.getType() == Material.TRAP_DOOR || block.getType() == Material.WOODEN_DOOR){
                    // Door permission logic.
                    if(fchunk.equals(fplayer)){
                        Role role = SimpleFactions.getInstance().getFactionsManager().getMember(player).getRole();
                        switch (role){
                            case MEMBER:
                                if(fplayer.getSetting(PermCategory.MEM, PermSetting.DOOR)) return;
                                else {
                                    event.setCancelled(true);
                                    sendLandAlreadyClaimedMessage(player, fchunk);
                                    return;
                                }
                            case MOD:
                                if(fplayer.getSetting(PermCategory.MOD, PermSetting.DOOR)) return;
                                else {
                                    event.setCancelled(true);
                                    sendLandAlreadyClaimedMessage(player, fchunk);
                                    return;
                                }
                            default:
                                return; // Owner role.
                        }
                    }
                    else if(fchunk.getAllies().contains(fplayer) && fchunk.getSetting(PermCategory.ALL, PermSetting.DOOR)) return;
                    else if(fchunk.getEnemies().contains(fplayer) && fchunk.getSetting(PermCategory.ENE, PermSetting.DOOR)) return;
                    else if(!fchunk.getAllies().contains(fplayer) && !fchunk.getEnemies().contains(fplayer) && fchunk.getSetting(PermCategory.NEU, PermSetting.DOOR)) return;
                    else {
                        event.setCancelled(true);
                        sendLandAlreadyClaimedMessage(player, fchunk);
                        return;
                    }
                }
                if(block.getType() == Material.STONE_BUTTON || block.getType() == Material.WOOD_BUTTON){
                    // Button permission logic.
                    if(fchunk.equals(fplayer)){
                        Role role = SimpleFactions.getInstance().getFactionsManager().getMember(player).getRole();
                        switch (role){
                            case MEMBER:
                                if(fplayer.getSetting(PermCategory.MEM, PermSetting.BUTTON)) return;
                                else {
                                    event.setCancelled(true);
                                    sendLandAlreadyClaimedMessage(player, fchunk);
                                    return;
                                }
                            case MOD:
                                if(fplayer.getSetting(PermCategory.MOD, PermSetting.BUTTON)) return;
                                else {
                                    event.setCancelled(true);
                                    sendLandAlreadyClaimedMessage(player, fchunk);
                                    return;
                                }
                            default:
                                return; // Owner role.
                        }
                    }
                    else if(fchunk.getAllies().contains(fplayer) && fchunk.getSetting(PermCategory.ALL, PermSetting.BUTTON)) return;
                    else if(fchunk.getEnemies().contains(fplayer) && fchunk.getSetting(PermCategory.ENE, PermSetting.BUTTON)) return;
                    else if(!fchunk.getAllies().contains(fplayer) && !fchunk.getEnemies().contains(fplayer) && fchunk.getSetting(PermCategory.NEU, PermSetting.BUTTON)) return;
                    else {
                        event.setCancelled(true);
                        sendLandAlreadyClaimedMessage(player, fchunk);
                        return;
                    }
                }
                if(block.getType() == Material.LEVER){
                    // Lever permission logic.
                    if(fchunk.equals(fplayer)){
                        Role role = SimpleFactions.getInstance().getFactionsManager().getMember(player).getRole();
                        switch (role){
                            case MEMBER:
                                if(fplayer.getSetting(PermCategory.MEM, PermSetting.LEVER)) return;
                                else {
                                    event.setCancelled(true);
                                    sendLandAlreadyClaimedMessage(player, fchunk);
                                    return;
                                }
                            case MOD:
                                if(fplayer.getSetting(PermCategory.MOD, PermSetting.LEVER)) return;
                                else {
                                    event.setCancelled(true);
                                    sendLandAlreadyClaimedMessage(player, fchunk);
                                    return;
                                }
                            default:
                                return; // Owner role.
                        }
                    }
                    else if(fchunk.getAllies().contains(fplayer) && fchunk.getSetting(PermCategory.ALL, PermSetting.LEVER)) return;
                    else if(fchunk.getEnemies().contains(fplayer) && fchunk.getSetting(PermCategory.ENE, PermSetting.LEVER)) return;
                    else if(!fchunk.getAllies().contains(fplayer) && !fchunk.getEnemies().contains(fplayer) && fchunk.getSetting(PermCategory.NEU, PermSetting.LEVER)) return;
                    else {
                        event.setCancelled(true);
                        sendLandAlreadyClaimedMessage(player, fchunk);
                        return;
                    }
                }
                if(block.getType() == Material.GOLD_PLATE || block.getType() == Material.IRON_PLATE
                        || block.getType() == Material.STONE_PLATE || block.getType() == Material.WOOD_PLATE){
                    // Pressure plate permission logic.
                    if(fchunk.equals(fplayer)){
                        Role role = SimpleFactions.getInstance().getFactionsManager().getMember(player).getRole();
                        switch (role){
                            case MEMBER:
                                if(fplayer.getSetting(PermCategory.MEM, PermSetting.PRESSUREPLATE)) return;
                                else {
                                    event.setCancelled(true);
                                    sendLandAlreadyClaimedMessage(player, fchunk);
                                    return;
                                }
                            case MOD:
                                if(fplayer.getSetting(PermCategory.MOD, PermSetting.PRESSUREPLATE)) return;
                                else {
                                    event.setCancelled(true);
                                    sendLandAlreadyClaimedMessage(player, fchunk);
                                    return;
                                }
                            default:
                                return; // Owner role.
                        }
                    }
                    else if(fchunk.getAllies().contains(fplayer) && fchunk.getSetting(PermCategory.ALL, PermSetting.PRESSUREPLATE)) return;
                    else if(fchunk.getEnemies().contains(fplayer) && fchunk.getSetting(PermCategory.ENE, PermSetting.PRESSUREPLATE)) return;
                    else if(!fchunk.getAllies().contains(fplayer) && !fchunk.getEnemies().contains(fplayer) && fchunk.getSetting(PermCategory.NEU, PermSetting.PRESSUREPLATE)) return;
                    else {
                        event.setCancelled(true);
                        sendLandAlreadyClaimedMessage(player, fchunk);
                        return;
                    }
                }
                // Partner logic.
                List<Partner> partnerList = fchunk.getPartners(chunk);
                if(partnerList == null){
                    if(fchunk.equals(fplayer)) event.setCancelled(false);
                    else {
                        event.setCancelled(true);
                        sendLandAlreadyClaimedMessage(player, fchunk);
                    }
                    return;
                } else{
                    for(Partner partner : partnerList){
                        if(!(partner.getData() instanceof UUID)) continue;
                        if(partner.getData().equals(player.getUniqueId()))
                            return; // Allowed standalone partner.
                    }
                    if(fplayer != null){
                        for(Partner partner : partnerList){
                            if(!(partner.getData() instanceof Faction)) continue;
                            if(((Faction) partner.getData()).getName().equals(fplayer.getName()))
                                return; // Allowed faction partner.
                        }
                        if(!fchunk.equals(fplayer)) {
                            event.setCancelled(true);
                            sendLandAlreadyClaimedMessage(player, fchunk);
                        }
                        return;
                    }
                }
                event.setCancelled(true);
                sendLandAlreadyClaimedMessage(player, fchunk);
            }
        }
    }

    private void sendLandAlreadyClaimedMessage(Player player, Faction faction){
        player.sendMessage(Chat.format(Settings.LAND_CLAIMED.replace("{faction}", faction.getName())));
    }
}
