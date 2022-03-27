package com.endersuite.endersync.config;

import com.endersuite.libcore.config.json.IConfiguration;
import com.endersuite.libcore.strfmt.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Add docs
 *
 * @author Maximilian Vincent Heidenreich
 * @since 20.07.21
 */
public class SynchronizationConfig implements IConfiguration {

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

    public List<String> synchronizationConfig = new ArrayList<>();
    public Map<String, SynchronizationConfig> templates = new HashMap<>();

    public String visitForMoreInformation = "https:://sync.endersuite.com/wiki#config"; // TODO: Add link after website deploy
    public Level logLevel = Level.TRACE;
    public String lang = "en";

    public MainConfiguration.NetworkSection networking = new MainConfiguration.NetworkSection();
    public static class NetworkSection {
        public String nodeName = "default";
    }

    public MainConfiguration.DatabaseSection database = new MainConfiguration.DatabaseSection();

    public static class SynchronizationConfiguration {
        public String fromNode;
        public String toNode;

        public boolean biDirectional = false;
    }

}
