package com.endersuite.endersync.networking;

import com.endersuite.endersync.Main;
import com.endersuite.endersync.networking.packets.AbstractPacket;
import com.endersuite.libcore.config.ConfigManager;
import com.endersuite.libcore.strfmt.Level;
import com.endersuite.libcore.strfmt.StrFmt;
import lombok.Getter;
import org.jgroups.Address;
import org.jgroups.JChannel;

import java.util.UUID;

/**
 * TODO: Add docs
 *
 * @author Maximilian Vincent Heidenreich
 * @since 09.05.21
 */
public class NetworkManager extends AbstractPacketDelgator {

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


    // ======================   CONSTRUCTOR

    public NetworkManager() throws Exception {
        super(Main.getPlugin().getEventLoop());

        this.jChannel = new JChannel();
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
    public void send(Address destination, AbstractPacket packet) throws Exception {
        getJChannel().send(destination, packet);
    }

    /**
     * Broadcasts a packet to all nodes in the current cluster.
     *
     * @param packet
     *          The packet to send
     * @throws Exception
     */
    public void broadcast(AbstractPacket packet) throws Exception {
        send(null, packet);
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
