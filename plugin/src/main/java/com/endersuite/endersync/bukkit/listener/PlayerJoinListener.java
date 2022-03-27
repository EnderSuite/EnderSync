package com.endersuite.endersync.bukkit.listener;

import com.endersuite.endersync.Plugin;
import com.endersuite.endersync.database.models.Player;
import com.endersuite.endersync.event.core.PlayerSynchronizeEvent;
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

        //Plugin.getPlugin().getDb()
        Player pDB = new Player(event.getPlayer().getUniqueId());
        pDB.name = event.getPlayer().getName();
        pDB.displayName = event.getPlayer().getDisplayName();
        Plugin.getPlugin().getDb().insert(pDB);

        eventLoop.dispatch(new PlayerSynchronizeEvent(event.getPlayer()));

        /*RequestIsPlayerOnlinePacket packet = new RequestIsPlayerOnlinePacket(event.getPlayer().getUniqueId());
        CompletableTransmission transmission = Transmission.newBuilder(packet)
                .broadcast(true)
                .collectAll()
                .timeout(Duration.ofSeconds(2))
                .build();

        try {
            transmission.transmit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        try {
            List<ACollectablePacket> responses = transmission.join();
            System.out.println(responses);
            for (ACollectablePacket response : responses) {
                ResponseIsPlayerOnlinePacket p = (ResponseIsPlayerOnlinePacket) response;
                new StrFmt(p.getNodeDisplayName() + ": " + p.isOnline()).toLog();
            }
        } catch (CompletableTimeoutException e) {
            System.out.println("Timeout blocking");
            e.printStackTrace();
        }*/


        /*try {
            Transmission.newBuilder(packet)
                    .broadcast(true)
                    .collectAll()
                    .timeout(Duration.ofSeconds(4))
                    .onDone((List<ACollectablePacket> result) -> {
                        System.out.println(result);
                        for (ACollectablePacket response : result) {
                            ResponseIsPlayerOnlinePacket p = (ResponseIsPlayerOnlinePacket) response;
                            new StrFmt(p.getNodeDisplayName() + ": " + p.isOnline()).toLog();
                        }
                    })
                    .onTimeout((CompletableTimeoutException e) -> {
                            new StrFmt("Timeout!! whehehe").setLevel(Level.ERROR).toLog();
                    })
                    .onError((Throwable t) -> t.printStackTrace())
                    .build()
                    .transmit();

        } catch (Exception exception) {
            exception.printStackTrace();
        }*/

        /*NetworkManager.getInstance().broadcastCollect(new RequestIsPlayerOnlinePacket(event.getPlayer().getUniqueId()), true).thenAccept(result -> {
                System.out.println(result);
                for (ACollectableResponsePacket response : result) {
                    ResponseIsPlayerOnlinePacket p = (ResponseIsPlayerOnlinePacket) response;
                    new StrFmt(p.getNodeDisplayName() + ": " + p.isOnline()).toLog();
                }
            });*/
    }

}
