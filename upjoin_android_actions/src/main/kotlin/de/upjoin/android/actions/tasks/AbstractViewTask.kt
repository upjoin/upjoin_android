package de.upjoin.android.actions.tasks

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class AbstractViewTask<T>: AbstractTask<T>() {

    override suspend fun runSecure() = withContext(Dispatchers.Main) {
        return@withContext runInView()
    }

    abstract fun runInView(): T?
}