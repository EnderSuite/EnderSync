package com.endersuite.endersync.networking.handlers;

import com.endersuite.endersync.networking.NetworkManager;
import com.endersuite.endersync.networking.packets.ACollectableRequestPacket;
import com.endersuite.endersync.networking.packets.ACollectableResponsePacket;
import com.endersuite.endersync.networking.packets.APacket;
import com.endersuite.libcore.strfmt.StrFmt;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO: Add docs
 *
 * @author Maximilian Vincent Heidenreich
 * @since 11.05.21
 */
public class CollectablePacketHandler {

    // ======================   VARS

    @Getter
    private static final Map<UUID, CompletableFuture<List<? extends ACollectableResponsePacket>>> collectionCallbacks = new ConcurrentHashMap<>();

    /**
     * Store all received collection packets.
     * -> Will be passed into a collectionCallback once all nodes have responded / timeout.
     */
    @Getter
    private static final Map<UUID, List<ACollectableResponsePacket>> collectionPackets = new ConcurrentHashMap<>();

    /**
     * Stores response packets after a request has been sent using {@link NetworkManager#broadcastCollect(ACollectableRequestPacket)}.
     * -> After all packets have been received call callback identified by same id.
     */
    @Getter
    private static final Map<UUID, List<APacket>> broadcastResponseCollections = new ConcurrentHashMap<>();

    // ======================   BUSINESS LOGIC

    public static void handleCollectablePacket(ACollectableResponsePacket packet) {

        new StrFmt("Recv cid: " + packet.getCollectId()).toConsole();

        if (!collectionPackets.containsKey(packet.getCollectId()))
            collectionPackets.put(packet.getCollectId(), new ArrayList<>());

        new StrFmt(collectionPackets.get(packet.getCollectId()).toString()).toConsole();
        collectionPackets.get(packet.getCollectId()).add(packet);

        if (collectionPackets.get(packet.getCollectId()).size() >= NetworkManager.getInstance().getJChannel().getView().getMembers().size()) {
            List<ACollectableResponsePacket> responses = collectionPackets.get(packet.getCollectId());
            collectionPackets.remove(packet.getCollectId());
            new StrFmt(collectionCallbacks.toString()).toConsole();
            CompletableFuture<List<? extends ACollectableResponsePacket>> abc = collectionCallbacks.get(packet.getCollectId());
            new StrFmt("callb: " + abc.toString());
            abc.complete(responses);
        }

    }

}
