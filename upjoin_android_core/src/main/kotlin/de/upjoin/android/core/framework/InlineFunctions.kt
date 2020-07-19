package de.upjoin.android.core.framework

import de.upjoin.android.core.logging.Logger

inline fun <R> saveBlock(block: () -> R): R? {
    return try {
        block.invoke()
    }
    catch (e: Exception) {
        Logger.error(Logger, e.message, e)
        null
    }
}
