package com.endersuite.endersync.events.core;

import com.endersuite.endersync.networking.packets.AbstractPacket;
import de.maximilianheidenreich.jeventloop.events.AbstractEvent;
import lombok.Getter;
import org.jgroups.Address;

/**
 * Gets dispatched whenever an {@link AbstractPacket} was received over the network.
 *
 * @author Maximilian Vincent Heidenreich
 * @since 09.05.21
 */
public class PacketReceivedEvent extends AbstractEvent<Void> {

    // ======================   VARS

    /**
     * The received packet.
     */
    @Getter
    private final AbstractPacket packet;

    /**
     * The sender of the packet.
     */
    @Getter
    private final Address sender;

    /**
     * The recipient of the packet.
     */
    @Getter
    private final Address recipient;


    // ======================   CONSTRUCTOR

    public PacketReceivedEvent(AbstractPacket packet, Address sender, Address recipient) {
        super((byte) 1);
        this.packet = packet;
        this.sender = sender;
        this.recipient = recipient;
    }


    // ======================   HELPERS

    @Override
    public String toString() {
        return String.format("%s(%s)-s(%s)-r(%s)", this.getClass().getSimpleName(), getId().toString().split("-")[0], sender, recipient);
    }

}
