package de.upjoin.android.core.modules

import de.upjoin.android.core.application.ModulizedApplication

interface ModuleLiveCycle {

    fun onCreate(application: ModulizedApplication)

    fun onTerminate(application: ModulizedApplication) {}
}