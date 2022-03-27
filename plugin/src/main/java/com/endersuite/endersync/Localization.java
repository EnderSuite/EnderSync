package com.endersuite.endersync;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Add docs
 *
 * @author Maximilian Vincent Heidenreich
 * @since 19.05.21
 */
public class Localization {

    // ======================   VARS

    private final Map<String, String> localizedStrings;


    // ======================   CONSTRUCTOR

    public Localization() {
        this.localizedStrings = new HashMap<>();
    }

    // ======================   BUSINESS LOGIC

    public String get(String identifier) {
        if (!this.localizedStrings.containsKey(identifier))
            return "Invalid localization identifier: " + identifier;

        return this.localizedStrings.get(identifier);
    }


    // ======================   HELPERS

    public void loadFromDatabase() {

    }

}
