package com.endersuite.endersync.modules;

import com.endersuite.database.mysql.Row;
import com.endersuite.database.mysql.builder.QueryBuilder;

import java.util.concurrent.TimeUnit;

/**
 * TODO: Add docs
 *
 * @author Maximilian Vincent Heidenreich
 * @since 09.05.21
 */
public abstract class ASynchronizedDataModule extends ASynchronizedModule {

    // ======================   VARS

    private final long saveInterval;
    private final TimeUnit saveIntervalUnit;

    private final long syncInterval;
    private final TimeUnit syncIntervalUnit;


    // ======================   CONSTRUCTOR

    public ASynchronizedDataModule(String name, long saveInterval, TimeUnit saveIntervalUnit, long syncInterval, TimeUnit syncIntervalUnit) {
        super(name);
        this.saveInterval = saveInterval;
        this.saveIntervalUnit = saveIntervalUnit;
        this.syncInterval = syncInterval;
        this.syncIntervalUnit = syncIntervalUnit;
    }


    // ======================   BUSINESS LOGIC

    /**
     * Synchronize something with the given data.
     * Note: When implementing this method, run any data transformations ... before calling
     * {@code Bukkit.getScheduler().runTask(() -> {...}))} to run Bukkit specific stuff inside the main thread.
     *
     * @param data
     *          The data to use for the synchronization
     * @return
     *          {@code true} if the synchronization was successful |{@code false} if not
     */
    public abstract boolean synchronize(Row data);  //TODO: catch runtime execptions and call failsave

    /**
     * Saves the current data of something to the database.
     *
     * @param queryBuilder
     *          The builder used to build the update query
     * @return
     *          {@code true} if the save was successful | {@code false} if not
     */
    public abstract boolean save(QueryBuilder queryBuilder);


    // ======================   HELPERS


}
