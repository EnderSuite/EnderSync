package com.endersuite.endersync.networking.handlers.player;

import com.endersuite.endersync.Plugin;
import com.endersuite.endersync.networking.NetworkManager;
import com.endersuite.endersync.networking.packets.core.player.RequestIsPlayerOnlinePacket;
import com.endersuite.endersync.networking.packets.core.player.ResponseIsPlayerOnlinePacket;
import com.endersuite.libcore.strfmt.Level;
import com.endersuite.libcore.strfmt.StrFmt;
import org.bukkit.Bukkit;

/**
 * TODO: Add docs
 *
 * @author Maximilian Vincent Heidenreich
 * @since 11.05.21
 */
public class RequestIsPlayerOnlinePacketHandler {

    private final static NetworkManager networkManager = NetworkManager.getInstance();

    public static void handle(RequestIsPlayerOnlinePacket packet) {

        Bukkit.getPlayer(packet.getPlayerUUID()).isOnline();

        new StrFmt("resp to sender: " + packet.getSender()).setLevel(Level.INFO).toConsole();
        try {
            networkManager.sendRaw(packet.getSender(), new ResponseIsPlayerOnlinePacket(
                    packet,
                    Bukkit.getPlayer(packet.getPlayerUUID()).isOnline(),
                    networkManager.getJChannel().getName()
            ));
        } catch (Exception exception) {
            Plugin.getPlugin().panic("sa", exception);
            exception.printStackTrace();
        }
    }

}
