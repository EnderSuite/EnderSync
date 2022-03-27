package com.endersuite.endersync.exceptions;

import com.endersuite.endersync.module.ASynchronizedModule;
import com.endersuite.endersync.module.ModuleManager;
import lombok.Getter;

/**
 * Thrown when trying to add a module with an invalid name using {@link ModuleManager#addModule(ASynchronizedModule)}.
 *
 * @author Maximilian Vincent Heidenreich
 * @since 10.05.21
 */
public class InvalidModuleNameException extends EnderSyncException {

    @Getter
    private final String reason;

    @Getter
    private final ASynchronizedModule module;

    @Getter
    private final String moduleName;

    public InvalidModuleNameException(String reason, ASynchronizedModule module) {
        this.reason = reason;
        this.module = module;
        this.moduleName = module.getName();
    }

}
