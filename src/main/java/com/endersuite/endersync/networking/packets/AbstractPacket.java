package com.endersuite.endersync.networking.packets;

import lombok.Getter;
import lombok.Setter;
import org.jgroups.Address;

import java.io.Serializable;
import java.util.UUID;

/**
 * A abstract packet implementation that can be sent over the network.
 *
 * @author Maximilian Vincent Heidenreich
 * @since 09.05.21
 */
@Getter
public abstract class AbstractPacket implements Serializable {

    // ======================   VARS

    /**
     * A unique id identifying the packet.
     */
    @Setter
    private UUID id;

    /**
     * Timestamp at which the packet instance was created.
     */
    @Getter
    private final long createdAt;

    /**
     * The sender of the packet (Only populated when received).
     */
    @Getter
    @Setter
    private Address sender;

    /**
     * The recipient of the packet (Only populated when received).
     */
    @Getter
    @Setter
    private Address recipient;

    // ======================   CONSTRUCTOR

    /**
     * Create a new AbstractPacket with a random id.
     */
    public AbstractPacket() {
        this(UUID.randomUUID());
    }

    /**
     * Creates a new AbstractPacket with a specified id.
     *
     * @param id
     *          The id to use.
     */
    public AbstractPacket(UUID id) {
        this.id = id;
        this.createdAt = System.currentTimeMillis();
    }


    // ======================   HELPERS

    @Override
    public String toString() {
        if (sender != null && recipient != null)
            return String.format("%s(%s)_s(%s)-r(%s)", this.getClass().getSimpleName(), getId().toString().split("-")[0], sender, recipient);
        else
            return String.format("%s(%s)", this.getClass().getSimpleName(), getId().toString().split("-")[0]);
    }


}
