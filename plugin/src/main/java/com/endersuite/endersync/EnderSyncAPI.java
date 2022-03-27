package com.endersuite.endersync;

import com.endersuite.database.mysql.ResultHandler;
import com.endersuite.database.mysql.Row;
import com.endersuite.endersync.module.ASynchronizedDataModule;
import com.endersuite.endersync.module.ASynchronizedPlayerModule;
import com.endersuite.endersync.packets.core.CachePlayerDataPacket;
import com.endersuite.libcore.strfmt.Level;
import com.endersuite.libcore.strfmt.Status;
import com.endersuite.libcore.strfmt.StrFmt;
import com.endersuite.packify.transmission.Transmission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Public EnderSync API that is also used internally.
 * TODO: Maybe move singletons into plugin and plugin reference to here? And Make all internal things protected?
 *
 * @author Maximilian Vincent Heidenreich
 * @since 10.05.21
 */
public class EnderSyncAPI {

    // ======================   VARS

    /**
     * The EnderSyncAPI singleton.
     */
    private static EnderSyncAPI instance;



    // ======================   CONSTRUCTOR

    private EnderSyncAPI() {}


    // ======================   BUSINESS LOGIC

    // TODO: Use collection of module names? Currently the api is unsafe and accept random modules

    /*public void saveDataModules() {}
    public void saveDataModules(Collection<ASynchronizedDataModule> dataSyncModules) {}

    public void syncDataModules() {}
    public void syncDataModules(Collection<ASynchronizedDataModule> dataSyncModules) {}

    public void savePlayerModules(Player player) {
        List<ASynchronizedPlayerModule> modules = Plugin.getPlugin().getModuleManager().getActivePlayerModules();
        savePlayerModules(player, modules);
    }
    public void savePlayerModules(Player player, Collection<ASynchronizedPlayerModule> playerSyncModules) {

        new StrFmt("{prefix} §7Saving data").setStatus(Status.PROGRESS).toPlayer(player);

        System.out.println("locked: " + isPlayerLocked(player.getUniqueId()));
        if (isPlayerLocked(player.getUniqueId())) {}

        lockPlayer(player.getUniqueId());

        // Send db update

        // Build map to broadcast
        Map<String, Row> playerData = new HashMap<>();
        try {
            for (ASynchronizedPlayerModule module : playerSyncModules)
                playerData.put(module.getTableName(), module.toRow(player));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // Broadcast player data
        try {
            CachePlayerDataPacket packet = new CachePlayerDataPacket(player.getUniqueId().toString(), playerData);
            Transmission.newBuilder(packet)
                    .broadcast(true)
                    .build()
                    .transmit();
        } catch (Exception exception) {
            exception.printStackTrace();
            // TODO: Print warn "Will fallback to database"
        }

        for (ASynchronizedPlayerModule module : playerSyncModules)
            module.save(player);

        new StrFmt("{prefix} §7Saved!").setStatus(Status.GOOD).toPlayer(player);
        //Bukkit.getScheduler().runTask(Plugin.getPlugin(), () -> player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1));

    }

    public void syncPlayerModules(Player player) {
        List<ASynchronizedPlayerModule> modules = Plugin.getPlugin().getModuleManager().getActivePlayerModules();
        syncPlayerModules(player, modules);
    }
    public void syncPlayerModules(Player player, Collection<ASynchronizedPlayerModule> playerSyncModules) {

        new StrFmt("{prefix} §7Synchronizing data").setStatus(Status.PROGRESS).toPlayer(player);

        // CACHED
        Map<String, Row> cachedPlayerData = Plugin.getPlugin().getPlayerDataCache().getIfPresent(player.getUniqueId());
        if (cachedPlayerData != null) {
            new StrFmt("{prefix} Cache §ahit§r for '§e" + player.getUniqueId() + "§r'").setLevel(Level.DEBUG).toLog();
            Plugin.getPlugin().getPlayerDataCache().invalidate(player.getUniqueId());
            processPlayerSync(player, cachedPlayerData, playerSyncModules);
            return;
        }

        // ! CACHED
        new StrFmt("{prefix} Cache §cmiss§r for '§e" + player.getUniqueId() + "§r'").setLevel(Level.DEBUG).toLog();

        // START DATA RACE
        final CompletableFuture<Map<String, Row>> cacheRecvCallback = new CompletableFuture<>();
        final CompletableFuture<Map<String, Row>> databaseRecvCallback = new CompletableFuture<>();


        Plugin.getPlugin().getCacheCallbacks().put(player.getUniqueId(), cacheRecvCallback);

        final CompletableFuture<Map<String, Row>> dataRaceCallback = new CompletableFuture<>();

        cacheRecvCallback.acceptEither(databaseRecvCallback, data -> {
            new StrFmt("Data race completed with data: %s", data).setLevel(Level.TRACE).toLog();
            processPlayerSync(player, data, playerSyncModules);
        });
        //dataRaceCallback.thenAccept(data -> {
        //    new StrFmt("Data race completed with data: %s", data).setLevel(Level.TRACE).toLog();
        //    processPlayerSync(player, data, playerSyncModules);
        //});
        //Plugin.getPlugin().getDataRaceManager().addDataRace(player.getUniqueId(), dataRaceCallback);

        // DB QUERIES
        if (!performDBQueries(player, playerSyncModules)) {
            scheduleDBQueries(player, playerSyncModules);
        }

    }

    private void scheduleDBQueries(final Player player, final Collection<ASynchronizedPlayerModule> playerSyncModules) {
        new StrFmt("{prefix} §7Waiting for save to be completed! Retrying in 1 second").setStatus(Status.PROGRESS).toPlayer(player);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getPlugin(), () -> {
            new StrFmt("Running sched foo check!").setLevel(Level.DEBUG).toLog();
            if (!performDBQueries(player, playerSyncModules))
                scheduleDBQueries(player, playerSyncModules);
        }, 24*3);
    }

    private boolean performDBQueries(Player player, Collection<ASynchronizedPlayerModule> playerSyncModules) {
        if (isPlayerLocked(player.getUniqueId())) return false;


        final List<CompletableFuture<Row>> futures = new ArrayList<>();
        for (ASynchronizedPlayerModule module : playerSyncModules) {
            futures.add(module.getRowFromDatabase(player));
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).thenAccept(ignored -> {
            final Map<String, Row> results = futures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toMap(Row::getTableName, e -> e));
            Plugin.getPlugin().getDataRaceManager().completeDataRace(player.getUniqueId(), results);
        });
        return true;
    }

    private void processPlayerSync(Player player, Map<String, Row> data) {
        //List<AbstractSynchronizedPlayerModule> modules = ModuleManager.getInstance().getActivePlayerModules().stream()
        //        .filter(m -> m.config);
        //processPlayerSync(player, data, modules);
    }
    private void processPlayerSync(Player player, Map<String, Row> data, Collection<ASynchronizedPlayerModule> modules) {
        for (ASynchronizedPlayerModule module : modules) {
            System.out.println(module.toString());
            boolean success = false;
            try {
                success = module.synchronize(player, data.get(module.getTableName()));     // TODO: Inactive module not in cahced data! // Decide on modName/Tablename
            }
            catch (RuntimeException e) {
                if (module.isCritical()) {
                    e.printStackTrace();
                    new StrFmt("Could not synchronize critical module " + module + "due to: ", e).setLevel(Level.ERROR).toLog();
                    // TODO: Do how config
                    player.kickPlayer(new StrFmt("{prefix} Could not synchronize §e%s§r! Please reconnect!", module.getName()).toString());
                }
            }
            if (!success) {
                Bukkit.getScheduler().runTask(Plugin.getPlugin(), () -> {
                            String msg = new StrFmt("{prefix} Could not synchronize §e%s§r! Please reconnect!", module.getName()).toString();
                            player.kickPlayer(msg);
                });
            }
        }
        unlockPlayer(player.getUniqueId());

        new StrFmt("{prefix} §7Synchronized!").setStatus(Status.GOOD).toPlayer(player);
        //Bukkit.getScheduler().runTask(Plugin.getPlugin(), () -> player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1));
    }


    public void cachePlayerData(UUID playerUUID, Map<String, Row> data) {
        new StrFmt("{prefix} Caching player data for '§e" + playerUUID + "§r'").setLevel(Level.DEBUG).toLog();
        new StrFmt("{prefix} Cache data: " + data.toString()).setLevel(Level.TRACE).toLog();

        Plugin.getPlugin().getPlayerDataCache().put(playerUUID, data);
    }*/


