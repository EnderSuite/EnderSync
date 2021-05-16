package com.endersuite.endersync;

import com.endersuite.libcore.plugin.bootstrap.PluginBootstrap;
import com.endersuite.libcore.strfmt.Level;
import com.endersuite.libcore.strfmt.StrFmt;

import java.io.File;

/**
 * The main entry point of the plugin.
 * It does all the heavy lifting to get the plugin started.
 *
 * @author Maximilian Vincent Heidenreich
 * @since 08.05.21
 */
public class Bootstrap extends PluginBootstrap {


    public Bootstrap() {
        super(Plugin.class, "{level} §l§7» §l§3Ender§l§fSync§r §cB§eO§bO§cT§eS§bT§cR§eA§bP§r : ");
        StrFmt.outputLevel = Level.TRACE;   // DEV only -> override with config

        File depsDir = new File(getDataFolder(), "lib");
        if (!depsDir.exists() && !depsDir.mkdirs()) {
            panic("Could not create dependency dir at '§e" + depsDir.getAbsolutePath() + "§r'");
        }

        /*try {
            getInjector().download(getResource("deps.txt"), depsDir.toPath(), false);
        }
        catch (RuntimeException e) {
            panic("Could not download dependencies", e);
        }


        try {
            getInjector().inject(depsDir.toPath());
        }
        catch (RuntimeException e) {
            panic("Could not inject dependencies", e);
        }*/

        try {
            loadPlugin(Plugin.class);
        }
        catch (RuntimeException e) {
            panic("Could not load plugin", e);
        }

    }

    // ======================   HELPERS

    /*public static Plugin getPlugin() {
        return instance;
    }*/

}
