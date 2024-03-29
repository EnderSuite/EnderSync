package com.endersuite.endersync.event.core;

import com.endersuite.endersync.module.ASynchronizedPlayerModule;
import de.maximilianheidenreich.jeventloop.events.AbstractEvent;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Indicates that specific modules should be synchronized for a player.
 *
 * @author Maximilian Vincent Heidenreich
 * @since 10.05.21
 */
public class PlayerSynchronizeEvent extends AbstractEvent<Void> {

    // ======================   VARS

    /**
     * The player to synchronize.
     */
    @Getter
    private final Player player;

    /**
     * The modules which should be synchronized.
     * Note: null = all available modules
     */
    @Getter
    private final Collection<ASynchronizedPlayerModule> syncModules;


    // ======================   CONSTRUCTOR

    public PlayerSynchronizeEvent(Player player) {
        this(player, null);
    }

    public PlayerSynchronizeEvent(Player player, Collection<ASynchronizedPlayerModule> syncModules) {
        super((byte) 4);
        this.player = player;
        this.syncModules = syncModules;
    }

}
