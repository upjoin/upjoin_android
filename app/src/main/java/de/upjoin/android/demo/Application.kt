package de.upjoin.android.demo

import de.upjoin.android.actions.ActionModule
import de.upjoin.android.actions.ActionModuleImpl
import de.upjoin.android.core.application.AppInfoModule
import de.upjoin.android.core.application.ModulizedApplication
import de.upjoin.android.view.modules.NetworkCallbackModule
import de.upjoin.android.view.modules.NetworkCallbackModuleImpl
import de.upjoin.android.view.modules.PreferencesRepository
import de.upjoin.android.view.modules.SplashModule

class Application: ModulizedApplication() {

    init {
        moduleLiveCycles[ActionModule.MODULE_ID] = ActionModuleImpl::class.java
        moduleLiveCycles[AppInfoModule.MODULE_ID] = AppInfoModuleImpl::class.java
        moduleLiveCycles[PreferencesRepository.MODULE_ID] = PreferencesRepository::class.java
        moduleLiveCycles[NetworkCallbackModule.MODULE_ID] = NetworkCallbackModuleImpl::class.java
        moduleLiveCycles[SplashModule.MODULE_ID] = SplashModuleImpl::class.java
    }
}