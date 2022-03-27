package com.endersuite.endersync.exceptions;

import com.endersuite.endersync.module.ASynchronizedModule;
import lombok.Getter;

/**
 * Thrown when trying to add a SyncModule but another previously added module already has the same name.
 *
 * @author Maximilian Vincent Heidenreich
 * @since 10.05.21
 */
public class DuplicateModuleNameException extends EnderSyncException {

    @Getter
    private final ASynchronizedModule module;

    @Getter
    private final String moduleName;

    public DuplicateModuleNameException(ASynchronizedModule module) {
        this.module = module;
        this.moduleName = module.getName();
    }

}
