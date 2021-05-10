package com.endersuite.endersync.modules;

import com.endersuite.database.mysql.Row;
import com.endersuite.database.mysql.builder.QueryBuilder;
import org.bukkit.entity.Player;

/**
 * Encapsulates some player (uuid) related data that can be synchronized across cluster nodes.
 *
 * TODO: doc: make sure to impl ...
 *
 * @author Maximilian Vincent Heidenreich
 * @since 09.05.21
 */
public abstract class AbstractSynchronizedPlayerModule extends AbstractSynchronizedModule {

    // ======================   VARS

    // ======================   CONSTRUCTOR

    /**
     * Creates a new AbstractSyncModule with a given name.
     * Note: This is where you should initialize / migrate your db structure!
     *
     * @param name
     *          The name of the module
     */
    public AbstractSynchronizedPlayerModule(String name) {
        super(name);
    }


    // ======================   BUSINESS LOGIC

    /**
     * Synchronizes the player with provided data.
     * Note: When implementing this method, run any data transformations ... before calling
     * {@code Bukkit.getScheduler().runTask(() -> {...}))} to update the player inside the main thread.
     *
     * @param player
     *          The player which should be synchronized
     * @param data
     *          The data to use for the synchronization
     * @return
     *          {@code true} if the synchronization was successful | {@code false} if not
     */
    public abstract boolean synchronize(Player player, Row data);  //TODO: catch runtime execptions and call failsave

    /**
     * Saves the current data of the player to the database.
     * Note: Should call
     * @param player
     *          The player to save
     * @param queryBuilder
     *          The builder used to build the update query
     * @return
     *          {@code true} if the save was successful | {@code false} if not
     */
    public abstract boolean save(Player player, QueryBuilder queryBuilder);


    // TODO: Add as option but suggest using batcher
    public abstract Row rawFetch(Player player);
    public abstract Row rawUpdate(Player player);

    /**
     * Converts current player data to a row object.
     * Note: Called internally to transfer player data to other cluster nodes
     *
     * @param player
     *          The player who's data should be converted
     * @return
     *          A Row instance populated with the players data
     */
    public Row toRow(Player player) {
        Row row = new Row();
        row.setTableName(getTableName());
        return populateRow(row, player);
    }

    /**
     * Populates a row instance with data from a player.
     * Note: This should be implemented to
     *
     * @param row
     *          The row to populate
     * @param player
     *          The players who's data should be used
     * @return
     *          The populated row
     */
    public abstract Row populateRow(Row row, Player player);


    // ======================   HELPERS

    public abstract String getQueryString(Player player);

    public abstract String getUpdateString(Player player);






}
