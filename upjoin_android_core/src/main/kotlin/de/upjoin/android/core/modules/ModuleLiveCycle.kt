package de.upjoin.android.core.modules

import de.upjoin.android.core.application.ModulizedApplication

/**
 * Module interface
 */
interface ModuleLiveCycle {

    /**
     * Application onCreate hook
     */
    fun onCreate(application: ModulizedApplication)

    /**
     * Application onTerminate hook
     */
    fun onTerminate(application: ModulizedApplication) {}
}