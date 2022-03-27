package com.endersuite.endersync.packets.core.player;

import com.endersuite.packify.packets.ACollectablePacket;
import lombok.Getter;

import java.util.UUID;

/**
 * TODO: Add docs
 *
 * @author Maximilian Vincent Heidenreich
 * @since 11.05.21
 */
public class ResponseIsPlayerOnlinePacket extends ACollectablePacket {

    // ======================   VARS

    /**
     * The player who was checked.
     */
    @Getter
    private final UUID playerUUID;

    /**
     * Whether the player is online.
     */
    @Getter
    private final boolean online;

    /**
     * The display name of the node as specified in its config.
     */
    @Getter
    private final String nodeDisplayName;


    // ======================   CONSTRUCTOR

    public ResponseIsPlayerOnlinePacket(RequestIsPlayerOnlinePacket requestPacket, boolean online, String nodeDisplayName) {
        super(requestPacket);
        this.playerUUID = requestPacket.getPlayerUUID();
        this.online = online;
        this.nodeDisplayName = nodeDisplayName;
    }


}
