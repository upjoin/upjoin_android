package de.upjoin.android.core.application

import de.upjoin.android.core.modules.ModuleLiveCycle

open class ModulizedApplication: android.app.Application() {

    val moduleLiveCycles = mutableMapOf<String, Class<out ModuleLiveCycle>>()
    private val instantiatedModules = mutableMapOf<String, ModuleLiveCycle>()

    override fun onCreate() {
        super.onCreate()

        moduleLiveCycles.entries.forEach {
            val module = it.value.getConstructor().newInstance()
            module.onCreate(this)
            instantiatedModules[it.key] = module
        }

    }

    override fun onTerminate() {
        instantiatedModules.values.forEach {
            it.onTerminate(this)
        }

        super.onTerminate()
    }

}