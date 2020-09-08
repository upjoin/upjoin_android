package de.upjoin.android.core.framework

import de.upjoin.android.core.logging.Logger

/**
 * wraps the code block into a save block
 */
inline fun <R> saveBlock(block: () -> R): R? {
    return try {
        block.invoke()
    }
    catch (e: Exception) {
        Logger.error(Logger, e.message, e)
        null
    }
}
