package com.endersuite.endersync.modules;

import com.endersuite.endersync.exceptions.DuplicateModuleNameException;
import com.endersuite.endersync.exceptions.InvalidModuleNameException;
import com.endersuite.endersync.exceptions.ModuleNotFoundException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages {@link AbstractSynchronizedPlayerModule}s and {@link AbstractSynchronizedDataModule}s.
 *
 * @author Maximilian Vincent Heidenreich
 * @since 10.05.21
 */
public class ModuleManager {

    // ======================   VARS

    /**
     * The ModuleManager singleton.
     */
    private static ModuleManager instance;

    /**
     * The active playerSyncModules which will get processed on save / sync.
     */
    @Getter
    private final List<AbstractSynchronizedPlayerModule> activePlayerModules;

    /**
     * The active dataSyncModules which will get processed on interval.
     */
    @Getter
    private final List<AbstractSynchronizedDataModule> activeDataModules;


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
    public void addModule(AbstractSynchronizedModule module) throws DuplicateModuleNameException, InvalidModuleNameException {
        if (getActiveModuleByName(module.getName()) != null)
            throw new DuplicateModuleNameException(module);

        // Validate name
        String name = module.getName();
        char[] invalidNameChars = new char[]{ ' ', '`', '´', '^', '^', '°', '%', '&', '(', ')', '{', '}', '+', '-', '/', '\\', '[', ']', '"', '\'' };
        for (char c : invalidNameChars) {
            if (name.indexOf(c) != -1)
                throw new InvalidModuleNameException("No '" + c + "' allowed!", module);
        }

        if (module instanceof AbstractSynchronizedDataModule)
            activeDataModules.add((AbstractSynchronizedDataModule) module); // TODO: Add scheduler to dispatch event
        else if (module instanceof AbstractSynchronizedPlayerModule)
            activePlayerModules.add((AbstractSynchronizedPlayerModule) module);
    }

    /**
     * Removes a module from the active modules list.
     *
     * @param module
     *          The module to remove
     * @throws ModuleNotFoundException
     *          When the module was not found inside the active modules list
     */
    public void removeModule(AbstractSynchronizedModule module) throws ModuleNotFoundException {
        if (module instanceof AbstractSynchronizedDataModule) {
            if (!activeDataModules.remove(module))
                throw new ModuleNotFoundException(module);
        }
        else if (module instanceof AbstractSynchronizedPlayerModule) {
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
    public AbstractSynchronizedModule getActiveModuleByName(String name) {
        AbstractSynchronizedDataModule dMod = activeDataModules.stream().filter(e -> e.getName().equals(name)).findFirst().orElse(null);
        AbstractSynchronizedPlayerModule pMod = activePlayerModules.stream().filter(e -> e.getName().equals(name)).findFirst().orElse(null);
        return dMod != null ? dMod : pMod;
    }

    /**
     * Returns the ModuleManager singleton instance.
     * Note: Also creates one if none exists.
     *
     * @return The ModuleManager instance.
     */
    public static ModuleManager getInstance() {
        if (ModuleManager.instance == null)
            ModuleManager.instance = new ModuleManager();

        return ModuleManager.instance;

    }

}
