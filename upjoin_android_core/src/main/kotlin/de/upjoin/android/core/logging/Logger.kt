package de.upjoin.android.core.logging

import android.util.Log

/**
 * Simplified logging
 */
object Logger {

    /**
     * writes a message to debug log
     *
     * @param o the log object
     * @param message the log message
     */
    fun debug(o: Any, message: String) {
        Log.d(o.javaClass.name, message)
    }

    /**
     * writes a message to info log
     *
     * @param o the log object
     * @param message the log message
     */
    fun info(o: Any, message: String) {
        Log.i(o.javaClass.name, message)
    }

    /**
     * writes a message to error log
     *
     * @param o the log object
     * @param message the log message
     */
    fun error(o: Any, message: String) {
        Log.e(o.javaClass.name, message)
    }

    /**
     * writes a message to error log
     *
     * @param o the log object
     * @param message the log message
     * @param t the throwable to log
     */
    fun error(o: Any, message: String?, t: Throwable) {
        Log.e(o.javaClass.name, message, t)
    }

}