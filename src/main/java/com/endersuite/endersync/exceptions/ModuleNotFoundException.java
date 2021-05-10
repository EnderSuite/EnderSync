package com.endersuite.endersync.exceptions;

import com.endersuite.endersync.modules.AbstractSynchronizedModule;
import lombok.Getter;

/**
 * Thrown when a requested module could not be found inside the active modules.
 * Example: Requesting sync of a module by reference but the module has
 *          not been added using {@link com.endersuite.endersync.modules.ModuleManager#addModule(AbstractSynchronizedModule)}.
 *
 * @author Maximilian Vincent Heidenreich
 * @since 10.05.21
 */
public class ModuleNotFoundException extends EnderSyncException {

    @Getter
    private final AbstractSynchronizedModule module;

    public ModuleNotFoundException(AbstractSynchronizedModule module) {
        this.module = module;
    }

}
