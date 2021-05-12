package com.endersuite.endersync.modules.core;

import com.endersuite.database.mysql.Row;
import com.endersuite.database.mysql.builder.QueryBuilder;
import com.endersuite.endersync.Plugin;
import com.endersuite.endersync.modules.ASynchronizedPlayerModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
    }


    // ======================   BUSINESS LOGIC

    @Override
    public boolean synchronize(Player player, Row data) {
        Bukkit.getScheduler().runTask(Plugin.getPlugin(), () -> {
            player.setHealthScale(data.getDouble("scale"));
            player.setMaxHealth(data.getDouble("max"));
            player.setHealth(data.getDouble("health"));
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
        row.add("health", player.getHealth());
        row.add("max", player.getMaxHealth());
        row.add("scale", player.getHealthScale());
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
