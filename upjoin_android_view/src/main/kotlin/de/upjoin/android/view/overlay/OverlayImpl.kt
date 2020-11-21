package de.upjoin.android.view.overlay

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import com.google.android.material.button.MaterialButton
import de.upjoin.android.view.R
import de.upjoin.android.view.extensions.hideProgress
import de.upjoin.android.view.extensions.showProgress
import kotlinx.android.synthetic.main.overlay_frame.view.*

abstract class OverlayImpl(val context: Context,
                           private val dialogTheme: Int = R.style.UPJOIN_FullWidthAlterDialogTheme
) {

    protected lateinit var dialog: AlertDialog
    private val frameView: View

    protected val cancelButton: MaterialButton
    protected val continueButton: MaterialButton
    protected val dialogTitleText: TextView
    protected val closeXButton: ImageView

    init {
        val inflater = ContextThemeWrapper(context, dialogTheme).getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        frameView = inflater.inflate(R.layout.overlay_frame, null)

        closeXButton = frameView.close_x
        cancelButton = frameView.cancel_button
        continueButton = frameView.continue_button
        dialogTitleText = frameView.dialogTitleText
    }

    protected fun hideButtonRow() {
        frameView.button_row.visibility = INVISIBLE
    }

    protected fun init(contentLayout: Int): View {
        val inflater = ContextThemeWrapper(context, dialogTheme).getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val contentView = inflater.inflate(contentLayout, null)
        frameView.dialog_form.addView(contentView)

        val builder = AlertDialog.Builder(context, dialogTheme)
        dialog = builder
                .setView(frameView)
                .setCancelable(false)
                .show()

        frameView.close_x.setOnClickListener { dialog.dismiss() }

        return contentView
    }

    fun showProgress() {
        frameView.dialog_form.showProgress(frameView.progress)
    }

    fun hideProgress() {
        frameView.dialog_form.hideProgress(frameView.progress)
    }

    fun dismiss() {
        dialog.dismiss()
    }

}