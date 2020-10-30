package de.upjoin.android.actions

import android.content.Context
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

object ForegroundActionExecutorScope: ActionExecutorScope {

    private const val timeout = 30000L

    private val coroutineScope = object: CoroutineScope {
        /**
         * Returns [EmptyCoroutineContext].
         */
        override val coroutineContext: CoroutineContext
            get() = EmptyCoroutineContext
    }

    private var viewActionWaitingQueue = ActionQueue("ViewQueue", coroutineScope, timeout)

    override fun executeAction(action: Action) {
        viewActionWaitingQueue.queue(action)
    }

    override fun cancelActionsForContext(context: Context) {
        viewActionWaitingQueue.cancelActionsForContext(context)
    }
}