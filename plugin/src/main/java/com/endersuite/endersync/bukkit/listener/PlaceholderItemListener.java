package com.endersuite.endersync.bukkit.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * TODO: Add docs
 *
 * @author Maximilian Vincent Heidenreich
 * @since 20.05.21
 */
public class PlaceholderItemListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInvClick(InventoryClickEvent event) {
        /*ItemStack item = event.getCurrentItem();
        if (item != null && item.getItemMeta().getLore().contains("§0{ESPLACEHOLDER}")) {
            event.getWhoClicked().sendMessage("NONO!");
            event.setCancelled(true);
        }*/
    }


    /*@EventHandler(priority = EventPriority.HIGHEST)
    public void onDrop(EntityDropItemEvent event) {
        for (String s : event.getItemDrop().getItemStack().getItemMeta().getLore()) {
            if (!s.contains("{ESPLACEHOLDER}")) continue;

            event.getEntity().sendMessage("NONO!");
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent event) {
        if (event.get().getItemStack().getItemMeta().getLore().contains("§0{ESPLACEHOLDER}")) {
            event.getEntity().sendMessage("NONO!");
            event.setCancelled(true);
        }
    }*/

}
