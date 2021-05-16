package com.endersuite.endersync;

import com.endersuite.database.mysql.Row;
import com.endersuite.endersync.module.ASynchronizedDataModule;
import com.endersuite.endersync.module.ASynchronizedPlayerModule;
import com.endersuite.endersync.packets.core.CachePlayerDataPacket;
import com.endersuite.libcore.strfmt.Level;
import com.endersuite.libcore.strfmt.StrFmt;
import com.endersuite.packify.transmission.Transmission;
import org.bukkit.entity.Player;

import java.util.*;

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

    public void saveDataModules() {}
    public void saveDataModules(Collection<ASynchronizedDataModule> dataSyncModules) {}

    public void syncDataModules() {}
    public void syncDataModules(Collection<ASynchronizedDataModule> dataSyncModules) {}

    public void savePlayerModules(Player player) {
        List<ASynchronizedPlayerModule> modules = Plugin.getPlugin().getModuleManager().getActivePlayerModules();
        savePlayerModules(player, modules);
    }
    public void savePlayerModules(Player player, Collection<ASynchronizedPlayerModule> playerSyncModules) {

        // Send db update

        Map<String, Row> playerData = new HashMap<>();

        try {
            for (ASynchronizedPlayerModule module : playerSyncModules)
                playerData.put(module.getName(), module.toRow(player));
        }
        catch (Exception e) {
            e.printStackTrace();
        }


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

    }

    public void syncPlayerModules(Player player) {
        List<ASynchronizedPlayerModule> modules = Plugin.getPlugin().getModuleManager().getActivePlayerModules();
        syncPlayerModules(player, modules);
    }
    public void syncPlayerModules(Player player, Collection<ASynchronizedPlayerModule> playerSyncModules) {

        // Send db select

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

        // -> Start playerData race
            // -> On callback run sync



    }

    private void processPlayerSync(Player player, Map<String, Row> data) {
        //List<AbstractSynchronizedPlayerModule> modules = ModuleManager.getInstance().getActivePlayerModules().stream()
        //        .filter(m -> m.config);
        //processPlayerSync(player, data, modules);
    }
    private void processPlayerSync(Player player, Map<String, Row> data, Collection<ASynchronizedPlayerModule> modules) {
        for (ASynchronizedPlayerModule module : modules) {
            boolean success = false;
            try {
                success = module.synchronize(player, data.get(module.getName()));     // TODO: Inactive module not in cahced data!
            }
            catch (RuntimeException e) {
                if (module.isCritical()) {
                    e.printStackTrace();
                    new StrFmt("Could not synchronize critical module " + module + "due to: ", e).setLevel(Level.ERROR).toLog();
                    // TODO: Do how config
                }
            }
        }
    }


    public void cachePlayerData(UUID playerUUID, Map<String, Row> data) {
        new StrFmt("{prefix} Caching player data for '§e" + playerUUID + "§r'").setLevel(Level.DEBUG).toLog();
        new StrFmt("{prefix} Cache data: " + data.toString()).setLevel(Level.TRACE).toLog();

        Plugin.getPlugin().getPlayerDataCache().put(playerUUID, data);
    }


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
