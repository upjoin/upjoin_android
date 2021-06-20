package de.upjoin.android.view.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.widget.ProgressBar

/**
 * Shows the progress UI and hides the form.
 */
private fun showProgress(form: View, progress: ProgressBar, show: Boolean, hiddenVisibility: Int = View.GONE) {
    val shortAnimTime = form.resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

    form.apply {
        visibility = if (show) hiddenVisibility else View.VISIBLE
        alpha = if (show) 1f else 0f
        animate()
            .setDuration(shortAnimTime)
            .alpha(if (show) 0f else 1f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    visibility = if (show) hiddenVisibility else View.VISIBLE
                }
            })
    }

    progress.apply {
        visibility = if (show) View.VISIBLE else View.GONE
        alpha = if (show) 0f else 1f
        animate()
            .setDuration(shortAnimTime)
            .alpha(if (show) 1f else 0f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    visibility = if (show) View.VISIBLE else View.GONE
                }
            })
    }
}

fun View.showProgress(progress: ProgressBar, hiddenVisibility: Int = View.GONE) {
    showProgress(this, progress, true, hiddenVisibility)
}

fun View.hideProgress(progress: ProgressBar, hiddenVisibility: Int = View.GONE) {
    showProgress(this, progress, false, hiddenVisibility)
}
