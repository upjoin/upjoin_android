package de.upjoin.android.view.overlay

import android.content.Context
import de.upjoin.android.view.R
import kotlinx.android.synthetic.main.overlay_webview.view.*


open class WebViewOverlayImpl(context: Context, dialogTheme: Int = R.style.UPJOIN_FullWidthAlterDialogTheme, url: String): OverlayImpl(context, dialogTheme) {

    init {
        val view = init(R.layout.overlay_webview)
        view.web_view.loadUrl(url)
    }

}
