package com.endersuite.endersync.modules;

import com.endersuite.database.mysql.Row;
import lombok.Getter;
import lombok.Setter;

/**
 * Abstract structure every Module has to follow.
 * Note: You should probably use {@link AbstractSynchronizedPlayerModule} or {@link AbstractSynchronizedPlayerModule}!
 *
 * // TODO: JSON CONFIG?
 *
 * @author Maximilian Vincent Heidenreich
 * @since 09.05.21
 */
public abstract class AbstractSynchronizedModule {
    
    // ======================   VARS

    /**
     * The unique name of the module (Also used for db table names).
     */
    @Getter
    private String name;

    /**
     * Whether the module is critical for a successful synchronization.
     * -> If true and the synchronization method fails (via falsy return / RuntimeException), the plugin will
     *    perform XXX action as configured inside the config (e.g. kick the player).
     */
    @Getter
    @Setter
    private boolean critical;

    /**
     * Whether the plugin should treat this modules as a raw module -> Don't perform TODO: impl
     */
    @Getter
    @Setter
    private boolean raw;

    
    // ======================   CONSTRUCTOR

    /**
     * Creates a new AbstractSyncModule with a given name.
     * Note: This is where you should initialize / migrate your db structure!
     *
     * @param name
     *          The name of the module
     */
    protected AbstractSynchronizedModule(String name) {
        this.name = name;
        this.critical = false;
        this.raw = false;
    }


    // ======================   BUSINESS LOGIC



    
    // ======================   HELPERS

    /**
     * Returns the db table name using the default format.
     * @return
     *          The table name
     */
    public String getTableName() {
        return "esmod_" + name;
    }

    
}
