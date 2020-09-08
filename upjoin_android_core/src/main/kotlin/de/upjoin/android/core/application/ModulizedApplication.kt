package de.upjoin.android.core.application

import de.upjoin.android.core.modules.ModuleLiveCycle

/**
 * allows to register modules on the app. Each module extends the application's lifecycle.
 */
open class ModulizedApplication: android.app.Application() {

    /**
     * registered module classes map
     */
    val moduleLiveCycles = mutableMapOf<String, Class<out ModuleLiveCycle>>()

    private val instantiatedModules = mutableMapOf<String, ModuleLiveCycle>()

    override fun onCreate() {
        super.onCreate()

        // instantiate each module and call onCreate lifecycle hook
        moduleLiveCycles.entries.forEach {
            val module = it.value.getConstructor().newInstance()
            module.onCreate(this)
            instantiatedModules[it.key] = module
        }
    }

    override fun onTerminate() {
        // terminate and free modules
        instantiatedModules.values.forEach {
            it.onTerminate(this)
        }
        instantiatedModules.clear()

        super.onTerminate()
    }

}