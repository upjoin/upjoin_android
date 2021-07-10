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
            instantiatedModules[it.key] = module
        }

        instantiatedModules.values.sortedBy { it.priority }.forEach { module ->
            module.onCreate(this)
        }
    }

    override fun onTerminate() {
        // terminate and free modules
        instantiatedModules.values.sortedByDescending { it.priority }.forEach {
            it.onTerminate(this)
        }
        instantiatedModules.clear()

        super.onTerminate()
    }

}
