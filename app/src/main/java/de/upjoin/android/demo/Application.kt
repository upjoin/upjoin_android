package de.upjoin.android.demo

import de.upjoin.android.core.application.AppInfoModule
import de.upjoin.android.core.application.ModulizedApplication

class Application: ModulizedApplication() {

    init {
        moduleLiveCycles[AppInfoModule.MODULE_ID] = AppInfoModuleImpl::class.java
    }
}