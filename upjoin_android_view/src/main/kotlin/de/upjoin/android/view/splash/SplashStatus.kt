package de.upjoin.android.view.splash

import androidx.lifecycle.MutableLiveData

object SplashStatus: MutableLiveData<SplashStatus.LoadingState>(LoadingState.NONE) {

    enum class LoadingState {
        NONE, FINISHED
    }

}