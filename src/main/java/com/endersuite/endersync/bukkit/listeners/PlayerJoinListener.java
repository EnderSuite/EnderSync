package com.endersuite.endersync.bukkit.listeners;

import com.endersuite.endersync.Plugin;
import com.endersuite.endersync.events.core.PlayerSynchronizeEvent;
import de.maximilianheidenreich.jeventloop.EventLoop;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * TODO: Add docs
 *
 * @author Maximilian Vincent Heidenreich
 * @since 10.05.21
 */
public class PlayerJoinListener implements Listener {

    private final static EventLoop eventLoop = Plugin.getPlugin().getEventLoop();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        eventLoop.dispatch(new PlayerSynchronizeEvent(event.getPlayer()));
    }

}
