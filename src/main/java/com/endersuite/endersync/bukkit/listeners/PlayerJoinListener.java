package com.endersuite.endersync.bukkit.listeners;

import com.endersuite.endersync.Plugin;
import com.endersuite.endersync.events.core.PlayerSynchronizeEvent;
import com.endersuite.endersync.networking.NetworkManager;
import com.endersuite.endersync.networking.packets.ACollectableResponsePacket;
import com.endersuite.endersync.networking.packets.core.player.RequestIsPlayerOnlinePacket;
import com.endersuite.endersync.networking.packets.core.player.ResponseIsPlayerOnlinePacket;
import com.endersuite.libcore.strfmt.StrFmt;
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

        /*try {
            NetworkManager.getInstance().broadcastCollect(new RequestIsPlayerOnlinePacket(event.getPlayer().getUniqueId())).thenAccept(result -> {
                System.out.println(result);
                for (ACollectableResponsePacket response : result) {
                    ResponseIsPlayerOnlinePacket p = (ResponseIsPlayerOnlinePacket) response;
                    new StrFmt(p.getNodeDisplayName() + ": " + p.isOnline()).toConsole();
                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }*/
    }

}
