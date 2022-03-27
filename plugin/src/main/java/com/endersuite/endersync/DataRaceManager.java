package com.endersuite.endersync;

import com.endersuite.database.mysql.Row;
import lombok.Getter;
import lombok.Synchronized;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO: Add docs
 *
 * @author Maximilian Vincent Heidenreich
 * @since 17.05.21
 */
public class DataRaceManager {

    // ======================   VARS

    @Getter
    private final Map<UUID, CompletableFuture<Map<String, Row>>> pendingDataRaces;


    // ======================   CONSTRUCTOR

    public DataRaceManager() {
        this.pendingDataRaces = new ConcurrentHashMap<>();
    }


    // ======================   BUSINESS LOGIC

    @Synchronized
    public boolean addDataRace(UUID playerId, CompletableFuture<Map<String, Row>> callback) {

        // RET: Data race with same id already pending
        if (this.pendingDataRaces.containsKey(playerId)) return false;

        this.pendingDataRaces.put(playerId, callback);
        return true;
    }

    @Synchronized   // TODO: Synchronize on playerID instead of whole map / class // TODO: Add timeouts
    public void completeDataRace(UUID playerId, Map<String, Row> data) {
        CompletableFuture<Map<String, Row>> callback = this.pendingDataRaces.get(playerId);

        // RET: No pending race
        if (callback == null) return;

        this.pendingDataRaces.remove(playerId);
        callback.complete(data);

    }

    // ======================   HELPERS


}
