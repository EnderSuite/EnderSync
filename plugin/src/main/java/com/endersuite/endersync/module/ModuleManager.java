package com.endersuite.endersync.module;

import com.endersuite.endersync.Plugin;
import com.endersuite.endersync.config.FeaturesJsonConfiguration;
import com.endersuite.endersync.exceptions.DuplicateModuleNameException;
import com.endersuite.endersync.exceptions.InvalidModuleNameException;
import com.endersuite.endersync.exceptions.ModuleNotFoundException;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages {@link ASynchronizedPlayerModule}s and {@link ASynchronizedDataModule}s.
 *
 * @author Maximilian Vincent Heidenreich
 * @since 10.05.21
 */
public class ModuleManager {

    // ======================   VARS

    /**
     * The active playerSyncModules which will get processed on save / sync.
     */
    @Getter
    private final List<ASynchronizedPlayerModule> activePlayerModules;

    /**
     * The active dataSyncModules which will get processed on interval.
     */
    @Getter
    private final List<ASynchronizedDataModule> activeDataModules;


    // ======================   CONSTRUCTOR

    public ModuleManager() {
        this.activePlayerModules = new ArrayList<>();
        this.activeDataModules = new ArrayList<>();
    }


    // ======================   BUSINESS LOGIC

    /**
     * Adds a module to the active modules list.
     *
     * @param module
     *          The module to add
     * @throws DuplicateModuleNameException
     *          When another previously added module already uses the same name
     * @throws InvalidModuleNameException
     *          When one or more invalid character were detected inside the name
     */
    public void addModule(ASynchronizedModule module) throws DuplicateModuleNameException, InvalidModuleNameException {
        if (getActiveModuleByName(module.getName()) != null)
            throw new DuplicateModuleNameException(module);

        // Validate name
        String name = module.getName();
        char[] invalidNameChars = new char[]{ ' ', '`', '´', '^', '^', '°', '%', '&', '(', ')', '{', '}', '+', '-', '/', '\\', '[', ']', '"', '\'' };
        for (char c : invalidNameChars) {
            if (name.indexOf(c) != -1)
                throw new InvalidModuleNameException("No '" + c + "' allowed!", module);
        }

        /*FeaturesJsonConfiguration features = Plugin.getPlugin().getConfigManager().getJson("features");
        if (!features.modules.containsKey(module.getName()) && module.getModuleConfigurationClass() != null) {
            try {
                features.modules.put(module.getName(), module.getModuleConfigurationClass().asSubclass(FeaturesJsonConfiguration.ModuleConfiguration.class).getDeclaredConstructor().newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            Plugin.getPlugin().getConfigManager().saveJson(features, Plugin.getPlugin().getPluginDataFolder().resolve("features.json").toFile());
        }
        if (features.modules.containsKey(module.getName())) {
            module.moduleConfiguration = features.modules.get(module.getName());
        }*/

        // Setup db
        /*try {
            boolean result;
            if (Plugin.getPlugin().getDb().isConnected())
                result = module.setupDatabase();
        }
        catch (Exception e) {
            e.printStackTrace();    // TODO: REMOVE
        }*/


        if (module instanceof ASynchronizedDataModule)
            activeDataModules.add((ASynchronizedDataModule) module); // TODO: Add scheduler to dispatch event
        else if (module instanceof ASynchronizedPlayerModule)
            activePlayerModules.add((ASynchronizedPlayerModule) module);
    }

    /**
     * Removes a module from the active modules list.
     *
     * @param module
     *          The module to remove
     * @throws ModuleNotFoundException
     *          When the module was not found inside the active modules list
     */
    public void removeModule(ASynchronizedModule module) throws ModuleNotFoundException {
        if (module instanceof ASynchronizedDataModule) {
            if (!activeDataModules.remove(module))
                throw new ModuleNotFoundException(module);
        }
        else if (module instanceof ASynchronizedPlayerModule) {
            if (!activePlayerModules.remove(module))
                throw new ModuleNotFoundException(module);
        }
    }

    // ======================   HELPERS

    protected void addDefaultModules() {}

    /**
     * Returns the AbstractModule identified by the specified name.
     * Note: Always returns DataSyncModules first, but due to the implementation,
     *       there can never be a PlayerSyncModule with the same name as a DataSyncModule!
     * @param name
     *          The name identifying the module
     * @return
     *          The module | {@code null} if none were found
     */
    public ASynchronizedModule getActiveModuleByName(String name) {
        ASynchronizedDataModule dMod = activeDataModules.stream().filter(e -> e.getName().equals(name)).findFirst().orElse(null);
        ASynchronizedPlayerModule pMod = activePlayerModules.stream().filter(e -> e.getName().equals(name)).findFirst().orElse(null);
        return dMod != null ? dMod : pMod;
    }

}
