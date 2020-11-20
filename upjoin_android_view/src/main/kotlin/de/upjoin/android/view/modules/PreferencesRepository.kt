package de.upjoin.android.view.modules

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import de.upjoin.android.core.application.ModulizedApplication
import de.upjoin.android.core.modules.ModuleLiveCycle
import de.upjoin.android.view.application.NightMode

open class PreferencesRepository: ModuleLiveCycle {

    lateinit var applicationContext: Context

    val prefs: SharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(applicationContext) }

    private val nightModeLock = Object()

    var nightMode: NightMode?
        get() {
            synchronized(nightModeLock) {
                if (!prefs.contains(NIGHTMODE)) return null
                return if (prefs.getBoolean(NIGHTMODE, false)) NightMode.ON else NightMode.OFF
            }
        }
        set(value) {
            synchronized(nightModeLock) {
                if (value == null) prefs.edit().remove(NIGHTMODE).commit()
                else prefs.edit().putBoolean(NIGHTMODE, value == NightMode.ON).commit()
            }
        }

    val appDefaultNightMode: NightMode = NightMode.ON

    override fun onCreate(application: ModulizedApplication) {
        applicationContext = application.applicationContext

        val nightMode = nightMode ?: appDefaultNightMode
        AppCompatDelegate.setDefaultNightMode(nightMode.value)

        preferencesRepository = this
    }

    companion object {
        const val MODULE_ID = "PreferencesRepositoryModule"
        const val NIGHTMODE = "nightmode"
    }

}
lateinit var preferencesRepository: PreferencesRepository