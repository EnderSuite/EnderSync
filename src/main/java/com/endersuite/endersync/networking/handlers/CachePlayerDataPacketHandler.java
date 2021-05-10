package com.endersuite.endersync.networking.handlers;

import com.endersuite.endersync.EnderSyncAPI;
import com.endersuite.endersync.networking.packets.core.CachePlayerDataPacket;

import java.util.UUID;

/**
 * Handles {@link CachePlayerDataPacket}s and populates the local cache with the received data.
 *
 * @author Maximilian Vincent Heidenreich
 * @since 10.05.21
 */
public class CachePlayerDataPacketHandler {

    private final static EnderSyncAPI api = EnderSyncAPI.getInstance();

    public static void handleCachePlayerDataPacket(CachePlayerDataPacket packet) {
        api.cachePlayerData(UUID.fromString(packet.getPlayerUUID()), packet.getPlayerData());
    }

}
