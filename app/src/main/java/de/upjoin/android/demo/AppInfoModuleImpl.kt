package de.upjoin.android.demo

import de.upjoin.android.core.BuildConfig
import de.upjoin.android.core.application.AppInfoModule
import de.upjoin.android.core.application.ModulizedApplication
import de.upjoin.android.core.application.appInfoModule
import de.upjoin.android.core.modules.ModuleLiveCycle

class AppInfoModuleImpl: AppInfoModule, ModuleLiveCycle {

    override val isDebug = BuildConfig.BUILD_TYPE == "debug"
    override val appVersion = BuildConfig.VERSION_NAME

    override fun onCreate(application: ModulizedApplication) {
        appInfoModule = this
    }

}