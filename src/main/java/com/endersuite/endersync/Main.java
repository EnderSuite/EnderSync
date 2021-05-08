package com.endersuite.endersync;

import com.endersuite.libcore.config.ConfigManager;
import com.endersuite.libcore.strfmt.Level;
import com.endersuite.libcore.strfmt.Status;
import com.endersuite.libcore.strfmt.StrFmt;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;


/**
 * The main entry point of the plugin.
 * It does all the heavy lifting to get the plugin started.
 *
 * @author Maximilian Vincent Heidenreich
 * @since 08.05.21
 */
public class Main extends JavaPlugin {

    // ======================   VARS

    /**
     * Store the plugin folder (plugins/EnderSync).
     */
    @Getter
    private static String pluginDataFolder;


    // ======================   BUKKIT LOGIC

    /**
     * Enables the plugin duh!
     * Just look at the code, cuz I won't write a doc string for it :)
     */
    @Override
    public void onEnable() {

        StrFmt.prefix = "{level} §l§7» §l§3Ender§l§fSync {status} : ";
        StrFmt.outputLevel = Level.TRACE;       // Dev only

        StrFmt.fromLocalized("core.plugin-enable-start").setLevel(Level.INFO).toConsole();

        // Setup data folder
        Main.pluginDataFolder = Bukkit.getPluginManager().getPlugin("EnderSync").getDataFolder().getAbsolutePath();
        File pluginFolder = new File(pluginDataFolder);
        if (!pluginFolder.exists()) {
            boolean res = pluginFolder.mkdir();

            if (!res) {
                panic("Could not create plugin data folder");
                return;
            }
        }

        // Load config file
        try {
            ConfigManager.getInstance().load("config", Paths.get(pluginDataFolder, "config.yml").toAbsolutePath().toString(), this);
        } catch (IOException exception) {
            exception.printStackTrace();
            panic("Could not load config.yml");
            return;
        }
        StrFmt.outputLevel = Level.valueOf(ConfigManager.getInstance().get("config").getString("core.log-level").toUpperCase());

        // Load lang file
        String langFile = "lang-" + ConfigManager.getInstance().get("config").getString("core.lang") + ".yml";
        try {
            ConfigManager.getInstance().load("lang", Paths.get(pluginDataFolder, langFile).toAbsolutePath().toString(), this);
        } catch (IOException exception) {
            exception.printStackTrace();
            panic("Could not load " + langFile);
            return;
        }
        StrFmt.localizationConfig = ConfigManager.getInstance().get("lang");
        StrFmt.fromLocalized("debug.lang-test").setLevel(Level.DEBUG).toConsole();

        // Load features file
        try {
            ConfigManager.getInstance().load("features", Paths.get(pluginDataFolder, "features.yml").toAbsolutePath().toString(), this);
        } catch (IOException exception) {
            exception.printStackTrace();
            panic("Could not load features.yml");
            return;
        }

        // Load extensions

        // Connect to db

        // Connect to network

        //


        // Output test TODO: Remove
        new StrFmt("{prefix} Trace message")
                .setLevel(Level.TRACE)
                .toConsole();
        new StrFmt("{prefix} Debug message")
                .setLevel(Level.DEBUG)
                .toConsole();
        new StrFmt("{prefix} Info message")
                .setLevel(Level.INFO)
                .toConsole();
        new StrFmt("{prefix} Warning message")
                .setLevel(Level.WARN)
                .toConsole();
        new StrFmt("{prefix} error message")
                .setLevel(Level.ERROR)
                .toConsole();
        new StrFmt("{prefix} fatal message")
                .setLevel(Level.FATAL)
                .toConsole();

        new StrFmt("{prefix} Progress message")
                .setStatus(Status.PROGRESS)
                .setLevel(Level.INFO)
                .toConsole();
        new StrFmt("{prefix} Good message")
                .setStatus(Status.GOOD)
                .setLevel(Level.INFO)
                .toConsole();
        new StrFmt("{prefix} Bad message")
                .setStatus(Status.BAD)
                .setLevel(Level.INFO)
                .toConsole();

        StrFmt.fromLocalized("core.plugin-enable-success").setLevel(Level.INFO).toConsole();

    }


    /**
     * Disables the plugin by gracefully shutting down opened connections & pending events.
     */
    @Override
    public void onDisable() {
        StrFmt.fromLocalized("core.plugin-disable-start").setLevel(Level.INFO).toConsole();
        StrFmt.fromLocalized("core.plugin-disable-success").setLevel(Level.INFO).toConsole();
    }


    // ======================   HELPERS

    /**
     * Panics (Disables) the plugin. Should be used in case of a fatal exception!
     */
    public void panic(String message) {
        new StrFmt("{prefix} Panic: " + message + "! (ノಠ益ಠ)ノ彡┻━┻").setLevel(Level.FATAL).toConsole();
        Bukkit.getPluginManager().disablePlugin(this);
    }

}
