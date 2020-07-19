package de.upjoin.android.actions.tasks

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class AbstractViewTask: AbstractTask<Unit>() {

    override suspend fun runSecure() = withContext(Dispatchers.Main) {
        runInView()
    }

    abstract fun runInView()
}