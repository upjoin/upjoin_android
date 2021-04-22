package de.upjoin.android.actions

import android.content.Context
import de.upjoin.android.core.logging.Logger
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

object UnqueuedActionExecutorScope: ActionExecutorScope {

    private val coroutineScope = object: CoroutineScope {
        /**
         * Returns [EmptyCoroutineContext].
         */
        override val coroutineContext: CoroutineContext
            get() = EmptyCoroutineContext
    }

    override fun executeAction(action: Action) {
        try {
            // TODO: support LastWins, FirstWins with UnqueuedActions
            /*synchronized(unquedRunningActions) {
                unquedRunningActions.forEach {
                    Logger.debug(this, "Unqued CheckQueue: Open Queued Action: ${it::class.java}")
                }
                unquedRunningActions.add(action)
                Logger.debug(this, "Unqued CheckQueue: Running Queued Action: ${action::class.java}")
            }*/
            val job = coroutineScope.launch {
                withContext(Dispatchers.Default) {
                    Logger.debug(this, "Starting Unqueued Action ${action::class.simpleName}")
                    action.run()
                }
            }
            job.invokeOnCompletion {
                /*synchronized(unquedRunningActions) {
                    unquedRunningActions.remove(action)
                }*/
                val runTime = Date().time - (action.startTime ?: 0)
                Logger.debug(this, "Ending Unqueued Action ${action::class.simpleName}")
                if (it != null) {
                    Logger.error(this, "Unqueued Action '${action::class.simpleName}' finished with Exception: ${it.message}, runtime ${runTime}ms", it)
                }
            }
        }
        catch (e: Exception) {
            // This Try-Catch is crucial, or was at least in ActionQueue, without it, the app randomly restarted during Registration
            Logger.error(this, e.message, e)
        }
    }

    override fun cancelActionsForContext(context: Context) {
        // do nothing yet, TODO: Should unqueued running actions be canceled if for context?
    }
}
