package com.endersuite.endersync.networking.packets.core.player;

import com.endersuite.endersync.networking.packets.ACollectableRequestPacket;
import lombok.Getter;

import java.util.UUID;

/**
 * Transports a request to nodes to check whether a player specified by a UUID is online.
 * If he is, the node should respond with a ResponseIsPlayerOnlinePacket TODO: link.
 *
 * @author Maximilian Vincent Heidenreich
 * @since 11.05.21
 */
public class RequestIsPlayerOnlinePacket extends ACollectableRequestPacket<ResponseIsPlayerOnlinePacket> {

    // ======================   VARS

    /**
     * The UUID of the player to check.
     */
    @Getter
    private final UUID playerUUID;


    // ======================   CONSTRUCTOR

    public RequestIsPlayerOnlinePacket(UUID playerUUID) {
        super();
        this.playerUUID = playerUUID;
    }

}
