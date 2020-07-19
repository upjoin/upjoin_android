package de.upjoin.android.core.application

/**
 * This module grands access to general application information.
 */
interface AppInfoModule {

    val isDebug: Boolean
    val appVersion: String

    companion object {
        const val MODULE_ID = "AppInfoModule"

        lateinit var instance: AppInfoModule
    }
}

val appInfoModule: AppInfoModule by lazy { AppInfoModule.instance }