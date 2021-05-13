package com.endersuite.endersync.networking.handlers.player;

import com.endersuite.endersync.Plugin;
import com.endersuite.endersync.packets.core.player.RequestIsPlayerOnlinePacket;
import com.endersuite.endersync.packets.core.player.ResponseIsPlayerOnlinePacket;
import com.endersuite.packify.NetworkManager;
import com.endersuite.packify.transmission.Transmission;
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

        try {
            ResponseIsPlayerOnlinePacket resp = new ResponseIsPlayerOnlinePacket(
                    packet,
                    Bukkit.getPlayer(packet.getPlayerUUID()) != null,
                    networkManager.getJChannel().getName()
            );
            Transmission.fromPacket(resp)
                    .to(packet.getSender())
                    .transmit();
        } catch (Exception exception) {
            Plugin.getPlugin().panic("sa", exception);
            exception.printStackTrace();
        }
    }

}
