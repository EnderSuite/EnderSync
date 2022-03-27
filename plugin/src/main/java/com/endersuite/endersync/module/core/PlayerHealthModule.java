package com.endersuite.endersync.module.core;

import com.endersuite.database.mysql.Row;
import com.endersuite.endersync.Plugin;
import com.endersuite.endersync.module.ASynchronizedPlayerModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

/**
 * Synchronizes player health.
 *      - Health value
 *      - Health scale
 *      - Health max
 *
 * @author Maximilian Vincent Heidenreich
 * @since 10.05.21
 */
public class PlayerHealthModule extends ASynchronizedPlayerModule {


    // ======================   VARS

    // ======================   CONSTRUCTOR

    public PlayerHealthModule() {
        super("core_health");
        setCritical(true);
    }


    // ======================   BUSINESS LOGIC

    @Override
    public boolean synchronize(Player player, Row data) {
        Bukkit.getScheduler().runTask(Plugin.getPlugin(), () -> {
            player.setHealthScale(data.getDouble("scale"));
            player.setMaxHealth(data.getDouble("max"));
            player.setHealth(data.getDouble("health"));
        });
        return true;
    }

    @Override
    public boolean save(Player player) {
        CompletableFuture<Boolean> callback = new CompletableFuture<>();

        return false;
    }

    @Override
    public boolean setupDatabase() {

        //Plugin.getPlugin().getDb().execUpdate("CREATE TABLE IF NOT EXISTS " + getTableName() + " (uuid varchar(64) not null, scale double not null, max double not null, health double not null)");

        return true;
    }

    @Override
    public CompletableFuture<Row> getRowFromDatabase(Player player) {
        CompletableFuture<Row> callback = new CompletableFuture<>();
        /*Plugin.getPlugin().getDb().asyncQuery((resultHandler) -> {
            callback.complete(resultHandler.getRowList().get(0));
        }, "SELECT scale, max, health FROM " + getTableName() + " WHERE uuid=?", player.getUniqueId().toString());*/
        return callback;
    }

    @Override
    public Row populateRow(Row row, Player player) {
        row.add("health", player.getHealth());
        row.add("max", player.getMaxHealth());
        row.add("scale", player.getHealthScale());
        return row;
    }


    // ======================   HELPERS


}
