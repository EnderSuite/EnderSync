package com.endersuite.endersync.events.handlers;

import com.endersuite.endersync.EnderSyncAPI;
import com.endersuite.endersync.events.core.PlayerSynchronizeEvent;
import com.endersuite.libcore.strfmt.Level;
import com.endersuite.libcore.strfmt.StrFmt;

/**
 * Handles {@link PlayerSynchronizeEvent}s and calls appropriate {@link EnderSyncAPI} methods.
 *
 * @author Maximilian Vincent Heidenreich
 * @since 10.05.21
 */
public class PlayerSynchronizeEventHandler {

    public static void onPlayerSyncEvent(PlayerSynchronizeEvent event) {
        new StrFmt("{prefix} Handling " + event).setLevel(Level.DEBUG).toConsole();

        if (event.getSyncModules() == null)
            EnderSyncAPI.getInstance().syncPlayerModules(event.getPlayer());
        else
            EnderSyncAPI.getInstance().syncPlayerModules(event.getPlayer(), event.getSyncModules());
    }

}
