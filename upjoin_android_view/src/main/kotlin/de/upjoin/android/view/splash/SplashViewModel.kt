package de.upjoin.android.view.splash

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import de.upjoin.android.actions.ActionChangeEventRegistry.filteredEventListener
import de.upjoin.android.actions.ActionChangeEventRegistry.registerListener
import de.upjoin.android.actions.ActionChangeEventRegistry.unregisterListener
import de.upjoin.android.actions.ActionExecutor.executeAction
import de.upjoin.android.actions.events.IsActionChangeEventFilter
import de.upjoin.android.view.actions.splash.AbstractLoadApplicationAction
import de.upjoin.android.view.modules.splashModule
import de.upjoin.android.view.splash.SplashStatus.LoadingState.FINISHED

class SplashViewModel: ViewModel() {

    private var initialized = false
    private var loadFinished = false
    private var timeFinished = false

    private val actionEventListener = filteredEventListener(
        IsActionChangeEventFilter(
            AbstractLoadApplicationAction::class
        )
    ) {
        loadFinished = true
        if (timeFinished) SplashStatus.postValue(FINISHED)
    }

    fun load(context: Context) {
        if (!initialized) {
            registerListener(actionEventListener)
            initialized = true
            Handler(
                Looper.myLooper() ?: throw RuntimeException(
                    "Can't create handler inside thread ${Thread.currentThread()} that has not called Looper.prepare()"
                )
            ).postDelayed(
                {
                    if (loadFinished) SplashStatus.postValue(FINISHED)
                    else timeFinished = true
                },
                splashModule.visibleTime.toLong()
            )
            executeAction(splashModule.createLoadApplicationAction(context))
        }
    }

    override fun onCleared() {
        unregisterListener(actionEventListener)
        initialized = false
        super.onCleared()
    }
}