    // ======================   PLAYER LOCKS

    /*public boolean isPlayerLocked(UUID playerId) {
        if (!Plugin.getPlugin().getDb().isConnected())
            return false;
        ResultHandler res = Plugin.getPlugin().getDb().execQuery("SELECT locked FROM player_locks WHERE uuid=?", playerId.toString());
        if (res.getRowList().size() <= 0) return false;
        return res.getRowList().get(0).getBoolean("locked");
    }

    public boolean lockPlayer(UUID playerId) {
        if (!Plugin.getPlugin().getDb().isConnected())
            return true;
        Plugin.getPlugin().getDb().execUpdate("INSERT INTO player_locks (uuid, locked) VALUES(?, ?) ON DUPLICATE KEY UPDATE locked=?", playerId.toString(), true, true);
        // TODO: add ret false if not locked
        return true;
    }

    public boolean unlockPlayer(UUID playerId) {
        if (!Plugin.getPlugin().getDb().isConnected())
            return true;
        Plugin.getPlugin().getDb().execUpdate("INSERT INTO player_locks (uuid, locked) VALUES(?, ?) ON DUPLICATE KEY UPDATE locked=?", playerId.toString(), false, false);
        // TODO: add ret false if not unlocked
        return true;
    }*/



    // ======================   HELPERS

    /**
     * Returns the ModuleManager singleton instance.
     * Note: Also creates one if none exists.
     *
     * @return The ModuleManager instance.
     */
    public static EnderSyncAPI getInstance() {
        if (EnderSyncAPI.instance == null)
            EnderSyncAPI.instance = new EnderSyncAPI();

        return EnderSyncAPI.instance;
    }

}
