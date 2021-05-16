package com.endersuite.endersync.module.core;

import com.endersuite.database.mysql.Row;
import com.endersuite.database.mysql.builder.QueryBuilder;
import com.endersuite.endersync.Plugin;
import com.endersuite.endersync.module.ASynchronizedPlayerModule;
import com.endersuite.libcore.serializer.ItemStackSerializer;
import com.endersuite.libcore.serializer.SerializedItem;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Synchronizes player inventory.
 *
 * @author Maximilian Vincent Heidenreich
 * @since 10.05.21
 */
public class InventoryModule extends ASynchronizedPlayerModule {


    // ======================   VARS

    // ======================   CONSTRUCTOR

    public InventoryModule() {
        super("core_inv");
    }


    // ======================   BUSINESS LOGIC

    @Override
    public boolean synchronize(Player player, Row data) {

        // TODO: Add incompatible check?
        String serialized = data.getString("inv");
        System.out.println("invser: "+serialized);

        Bukkit.getScheduler().runTask(Plugin.getPlugin(), () -> {

            List<SerializedItem> items = ItemStackSerializer.deserialize(serialized);
            for (SerializedItem item : items) {
                if (item.isEmptySlot()) {
                    player.getInventory().setItem(item.getSlot(), null);
                    continue;
                }
                ItemStack itemasd = item.toItemStack();
                player.getInventory().setItem(item.getSlot(), itemasd);
            }

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

        String serialized = ItemStackSerializer.serialize(player.getInventory());
        System.out.println("PopulateRow: " + serialized);
        row.add("inv", serialized);
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
