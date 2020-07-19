package de.upjoin.android.core.logging

import android.util.Log

object Logger {

    fun debug(o: Any, message: String) {
        Log.d(o.javaClass.name, message)
    }

    fun info(o: Any, message: String) {
        Log.i(o.javaClass.name, message)
    }

    fun error(o: Any, message: String) {
        Log.e(o.javaClass.name, message)
    }

    fun error(o: Any, message: String?, t: Throwable) {
        Log.e(o.javaClass.name, message, t)
    }

}