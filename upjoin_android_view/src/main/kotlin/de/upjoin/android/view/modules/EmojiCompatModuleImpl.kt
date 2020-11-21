package de.upjoin.android.view.modules

import androidx.core.provider.FontRequest
import androidx.emoji.text.EmojiCompat
import androidx.emoji.text.FontRequestEmojiCompatConfig
import de.upjoin.android.core.application.ModulizedApplication
import de.upjoin.android.core.modules.ModuleLiveCycle
import de.upjoin.android.view.R

class EmojiCompatModuleImpl: ModuleLiveCycle {

    override fun onCreate(application: ModulizedApplication) {

        /*EmojiCompat.Config config = new BundledEmojiCompatConfig(this);
          EmojiCompat.init(config);*/

        val fontRequest = FontRequest(
            "com.google.android.gms.fonts",
            "com.google.android.gms",
            "Noto Color Emoji Compat",
            R.array.com_google_android_gms_fonts_certs
        )

        val config = FontRequestEmojiCompatConfig(application.applicationContext, fontRequest)
            .registerInitCallback(object : EmojiCompat.InitCallback() {

                override fun onInitialized() {
                    //Log.i("MyApplication", "EmojiCompat initialized");
                }

                override fun onFailed(throwable: Throwable?) {
                    //Log.e("MyApplication", "EmojiCompat initialization failed", throwable);
                }
            })
        EmojiCompat.init(config)

        emojiCompatModule = this
    }

    companion object {
        const val MODULE_ID = "EmojiCompatModule"
    }
}
lateinit var emojiCompatModule: EmojiCompatModuleImpl