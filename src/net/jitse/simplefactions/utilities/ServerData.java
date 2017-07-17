package net.jitse.simplefactions.utilities;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.jitse.simplefactions.SimpleFactions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jitse on 13-11-2016.
 */
public class ServerData implements PluginMessageListener {

    private final Map<String, Integer> data = new HashMap<>();
    private final SimpleFactions plugin;

    public ServerData(SimpleFactions plugin){
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", this);
        this.plugin  = plugin;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) return;
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("PlayerCount")) {
            String server = in.readUTF();
            int playercount = in.readInt();
            if(data.containsKey(server)) data.remove(server);
            data.put(server, playercount);
        }
    }

    public void requestPlayerCount(String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerCount");
        out.writeUTF(server);
        Bukkit.getServer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }

    public int getCachedPlayerCount(String name) {
        if(data.containsKey(name)) return data.get(name);
        return 0;
    }
}
