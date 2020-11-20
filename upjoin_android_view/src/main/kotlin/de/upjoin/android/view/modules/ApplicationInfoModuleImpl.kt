package de.upjoin.android.view.modules

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.annotation.RequiresPermission
import de.upjoin.android.actions.ActionExecutor
import de.upjoin.android.core.application.AppInfoModule
import de.upjoin.android.core.application.ModulizedApplication
import de.upjoin.android.core.application.appInfoModule
import de.upjoin.android.core.modules.ModuleLiveCycle

abstract class ApplicationInfoModuleImpl: ModuleLiveCycle, AppInfoModule, Application.ActivityLifecycleCallbacks {

    lateinit var applicationContext: Context

    var foregroundActivityName: String? = null

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    override fun onCreate(application: ModulizedApplication) {
        appInfoModule = this
        applicationInfoModule = this
        applicationContext = application.applicationContext
        application.registerActivityLifecycleCallbacks(this)
    }

    fun isForeground(): Boolean {
        return foregroundActivityName !=null
    }

    fun isBackground(): Boolean {
        return foregroundActivityName ==null
    }

    override fun onActivityPaused(activity: Activity) {
        foregroundActivityName = null
    }

    override fun onActivityResumed(activity: Activity) {
        foregroundActivityName = activity.localClassName
    }

    override fun onActivityDestroyed(activity: Activity) {
        ActionExecutor.cancelActionsForContext(activity)
    }

    override fun onActivityStarted(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
}

/**
 * global singleton instance
 */
lateinit var applicationInfoModule: ApplicationInfoModuleImpl