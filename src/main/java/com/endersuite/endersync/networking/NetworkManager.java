package com.endersuite.endersync.networking;

import com.endersuite.endersync.Plugin;
import com.endersuite.endersync.networking.handlers.CollectablePacketHandler;
import com.endersuite.endersync.networking.packets.ACollectableRequestPacket;
import com.endersuite.endersync.networking.packets.ACollectableResponsePacket;
import com.endersuite.endersync.networking.packets.APacket;
import com.endersuite.libcore.config.ConfigManager;
import com.endersuite.libcore.strfmt.Level;
import com.endersuite.libcore.strfmt.StrFmt;
import lombok.Getter;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.ObjectMessage;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * TODO: Add docs
 *
 * @author Maximilian Vincent Heidenreich
 * @since 09.05.21
 */
public class NetworkManager extends APacketDelegator {

    // ======================   VARS

    /**
     * The NetworkManager singleton.
     */
    private static NetworkManager instance;

    /**
     * The configured {@link JChannel} instance.
     */
    @Getter
    private final JChannel jChannel;

    //private Map<UUID, >


    // ======================   CONSTRUCTOR

    public NetworkManager() throws Exception {
        super(Plugin.getPlugin().getEventLoop());

        this.jChannel = new JChannel(Plugin.getPlugin().getResource("jgroup_conf.xml"));
        this.jChannel.setReceiver(new DefaultReceiver());
        //this.jChannel.setDiscardOwnMessages(true);

        String nodeName = ConfigManager.getInstance().get("config").getString("network.node-name");
        if ("default".equalsIgnoreCase(nodeName))
            nodeName = UUID.randomUUID().toString();

        getJChannel().name(nodeName);

    }


    // ======================   BUSINESS LOGIC

    /**
     * Connects to an existing cluster or creates a new one if none exist already.
     *
     * @param clusterName
     *          The cluster name
     * @throws Exception
     */
    public void connect(String clusterName) throws Exception {
        getJChannel().connect(clusterName);
    }

    /**
     * Closes the connection to the cluster.
     */
    public void disconnect() {
        getJChannel().close();
    }

    // ======================   HELPERS

    /**
     * Sends a packet to a node specified by its address.
     *
     * @param destination
     *          The address of the destination node
     * @param packet
     *          The packet to send
     * @throws Exception
     */
    public void sendRaw(Address destination, APacket packet) throws Exception {
        getJChannel().send(destination, packet);   // Add option to set custom flas like loopback
    }

    /**
     * Broadcasts a packet to all nodes in the current cluster.
     *
     * @param packet
     *          The packet to send
     * @throws Exception
     */
    public void broadcastRaw(APacket packet) throws Exception {
        sendRaw(null, packet);
    }

    public void broadcastRaw(APacket packet, boolean loopback) throws Exception {

    }

    public <T extends ACollectableResponsePacket> CompletableFuture<List<? extends ACollectableResponsePacket>> broadcastCollect(ACollectableRequestPacket<T> packet) throws Exception {
        CompletableFuture<List<? extends ACollectableResponsePacket>> callback = new CompletableFuture<>();
        new StrFmt("sending: " + packet.getCollectId()).toConsole();
        CollectablePacketHandler.getCollectionCallbacks().put(packet.getCollectId(), callback);
        broadcastRaw(packet);
        return callback;
    }


    // ======================   GETTER & SETTER

    /**
     * Returns the NetworkManager singleton instance.
     * Note: Also creates one if none exists.
     *
     * @return The NetworkManager instance.
     */
    public static NetworkManager getInstance() {
        NetworkManager networkManager = null;
        try {
            networkManager = new NetworkManager();
        } catch (Exception exception) {
            exception.printStackTrace();
            new StrFmt("{prefix} Could not initialize JGroup networking! Database fallback will be used! This might affect performance!").setLevel(Level.ERROR).toConsole();
        }

        if (NetworkManager.instance == null)
            NetworkManager.instance = networkManager;

        return NetworkManager.instance;

    }

}
