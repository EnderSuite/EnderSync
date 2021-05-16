package com.endersuite.endersync.bukkit.listener;

import com.endersuite.endersync.Plugin;
import com.endersuite.endersync.event.core.PlayerSaveEvent;
import de.maximilianheidenreich.jeventloop.EventLoop;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * TODO: Add docs
 *
 * @author Maximilian Vincent Heidenreich
 * @since 10.05.21
 */
public class PlayerLeaveListener implements Listener {

    private final static EventLoop eventLoop = Plugin.getPlugin().getEventLoop();

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        eventLoop.dispatch(new PlayerSaveEvent(event.getPlayer()));
    }

}
