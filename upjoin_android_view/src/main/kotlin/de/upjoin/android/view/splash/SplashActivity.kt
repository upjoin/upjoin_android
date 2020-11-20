package de.upjoin.android.view.splash

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import de.upjoin.android.view.modules.splashModule
import de.upjoin.android.view.splash.SplashStatus.LoadingState

open class SplashActivity: AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(splashModule.contentView)

        SplashStatus.observe(this, {
            if (it == LoadingState.FINISHED) close()
        })
        viewModel.load(this)
    }

    protected fun close() {
        finish()
    }

}