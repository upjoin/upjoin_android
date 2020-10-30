package de.upjoin.android.actions

import android.content.Context
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

object BackgroundActionExecutorScope: ActionExecutorScope {

    private const val timeout = 90000L

    private val coroutineScope = object: CoroutineScope {
        /**
         * Returns [EmptyCoroutineContext].
         */
        override val coroutineContext: CoroutineContext
            get() = EmptyCoroutineContext
    }

    private var backgroundActionWaitingQueue = ActionQueue("BackgroundQueue", coroutineScope, timeout)

    override fun executeAction(action: Action) {
        backgroundActionWaitingQueue.queue(action)
    }

    override fun cancelActionsForContext(context: Context) {
        backgroundActionWaitingQueue.cancelActionsForContext(context)
    }
}