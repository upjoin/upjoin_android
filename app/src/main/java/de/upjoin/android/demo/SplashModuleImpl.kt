package de.upjoin.android.demo

import android.content.Context
import de.upjoin.android.demo.actions.splash.LoadApplicationAction
import de.upjoin.android.view.modules.AbstractSplashModule

class SplashModuleImpl: AbstractSplashModule() {

    override val contentView: Int = R.layout.activity_splash

    override fun createLoadApplicationAction(context: Context) = LoadApplicationAction(context)

}