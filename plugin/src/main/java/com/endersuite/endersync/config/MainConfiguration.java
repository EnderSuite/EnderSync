package com.endersuite.endersync.config;

import com.endersuite.libcore.config.json.IConfiguration;
import com.endersuite.libcore.strfmt.Level;

/**
 * Contains the main configuration used by different parts of the plugin.
 *
 * @author Maximilian Vincent Heidenreich
 * @since 21.05.21
 */
public class MainConfiguration implements IConfiguration {

    // ======================   UTIL

    private double version = 5.0;

    @Override
    public double getConfigVersion() {
        return version;
    }

    @Override
    public void setConfigVersion(double version) {
        this.version = version;
    }


    // ======================   MAIN CONFIG

    public String visitForMoreInformation = "https:://sync.endersuite.com/wiki#config"; // TODO: Add link after website deploy
    public Level logLevel = Level.TRACE;
    public String lang = "en";

    public NetworkSection networking = new NetworkSection();
    public static class NetworkSection {
        public String nodeName = "default";
    }

    public DatabaseSection database = new DatabaseSection();
    public static class DatabaseSection {
        public String host = "127.0.0.1";
        public int port = 3306;
        public String database = "endersync";
        public String username = "endersyncUser";
        public String password = "ChangeMe!";
        public boolean useSSL = true;
    }

}
