package com.endersuite.endersync.module.core;

import com.endersuite.database.mysql.Row;
import com.endersuite.endersync.Plugin;
import com.endersuite.endersync.module.ASynchronizedPlayerModule;
import com.endersuite.libcore.serializer.ASerializedItemStack;
import com.endersuite.libcore.serializer.DevItemStack;
import com.endersuite.libcore.serializer.DevItemStackSerializer;
import com.endersuite.libcore.serializer.SerializedInventory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Synchronizes player inventory.
 *
 * @author Maximilian Vincent Heidenreich
 * @since 10.05.21
 */
public class InventoryModule extends ASynchronizedPlayerModule {

    // ======================   VARS

    /*public static class InventoryModuleConfiguration extends FeaturesJsonConfiguration.ModuleConfiguration {
        public String message = "Hello Teher";
    }*/

    // ======================   CONSTRUCTOR

    public InventoryModule() {
        super("core_inv");
        //setModuleConfigurationClass(InventoryModuleConfiguration.class);
    }


    // ======================   BUSINESS LOGIC

    @Override
    public boolean synchronize(Player player, Row data) {

        // TODO: Add incompatible check?
        //List<ASerializedItemStack<?>> serializedRaw = new Gson().fromJson(data.getString("inv"), new TypeToken<ArrayList<ASerializedItemStack<?>>>() {}.getType());

        SerializedInventory serializedInventory = SerializedInventory.fromJson(data.getString("inv"));

        Bukkit.getScheduler().runTask(Plugin.getPlugin(), () -> {

            player.getInventory().setContents(serializedInventory.getContents());
            /*for (ASerializedItemStack<?> item : serializedRaw) {
                if (!item.isEmpty()) {
                    player.getInventory().setItem(item.getSlot(), item.toItemStack());
                }
                else {
                    player.getInventory().setItem(item.getSlot(), null);
                }
            }*/
            player.getInventory().setItem(4, getUnsupportedItemPlaceholder("Iron Axe", 1, false));
            player.getInventory().setItem(5, getUnsupportedItemPlaceholder("Wood Axe", 1, true));
            //player.getInventory().setItem(4, getUnsupportedBlockPlaceholder());

        });
        return true;
    }

    @Override
    public boolean save(Player player) {
        //List<ASerializedItemStack<?>> serialized = InventorySerializer.serializeInventoryContents(player.getInventory());
        SerializedInventory serializedInventory = new SerializedInventory(player.getInventory());
        String s = "INSERT INTO "+ getTableName() +" (uuid, inv) VALUES('"+player.getUniqueId().toString()+"', '"+serializedInventory.toJson()+"') ON DUPLICATE KEY UPDATE inv='"+serializedInventory.toJson()+"'";
        //Plugin.getPlugin().getDb().execUpdate(s);
        //Plugin.getPlugin().getDdatabase().execUpdate("INSERT INTO ? (uuid, inv) VALUES(?, ?) ON DUPLICATE KEY UPDATE inv=?", getTableName(), player.getUniqueId().toString(), DevItemStackSerializer.toJson(inv), DevItemStackSerializer.toJson(inv));
        return true;
    }

    @Override
    public boolean setupDatabase() {
        //System.out.println((this.moduleConfiguration instanceof InventoryModuleConfiguration));
        //System.out.println("Configured value: " + getModuleConfig(InventoryModuleConfiguration.class));
        //Plugin.getPlugin().getDb().execUpdate("CREATE TABLE IF NOT EXISTS " + getTableName() + " (uuid varchar(36) not null, inv TEXT not null, PRIMARY KEY (uuid))");
        // TODO: Check if created
        return true;
    }

    @Override
    public CompletableFuture<Row> getRowFromDatabase(Player player) {
        CompletableFuture<Row> callback = new CompletableFuture<>();
        /*Plugin.getPlugin().getDb().asyncQuery((resultHandler) -> {
            callback.complete(resultHandler.getRowList().get(0));
        }, "SELECT inv FROM " + getTableName() + " WHERE uuid=?", player.getUniqueId().toString());*/
        return callback;
    }

    @Override
    public Row populateRow(Row row, Player player) {
        //List<ASerializedItemStack<?>> serialized = InventorySerializer.serializeInventoryContents(player.getInventory());
        SerializedInventory serializedInventory = new SerializedInventory(player.getInventory());
        row.add("inv", serializedInventory.toJson());
        return row;
    }


    // ======================   HELPERS

    public static ItemStack getUnsupportedBlockPlaceholder(String displayName, int amount) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, amount);
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString("70e0888d-a55d-42da-ac48-5096f9bcaf58")));
        meta.setDisplayName("Unsupported block: §e" + displayName);
        meta.setLore(Arrays.asList(
                "§dThis item cannot be synchronized",
                "§dbecause this servers version does",
                "§dnot support it!",
                "§0{ESPLACEHOLDER}"
        ));
        itemStack.setItemMeta(meta);
        // meta.setEnchantment
        return itemStack;
    }

    public static ItemStack getUnsupportedItemPlaceholder(String displayName, int amount, boolean enchanted) {
        ItemStack itemStack = new ItemStack(Material.BARRIER, amount);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("Unsupported item: §e" + displayName);
        meta.setLore(Arrays.asList(
                "§dThis item cannot be synchronized",
                "§dbecause this servers version does",
                "§dnot support it!",
                "§0{ESPLACEHOLDER}"
        ));
        if (enchanted)  // Add from json if they exist in this version / lore if not
            meta.addEnchant(Enchantment.SILK_TOUCH, 1, false);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

}
