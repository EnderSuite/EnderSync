package com.endersuite.endersync.module.core;

import com.endersuite.database.mysql.Row;
import com.endersuite.database.mysql.builder.QueryBuilder;
import com.endersuite.endersync.Plugin;
import com.endersuite.endersync.module.ASynchronizedPlayerModule;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

/**
 * Synchronizes player gamemode.
 *      - Gamemode
 *
 * @author Maximilian Vincent Heidenreich
 * @since 10.05.21
 */
public class PlayerGamemodeModule extends ASynchronizedPlayerModule {


    // ======================   VARS

    // ======================   CONSTRUCTOR

    public PlayerGamemodeModule() {
        super("core_gamemode");
    }


    // ======================   BUSINESS LOGIC

    @Override
    public boolean synchronize(Player player, Row data) {

        // TODO: Add incompatible check?
        GameMode gameMode = GameMode.valueOf(data.getString("gamemode"));

        Bukkit.getScheduler().runTask(Plugin.getPlugin(), () -> {
            player.setGameMode(gameMode);
        });
        return false;
    }

    @Override
    public boolean save(Player player, QueryBuilder queryBuilder) {
        return false;
    }

    @Override
    public Row rawFetch(Player player) {
        return null;
    }

    @Override
    public Row rawUpdate(Player player) {
        return null;
    }

    @Override
    public Row populateRow(Row row, Player player) {
        row.add("gamemode", player.getGameMode().toString());
        return row;
    }

    @Override
    public String getQueryString(Player player) {
        return null;
    }

    @Override
    public String getUpdateString(Player player) {
        return null;
    }


    // ======================   HELPERS


}
