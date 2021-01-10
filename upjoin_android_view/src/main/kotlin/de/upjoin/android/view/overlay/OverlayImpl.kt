package de.upjoin.android.view.overlay

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import androidx.appcompat.view.ContextThemeWrapper
import de.upjoin.android.view.R
import de.upjoin.android.view.databinding.OverlayFrameBinding
import de.upjoin.android.view.extensions.hideProgress
import de.upjoin.android.view.extensions.showProgress

abstract class OverlayImpl(val context: Context,
                           private val dialogTheme: Int = R.style.UPJOIN_FullWidthAlterDialogTheme
) {

    protected lateinit var dialog: AlertDialog
    protected val frameBinding: OverlayFrameBinding

    init {
        val inflater = ContextThemeWrapper(context, dialogTheme).getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val frameView = inflater.inflate(R.layout.overlay_frame, null)
        frameBinding = OverlayFrameBinding.bind(frameView)
    }

    protected fun hideButtonRow() {
        frameBinding.buttonRow.visibility = INVISIBLE
    }

    protected fun init(contentLayout: Int): View {
        val inflater = ContextThemeWrapper(context, dialogTheme).getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val contentView = inflater.inflate(contentLayout, null)
        frameBinding.dialogForm.addView(contentView)

        val builder = AlertDialog.Builder(context, dialogTheme)
        dialog = builder
                .setView(frameBinding.root)
                .setCancelable(false)
                .show()

        frameBinding.closeX.setOnClickListener { dialog.dismiss() }

        return contentView
    }

    fun showProgress() {
        frameBinding.dialogForm.showProgress(frameBinding.progress)
    }

    fun hideProgress() {
        frameBinding.dialogForm.hideProgress(frameBinding.progress)
    }

    fun dismiss() {
        dialog.dismiss()
    }

}