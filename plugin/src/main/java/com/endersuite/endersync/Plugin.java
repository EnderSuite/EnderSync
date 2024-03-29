package com.endersuite.endersync;

import com.dieselpoint.norm.Database;
import com.endersuite.database.configuration.Credentials;
import com.endersuite.database.mysql.MySQLDatabase;
import com.endersuite.database.mysql.Row;
import com.endersuite.endersync.bukkit.listener.PlaceholderItemListener;
import com.endersuite.endersync.config.FeaturesJsonConfiguration;
import com.endersuite.endersync.config.MainConfiguration;
import com.endersuite.endersync.database.models.Player;
import com.endersuite.endersync.database.models.Settings;
import com.endersuite.endersync.module.core.InventoryModule;
import com.endersuite.endersync.module.core.PlayerGameModeModule;
import com.endersuite.endersync.module.core.PlayerHealthModule;
import com.endersuite.endersync.bukkit.listener.PlayerJoinListener;
import com.endersuite.endersync.bukkit.listener.PlayerLeaveListener;
import com.endersuite.endersync.event.core.PlayerSaveEvent;
import com.endersuite.endersync.event.core.PlayerSynchronizeEvent;
import com.endersuite.endersync.event.handler.PlayerSaveEventHandler;
import com.endersuite.endersync.event.handler.PlayerSynchronizeEventHandler;
import com.endersuite.endersync.exceptions.DuplicateModuleNameException;
import com.endersuite.endersync.exceptions.InvalidModuleNameException;
import com.endersuite.endersync.module.ModuleManager;
import com.endersuite.endersync.networking.handler.core.CachePlayerDataPacketHandler;
import com.endersuite.endersync.networking.handler.player.RequestIsPlayerOnlinePacketHandler;
import com.endersuite.endersync.packets.core.CachePlayerDataPacket;
import com.endersuite.endersync.packets.core.player.RequestIsPlayerOnlinePacket;
import com.endersuite.libcore.config.ConfigManager;
import com.endersuite.libcore.plugin.EnderPlugin;
import com.endersuite.libcore.plugin.bootstrap.PluginBootstrap;
import com.endersuite.libcore.strfmt.Level;
import com.endersuite.libcore.strfmt.Status;
import com.endersuite.libcore.strfmt.StrFmt;
import com.endersuite.packify.NetworkManager;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import de.maximilianheidenreich.jeventloop.EventLoop;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
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

    // REFERENCES
    @Getter private ConfigManager configManager;
    @Getter private NetworkManager networkManager;
    @Getter private Database db;
    //@Getter private ModuleManager moduleManager;
    //@Getter private DataRaceManager dataRaceManager;
    @Getter private EventLoop eventLoop;
    //@Getter private MySQLDatabase db;    // TODO: Come up with a good name / place cuz Bukkit Plugin class already impl getDatabase()!

    /**
     * The plugin folder (plugins/EnderSync).
     */
    @Getter
    private Path pluginDataFolder;


    /**
     * The cache that stores player data received over the network.
     * Note: Player data is stored a a Map with specific keys for each SyncModule TODO: finish docs
     */
    @Getter
    private Cache<UUID, Map<String, Row>> playerDataCache;

    @Getter
    private Cache<UUID, CompletableFuture<Map<String, Row>>> cacheCallbacks;


    // ======================   BUKKIT LOGIC

    /**
     * Enables the plugin duh!
     * Just look at the code, cuz I won't write a doc string for it :)
     */
    @Override
    public void onEnable() {
        this.eventLoop = new EventLoop();
        this.configManager = new ConfigManager();
        //this.moduleManager = new ModuleManager();
        //this.dataRaceManager = new DataRaceManager();

        StrFmt.prefix = "{level} §l§7» §l§3Ender§l§fSync§r {status} : ";

        // Setup data folder
        this.pluginDataFolder = Bukkit.getPluginManager().getPlugin("EnderSync").getDataFolder().toPath();
        File pluginFolder = pluginDataFolder.toFile();
        if (!pluginFolder.exists() && !pluginFolder.mkdir()) {
            panic("Could not create plugin data folder");
            return;
        }

        // Load config file
        try {
            getConfigManager().loadJson("config", MainConfiguration.class, pluginDataFolder.resolve("config.json"));
            //getConfigManager().load("config", pluginDataFolder.resolve("config.yml"), this);
        } catch (IOException e) {
            panic("Could not load config.yml", e);
            return;
        }
        StrFmt.outputLevel = ((MainConfiguration) getConfigManager().getJson("config")).logLevel;

        // Load lang file
        String langFile = "lang-" + ((MainConfiguration) getConfigManager().getJson("config")).lang + ".yml";
        try {
            getConfigManager().load("lang", pluginDataFolder.resolve(langFile), this);
        } catch (IOException e) {
            panic("Could not load " + langFile, e);
            return;
        }
        StrFmt.localizationConfig = getConfigManager().get("lang");
        //StrFmt.fromLocalized("debug.lang-test").setLevel(Level.DEBUG).toLog();

        // Load features file
        /*try {
            getConfigManager().loadJson("features", FeaturesJsonConfiguration.class, pluginDataFolder.resolve("features.json"));
        } catch (IOException e) {
            panic("Could not load features.yml", e);
            return;
        }*/

        // Setup cache
        /*this.playerDataCache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS) // TODO: Extract from config
                .maximumSize(2_000)     // TODO: Extract to config?
                .build();

        this.cacheCallbacks = Caffeine.newBuilder()
                .expireAfterAccess(0, TimeUnit.NANOSECONDS) // Good? or longer time but manual evict inside callback?
                .maximumSize(2_000)
                .build();*/

        // Connect to network
        /*try {
            throw new Exception("test");
            this.networkManager = new NetworkManager(getEventLoop(), ((MainConfiguration) getConfigManager().getJson("config")).networking.nodeName);
            getNetworkManager().connect("endersync_cluster");

            // Register packet handlers
            getNetworkManager().addPacketHandler(CachePlayerDataPacket.class, CachePlayerDataPacketHandler::handle);
            getNetworkManager().addPacketHandler(RequestIsPlayerOnlinePacket.class, RequestIsPlayerOnlinePacketHandler::handle);
        } catch (Exception e) {
            new StrFmt("{prefix} Could not initialize JGroup networking! Database fallback will be used! This might affect performance!", e).setLevel(Level.ERROR).toLog();
        }*/

        // Connect to db
        // TODO: Fatal if no network & db fail
        /*{
            MainConfiguration config = getConfigManager().getJson("config");
            Credentials credentials = new Credentials(
                    config.database.host,
                    config.database.database,
                    config.database.username,
                    config.database.password,
                    config.database.port,
                    config.database.useSSL,
                    false
            );
            try {
                this.db = new MySQLDatabase(credentials);
                //this.db.connect();    // TODO: change db lib to throw error
            } catch (Exception e) {
                panic("Could not connect to database at " + this.db.toString(), e);  // TODO: Change to conn URI
                return;
            }
            //this.db.execUpdate("CREATE TABLE IF NOT EXISTS player_locks (uuid varchar(36) not null, locked BOOLEAN not null, PRIMARY KEY (uuid))");
        }*/

        // Load default modules
        //addDefaultModules();

        // Load extensions

        {
            // Output test TODO: Remove
            new StrFmt("{prefix} Trace message with %s", "formatter replacement lul")
                    .setLevel(Level.TRACE)
                    .toLog();
            new StrFmt("{prefix} Debug message")
                    .setLevel(Level.DEBUG)
                    .toLog();
            new StrFmt("{prefix} Info message")
                    .setLevel(Level.INFO)
                    .toLog();
            new StrFmt("{prefix} Warning message")
                    .setLevel(Level.WARN)
                    .toLog();
            new StrFmt("{prefix} error message")
                    .setLevel(Level.ERROR)
                    .toLog();
            new StrFmt("{prefix} fatal message")
                    .setLevel(Level.FATAL)
                    .toLog();

            new StrFmt("{prefix} Progress message")
                    .setStatus(Status.PROGRESS)
                    .setLevel(Level.INFO)
                    .toLog();
            new StrFmt("{prefix} Good message")
                    .setStatus(Status.GOOD)
                    .setLevel(Level.INFO)
                    .toLog();
            new StrFmt("{prefix} Bad message")
                    .setStatus(Status.BAD)
                    .setLevel(Level.INFO)
                    .toLog();
        }


        db = new Database();
        db.setJdbcUrl("jdbc:mysql://localhost:3306/endersuite?useSSL=false");
        db.setUser("root");
        db.setPassword("esDev");

        Long cnt = db.sql("SELECT count(*) FROM information_schema.tables WHERE table_name = 'endersync_players' LIMIT 1;").first(Long.class);
        new StrFmt("Cnt: " + cnt, Level.INFO).toLog();
        if (cnt <= 0) db.createTable(Player.class);

        // Register event handlers
        getEventLoop().addEventHandler(PlayerSaveEvent.class, PlayerSaveEventHandler::onPlayerSaveEvent);
        getEventLoop().addEventHandler(PlayerSynchronizeEvent.class, PlayerSynchronizeEventHandler::onPlayerSyncEvent);

        getEventLoop().start();

        // Register listeners
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(), this);
        pluginManager.registerEvents(new PlayerLeaveListener(), this);
        pluginManager.registerEvents(new PlaceholderItemListener(), this);

        StrFmt.fromLocalized("core.plugin-enable-success").setLevel(Level.INFO).toLog();

    }

    /**
     * Disables the plugin by gracefully shutting down opened connections and pending events.
     */
    @Override
    public void onDisable() {
        StrFmt.fromLocalized("core.plugin-disable-start").setLevel(Level.INFO).toLog();

        this.networkManager.disconnect();
        //this.db.disconnect();

        StrFmt.fromLocalized("core.plugin-disable-success").setLevel(Level.INFO).toLog();
    }


    // ======================   HELPERS

    /**
     * Panics (Disables) the plugin. Should be used in case of a fatal exception!
     *
     * @param message
     */
    public void panic(String message) {
        new StrFmt("{prefix} Panic: %s! (ノಠ益ಠ)ノ彡┻━┻", message).setLevel(Level.FATAL).toLog();
        Bukkit.getPluginManager().disablePlugin(this);
    }

    /**
     * Panics (Disables) the plugin and Prints stacktrace. Should be used in case of a fatal exception!
     *
     * @param message
     * @param throwable
     */
    public void panic(String message, Throwable throwable) {
        throwable.printStackTrace();
        this.panic(message);
    }

    /*private void addDefaultModules() {
        // TODO: Config check
        try {
            getModuleManager().addModule(new InventoryModule());
            getModuleManager().addModule(new PlayerHealthModule());
            getModuleManager().addModule(new PlayerGameModeModule());
        } catch (DuplicateModuleNameException | InvalidModuleNameException e) {
            e.printStackTrace();
        }
    }*/


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
