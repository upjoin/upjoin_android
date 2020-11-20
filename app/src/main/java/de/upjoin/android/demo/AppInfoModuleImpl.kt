package de.upjoin.android.demo

import de.upjoin.android.view.modules.ApplicationInfoModuleImpl

class AppInfoModuleImpl: ApplicationInfoModuleImpl() {

    override val isDebug = BuildConfig.BUILD_TYPE == "debug"
    override val appVersion = BuildConfig.VERSION_NAME

}