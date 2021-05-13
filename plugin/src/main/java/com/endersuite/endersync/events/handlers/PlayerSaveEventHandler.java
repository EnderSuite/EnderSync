package com.endersuite.endersync.events.handlers;

import com.endersuite.endersync.EnderSyncAPI;
import com.endersuite.endersync.events.core.PlayerSaveEvent;
import com.endersuite.libcore.strfmt.Level;
import com.endersuite.libcore.strfmt.StrFmt;

/**
 * Handles {@link PlayerSaveEvent}s and calls appropriate {@link EnderSyncAPI} methods.
 *
 * @author Maximilian Vincent Heidenreich
 * @since 10.05.21
 */
public class PlayerSaveEventHandler {

    public static void onPlayerSaveEvent(PlayerSaveEvent event) {
        new StrFmt("{prefix} Handling " + event).setLevel(Level.DEBUG).toLog();

        if (event.getSaveModules() == null)
            EnderSyncAPI.getInstance().savePlayerModules(event.getPlayer());
        else
            EnderSyncAPI.getInstance().savePlayerModules(event.getPlayer(), event.getSaveModules());

    }

}
