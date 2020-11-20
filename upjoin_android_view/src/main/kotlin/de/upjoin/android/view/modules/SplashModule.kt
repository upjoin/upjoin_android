package de.upjoin.android.view.modules

import android.content.Context
import de.upjoin.android.view.actions.splash.AbstractLoadApplicationAction

interface SplashModule {

    val contentView: Int
    var visibleTime: Int

    fun createLoadApplicationAction(context: Context): AbstractLoadApplicationAction

    fun showSplashActivity(context: Context)

    companion object {
        const val MODULE_ID = "SplashModule"
    }
}
lateinit var splashModule: SplashModule
