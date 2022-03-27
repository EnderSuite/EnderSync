package com.endersuite.endersync.module.core;

import com.endersuite.database.mysql.Row;
import com.endersuite.endersync.Plugin;
import com.endersuite.endersync.module.ASynchronizedPlayerModule;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

/**
 * Synchronizes player gamemode.
 *      - Gamemode
 *
 * @author Maximilian Vincent Heidenreich
 * @since 10.05.21
 */
public class PlayerGameModeModule extends ASynchronizedPlayerModule {

    // ======================   VARS

    // ======================   CONSTRUCTOR

    public PlayerGameModeModule() {
        super("core_gamemode");
    }


    // ======================   BUSINESS LOGIC

    @Override
    public boolean synchronize(Player player, Row data) {
        if (data == null) {
            return save(player);
        }

        // TODO: Add incompatible check?
        GameMode gameMode = GameMode.valueOf(data.getString("gameMode"));

        Bukkit.getScheduler().runTask(Plugin.getPlugin(), () -> player.setGameMode(gameMode));
        return false;
    }

    @Override
    public boolean save(Player player) {
        /*Plugin.getPlugin().getDb().execUpdate(
                "INSERT INTO " + getTableName() + " (uuid, gameMode) VALUES(?) ON DUPLICATE KEY UPDATE inv=?",
                player.getGameMode().toString()
        );*/
        return true;
    }

    @Override
    public boolean setupDatabase() {
        //Plugin.getPlugin().getDb().execUpdate("CREATE TABLE IF NOT EXISTS " + getTableName() + "(uuid VARCHAR(32) NOT NULL, gameMode VARCHAR(12) NOT NULL);");
        return true;
    }

    @Override
    public CompletableFuture<Row> getRowFromDatabase(Player player) {
        CompletableFuture<Row> callback = new CompletableFuture<>();
        callback.complete(null);
        return callback;
    }

    @Override
    public Row populateRow(Row row, Player player) {
        row.add("gameMode", player.getGameMode().toString());
        return row;
    }


    // ======================   HELPERS


}
