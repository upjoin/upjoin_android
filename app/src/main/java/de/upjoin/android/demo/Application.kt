package de.upjoin.android.demo

import de.upjoin.android.actions.ActionModule
import de.upjoin.android.actions.ActionModuleImpl
import de.upjoin.android.core.application.AppInfoModule
import de.upjoin.android.core.application.ModulizedApplication

class Application: ModulizedApplication() {

    init {
        moduleLiveCycles[ActionModule.MODULE_ID] = ActionModuleImpl::class.java
        moduleLiveCycles[AppInfoModule.MODULE_ID] = AppInfoModuleImpl::class.java
    }
}