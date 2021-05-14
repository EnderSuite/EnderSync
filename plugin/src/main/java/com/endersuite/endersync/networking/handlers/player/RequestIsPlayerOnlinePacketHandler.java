package com.endersuite.endersync.networking.handlers.player;

import com.endersuite.endersync.Plugin;
import com.endersuite.endersync.packets.core.player.RequestIsPlayerOnlinePacket;
import com.endersuite.endersync.packets.core.player.ResponseIsPlayerOnlinePacket;
import com.endersuite.libcore.strfmt.Level;
import com.endersuite.libcore.strfmt.StrFmt;
import com.endersuite.packify.transmission.Transmission;
import org.bukkit.Bukkit;

/**
 * TODO: Add docs
 *
 * @author Maximilian Vincent Heidenreich
 * @since 11.05.21
 */
public class RequestIsPlayerOnlinePacketHandler {

    public static void handle(RequestIsPlayerOnlinePacket packet) {

        try {
            ResponseIsPlayerOnlinePacket resp = new ResponseIsPlayerOnlinePacket(
                    packet,
                    Bukkit.getPlayer(packet.getPlayerUUID()) != null,
                    Plugin.getPlugin().getNetworkManager().getJChannel().getName()
            );
            Transmission.newBuilder(resp)
                    .to(packet.getSender())
                    .build()
                    .transmit();
        } catch (Exception e) {
            e.printStackTrace();
            new StrFmt("Could not respond to " + packet + "!", e).setLevel(Level.ERROR).toLog();
        }
    }

}
