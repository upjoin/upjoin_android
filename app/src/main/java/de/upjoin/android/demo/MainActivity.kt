package de.upjoin.android.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import de.upjoin.android.view.modules.splashModule
import de.upjoin.android.view.splash.SplashStatus
import de.upjoin.android.view.splash.SplashStatus.LoadingState

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SplashStatus.observe(this, {
            when (it) {
                LoadingState.NONE -> splashModule.showSplashActivity(this)
                LoadingState.FINISHED -> {} // do nothing
            }
        })
    }

}