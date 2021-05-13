package com.endersuite.endersync.events.core;

import com.endersuite.endersync.modules.ASynchronizedPlayerModule;
import de.maximilianheidenreich.jeventloop.events.AbstractEvent;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Indicates that specific modules should be saved for a player.
 *
 * @author Maximilian Vincent Heidenreich
 * @since 10.05.21
 */
public class PlayerSaveEvent extends AbstractEvent<Void> {

    // ======================   VARS

    /**
     * The player to save.
     */
    @Getter
    private final Player player;

    /**
     * The modules which should be saved.
     * Note: null = all available modules
     */
    @Getter
    private final Collection<ASynchronizedPlayerModule> saveModules;


    // ======================   CONSTRUCTOR

    public PlayerSaveEvent(Player player) {
        this(player, null);
    }

    public PlayerSaveEvent(Player player, Collection<ASynchronizedPlayerModule> saveModules) {
        super((byte) 2);
        this.player = player;
        this.saveModules = saveModules;
    }

}
