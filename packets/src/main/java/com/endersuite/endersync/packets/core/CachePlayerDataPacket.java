package com.endersuite.endersync.packets.core;

import com.endersuite.database.mysql.Row;
import com.endersuite.packify.packets.APacket;
import lombok.Getter;

import java.util.Map;

/**
 * Transports player data to other nodes in the cluster which will cache the playerData.
 *
 * @author Maximilian Vincent Heidenreich
 * @since 10.05.21
 */
public class CachePlayerDataPacket extends APacket {

    // ======================   VARS

    /**
     * The player's uuid to which the data is bound.
     */
    @Getter
    private final String playerUUID;

    /**
     * The data itself.
     */
    @Getter
    private final Map<String, Row> playerData;


    // ======================   CONSTRUCTOR

    public CachePlayerDataPacket(String playerUUID, Map<String, Row> playerData) {
        super();
        this.playerUUID = playerUUID;
        this.playerData = playerData;
    }

}
