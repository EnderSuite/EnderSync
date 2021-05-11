package com.endersuite.endersync;

import com.endersuite.database.mysql.Row;
import com.endersuite.endersync.bukkit.listeners.PlayerJoinListener;
import com.endersuite.endersync.bukkit.listeners.PlayerLeaveListener;
import com.endersuite.endersync.config.FeaturesJsonConfiguration;
import com.endersuite.endersync.events.core.PacketReceivedEvent;
import com.endersuite.endersync.events.core.PlayerSaveEvent;
import com.endersuite.endersync.events.core.PlayerSynchronizeEvent;
import com.endersuite.endersync.events.handlers.PlayerSaveEventHandler;
import com.endersuite.endersync.events.handlers.PlayerSynchronizeEventHandler;
import com.endersuite.endersync.exceptions.DuplicateModuleNameException;
import com.endersuite.endersync.exceptions.InvalidModuleNameException;
import com.endersuite.endersync.modules.ModuleManager;
import com.endersuite.endersync.modules.core.PlayerGamemodeModule;
import com.endersuite.endersync.modules.core.PlayerHealthModule;
import com.endersuite.endersync.networking.NetworkManager;
import com.endersuite.endersync.networking.handlers.CachePlayerDataPacketHandler;
import com.endersuite.endersync.networking.packets.core.CachePlayerDataPacket;
import com.endersuite.libcore.config.ConfigManager;
import com.endersuite.libcore.file.ResourceUtil;
import com.endersuite.libcore.plugin.EnderPlugin;
import com.endersuite.libcore.plugin.bootstrap.PluginBootstrap;
import com.endersuite.libcore.strfmt.Level;
import com.endersuite.libcore.strfmt.Status;
import com.endersuite.libcore.strfmt.StrFmt;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import de.maximilianheidenreich.jeventloop.EventLoop;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * TODO: Add docs
 *
 * @author Maximilian Vincent Heidenreich
 * @since 11.05.21
 */
public class Plugin extends EnderPlugin {

    public Plugin(PluginBootstrap plugin) {
        super(plugin);
        Plugin.instance = this;
    }

    // ======================   VARS

    /**
     * The plugin singleton.
     */
    private static Plugin instance;

    /**
     * The plugin folder (plugins/EnderSync).
     */
    @Getter
    private Path pluginDataFolder;

    @Getter
    private Path pluginDepsFolder;

    /**
     * The eventloop used throughout the plugin.
     */
    @Getter
    private EventLoop eventLoop;

    /**
     * The cache that stores player data received over the network.
     * Note: Player data is stored a a Map with specific keys for each SyncModule TODO: finish docs
     */
    @Getter
    private Cache<UUID, Map<String, Row>> playerDataCache;


    // ======================   BUKKIT LOGIC

    /**
     * Enables the plugin duh!
     * Just look at the code, cuz I won't write a doc string for it :)
     */
    @Override
    public void onEnable() {
        this.eventLoop = new EventLoop();
        ModuleManager.getInstance();

        StrFmt.prefix = "{level} §l§7» §l§3Ender§l§fSync§r {status} : ";
        StrFmt.outputLevel = Level.TRACE;       // Dev only

        //new StrFmt("{prefix} Java version §a" + System.getProperty("java.version") + "§r is supported!").setLevel(Level.INFO).toConsole();

        // Setup data folder
        pluginDataFolder = Bukkit.getPluginManager().getPlugin("EnderSync").getDataFolder().toPath();
        File pluginFolder = pluginDataFolder.toFile();
        if (!pluginFolder.exists() && !pluginFolder.mkdir()) {
            panic("Could not create plugin data folder");
            return;
        }

        // Setup deps folder
        pluginDepsFolder = pluginDataFolder.resolve("deps");
        File depsFolder = pluginDepsFolder.toFile();
        if (!depsFolder.exists() && !depsFolder.mkdir()) {
            panic("Could not create plugin deps folder");
            return;
        }

        try {
            ResourceUtil.extractResource("deps.txt", pluginDataFolder.resolve("deps.txt"), this);
        } catch (IOException e) {
            panic("Could not extract deps.txt resouce!", e);
            return;
        }

        // Injects
        //new ClassPathInjector(depsFolder, pluginDataFolder.resolve("deps.txt").toFile());

        // Load config file
        try {
            ConfigManager.getInstance().load("config", pluginDataFolder.resolve("config.yml"), this);
        } catch (IOException exception) {
            exception.printStackTrace();
            panic("Could not load config.yml");
            return;
        }
        StrFmt.outputLevel = Level.valueOf(ConfigManager.getInstance().get("config").getString("core.log-level").toUpperCase());

        // Load lang file
        String langFile = "lang-" + ConfigManager.getInstance().get("config").getString("core.lang") + ".yml";
        try {
            ConfigManager.getInstance().load("lang", pluginDataFolder.resolve(langFile), this);
        } catch (IOException exception) {
            exception.printStackTrace();
            panic("Could not load " + langFile);
            return;
        }
        StrFmt.localizationConfig = ConfigManager.getInstance().get("lang");
        StrFmt.fromLocalized("debug.lang-test").setLevel(Level.DEBUG).toConsole();

        // Load features file
        /*try {
            ConfigManager.getInstance().load("features", pluginDataFolder.resolve("features.yml"), this);
        } catch (IOException exception) {
            exception.printStackTrace();
            panic("Could not load features.yml");
            return;
        }*/
        try {
            ConfigManager.getInstance().loadJson("features", FeaturesJsonConfiguration.class, pluginDataFolder.resolve("features.json"));
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        // Load default modules
        addDefaultModules();

        // Setup cache
        playerDataCache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS) // TODO: Extract from config
                .maximumSize(2_000)     // TODO: Extract to config?
                .build();

        // Load extensions

        // Connect to db

        // Connect to network
        NetworkManager networkManager = NetworkManager.getInstance();
        try {
            networkManager.connect("endersync_cluster");
        } catch (Exception exception) {
            exception.printStackTrace();
        }

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

        // Register packet handlers
        networkManager.addPacketHandler(CachePlayerDataPacket.class, CachePlayerDataPacketHandler::handleCachePlayerDataPacket);

        // Register event handlers
        eventLoop.addEventHandler(PlayerSaveEvent.class, PlayerSaveEventHandler::onPlayerSaveEvent);
        eventLoop.addEventHandler(PlayerSynchronizeEvent.class, PlayerSynchronizeEventHandler::onPlayerSyncEvent);
        eventLoop.addEventHandler(PacketReceivedEvent.class, NetworkManager.getInstance()::handlePacketReceivedEvent);

        eventLoop.start();

        // Register listeners
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(), this);
        pluginManager.registerEvents(new PlayerLeaveListener(), this);

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

    /**
     * Panics (Disables) the plugin & Prints stacktrace. Should be used in case of a fatal exception!
     */
    public void panic(String message, Throwable throwable) {
        throwable.printStackTrace();
        this.panic(message);
    }

    private void addDefaultModules() {
        // TODO: Config check
        try {
            ModuleManager.getInstance().addModule(new PlayerHealthModule());
            ModuleManager.getInstance().addModule(new PlayerGamemodeModule());
        } catch (DuplicateModuleNameException | InvalidModuleNameException e) {
            e.printStackTrace();
        }
    }


    // ======================   GETTER & SETTER

    /**
     * Returns the singleton instance of the Main class.
     *
     * @return The singleton instance of the Main class.
     */
    public static Plugin getPlugin() {
        return Plugin.instance;
    }

}
