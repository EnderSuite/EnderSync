package com.endersuite.endersync.networking;

import com.endersuite.endersync.Main;
import com.endersuite.endersync.events.core.PacketReceivedEvent;
import com.endersuite.endersync.networking.packets.AbstractPacket;
import com.endersuite.endersync.networking.packets.TestPacket;
import com.endersuite.libcore.strfmt.Level;
import com.endersuite.libcore.strfmt.StrFmt;
import de.maximilianheidenreich.jeventloop.EventLoop;
import org.jgroups.Message;
import org.jgroups.Receiver;
import org.jgroups.View;
import org.jgroups.util.MessageBatch;

/**
 * Handles incoming {@link Message}'s and dispatches {@link PacketReceivedEvent}s.
 *
 * @author Maximilian Vincent Heidenreich
 * @since 09.05.21
 */
public class DefaultReceiver implements Receiver {

    private final EventLoop eventLoop = Main.getPlugin().getEventLoop();

    /**
     * TODO: Add docs -> What changes inside the plugin?
     *
     * @param new_view
     *          The new membership state
     */
    @Override
    public void viewAccepted(View new_view) {
        new StrFmt("{prefix} Cluster members updated: " + new_view)
                .setLevel(Level.DEBUG)
                .toConsole();

        try {   // TODO: remove
            NetworkManager.getInstance().broadcast(new TestPacket(true));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Executes {@link DefaultReceiver#processSingleMessage(Message)} for the received {@link Message}.
     *
     * @param msg
     *          The received message
     */
    @Override
    public void receive(Message msg) {
        processSingleMessage(msg);
    }

    /**
     * Executes {@link DefaultReceiver#processSingleMessage(Message)} for each {@link Message} inside of the batch.
     *
     * @param batch
     *          The received batch
     */
    @Override
    public void receive(MessageBatch batch) {
        batch.stream().forEach(this::processSingleMessage);
    }

    /**
     * Sets the sender & recipient fields and dispatches {@link PacketReceivedEvent}.
     *
     * @param msg
     *          The Message instance from JGroup
     */
    private void processSingleMessage(Message msg) {
        if (msg.getObject() instanceof AbstractPacket) {
            AbstractPacket packet = (AbstractPacket) msg.getObject();
            packet.setSender(msg.getSrc());
            packet.setRecipient(msg.getDest());

            new StrFmt("{prefix} Received packet: " + packet + " in " + (System.currentTimeMillis() - packet.getCreatedAt()) + "ms")
                    .setLevel(Level.DEBUG)
                    .toConsole();
            eventLoop.dispatch(new PacketReceivedEvent(packet, msg.getSrc(), msg.getDest()));
        }
    }

}
