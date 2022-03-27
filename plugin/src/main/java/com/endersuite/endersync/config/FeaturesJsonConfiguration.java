package com.endersuite.endersync.config;

import com.endersuite.libcore.config.json.IConfiguration;

import java.util.*;

/**
 * TODO: Add docs
 *
 * @author Maximilian Vincent Heidenreich
 * @since 10.05.21
 */
public class FeaturesJsonConfiguration implements IConfiguration {

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


    // ======================   MODULE CONFIG

    public static class ModuleConfiguration implements IConfiguration {

        private double version = 1.0;

        @Override
        public double getConfigVersion() {
            return version;
        }

        @Override
        public void setConfigVersion(double version) {
            this.version = version;
        }

        /**
         * Enable module on default saves.
         */
        public boolean onSave = false;

        /**
         * Enable module on default synchronizations.
         */
        public boolean onSynchronize = false;

    }



    // ======================   CORE FEATURES




    // ======================   MODULES

    public Map<String, ModuleConfiguration> modules = new HashMap<>();


}
