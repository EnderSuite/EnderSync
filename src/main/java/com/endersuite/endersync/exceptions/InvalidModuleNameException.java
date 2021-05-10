package com.endersuite.endersync.exceptions;

import com.endersuite.endersync.modules.AbstractSynchronizedModule;
import lombok.Getter;

/**
 * Thrown when trying to add a module with an invalid name using {@link com.endersuite.endersync.modules.ModuleManager#addModule(AbstractSynchronizedModule)}.
 *
 * @author Maximilian Vincent Heidenreich
 * @since 10.05.21
 */
public class InvalidModuleNameException extends EnderSyncException {

    @Getter
    private final String reason;

    @Getter
    private final AbstractSynchronizedModule module;

    @Getter
    private final String moduleName;

    public InvalidModuleNameException(String reason, AbstractSynchronizedModule module) {
        this.reason = reason;
        this.module = module;
        this.moduleName = module.getName();
    }

}
