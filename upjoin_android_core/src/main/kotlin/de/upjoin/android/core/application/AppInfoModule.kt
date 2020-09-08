package de.upjoin.android.core.application

/**
 * This module grands access to general application information.
 */
interface AppInfoModule {

    /**
     * true if the app runs in debug mode
     */
    val isDebug: Boolean

    /**
     * version string of this app
     */
    val appVersion: String

    companion object {

        /**
         * Module ID used for module registration
         */
        const val MODULE_ID = "AppInfoModule"
    }
}

/**
 * global singleton instance
 */
lateinit var appInfoModule: AppInfoModule