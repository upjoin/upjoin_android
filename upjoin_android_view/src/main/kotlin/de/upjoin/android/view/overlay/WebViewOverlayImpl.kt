package de.upjoin.android.view.overlay

import android.content.Context
import de.upjoin.android.view.R
import de.upjoin.android.view.databinding.OverlayWebviewBinding

open class WebViewOverlayImpl(context: Context, dialogTheme: Int = R.style.UPJOIN_FullWidthAlterDialogTheme, url: String): OverlayImpl(context, dialogTheme) {

    protected val webBinding: OverlayWebviewBinding

    init {
        val view = init(R.layout.overlay_webview)
        webBinding = OverlayWebviewBinding.bind(view)
        webBinding.webView.loadUrl(url)
    }

}
