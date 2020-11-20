package de.upjoin.android.view.modules

import android.content.Context
import android.content.Intent
import de.upjoin.android.core.application.ModulizedApplication
import de.upjoin.android.core.modules.ModuleLiveCycle
import de.upjoin.android.view.splash.SplashActivity

abstract class AbstractSplashModule: SplashModule, ModuleLiveCycle {

    override var visibleTime: Int = 1000

    override fun showSplashActivity(context: Context) {
        val intent = Intent(context, SplashActivity::class.java)
        context.startActivity(intent)
    }

    override fun onCreate(application: ModulizedApplication) {
        splashModule = this
    }
}