package com.endersuite.endersync.module;

import lombok.Getter;
import lombok.Setter;

/**
 * Abstract structure every Module has to follow.
 * Note: You should probably use {@link ASynchronizedPlayerModule} or {@link ASynchronizedPlayerModule}!
 *
 * // TODO: JSON CONFIG?
 *
 * @author Maximilian Vincent Heidenreich
 * @since 09.05.21
 */
public abstract class ASynchronizedModule {
    
    // ======================   VARS

    /**
     * The unique name of the module (Also used for db table names).
     */
    @Getter
    private final String name;

    /**
     * Whether the module is critical for a successful synchronization.
     * -> If true and the synchronization method fails (via falsy return / RuntimeException), the plugin will
     *    perform XXX action as configured inside the config (e.g. kick the player).
     */
    @Getter
    @Setter
    private boolean critical;

    /*@Getter
    @Setter
    private Class<? extends FeaturesJsonConfiguration.ModuleConfiguration> moduleConfigurationClass;
    public FeaturesJsonConfiguration.ModuleConfiguration moduleConfiguration;
*/

    // ======================   CONSTRUCTOR

    /**
     * Creates a new AbstractSyncModule with a given name.
     *
     * @param name
     *          The name of the module
     */
    protected ASynchronizedModule(String name) {
        this.name = name;
        this.critical = false;
    }


    // ======================   BUSINESS LOGIC

    /**
     * Initialized the database table of a given module.
     *
     * @return {@code true} if everything succeeded and the plugin can continue | {@code false} if not
     */
    public abstract boolean setupDatabase();


    // ======================   HELPERS

    @Override
    public String toString() {
        return String.format("Module(%s)", this.name);
    }

    /**
     * Returns the db table name using the default format.
     * @return
     *          The table name
     */
    public String getTableName() {
        return "mod_" + this.name;
    }


    /*public <T> T getModuleConfig(Class<T> clazz) {
        return clazz.cast(this.moduleConfiguration);
    }*/
    
}
