package de.upjoin.android.actions

import android.content.Context
import android.os.Handler
import de.upjoin.android.core.logging.Logger
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

object ActionExecutor {

    /**
     * Action is executed on the background Scope.
     * Note that it can still be attached to and canceled with a view context
     */
    object BackgroundScope : CoroutineScope {

        /**
         * Returns [EmptyCoroutineContext].
         */
        override val coroutineContext: CoroutineContext
            get() = EmptyCoroutineContext
    }

    object UnqueuedScope : CoroutineScope {

        /**
         * Returns [EmptyCoroutineContext].
         */
        override val coroutineContext: CoroutineContext
            get() = EmptyCoroutineContext
    }

    object ContextScope : CoroutineScope {

        /**
         * Returns [EmptyCoroutineContext].
         */
        override val coroutineContext: CoroutineContext
            get() = EmptyCoroutineContext
    }

    private const val BACKGROUND_ACTION_TIMEOUT = 90000L
    private const val UNQUEUED_TIMEOUT = 60000L
    private const val VIEW_ACTION_TIMEOUT = 30000L

    private var viewActionWaitingQueue = ActionQueue("ViewQueue", VIEW_ACTION_TIMEOUT)
    private var backgroundActionWaitingQueue = ActionQueue("BackgroundQueue", BACKGROUND_ACTION_TIMEOUT)

    fun executeActionDelayed(action: Action, delayMillis: Long) {
        Handler().postDelayed({ executeAction(action) }, delayMillis)
    }

    //private val unquedRunningActions = mutableListOf<Action>()

    fun executeAction(action: Action) {
        when (action.scope) {
            is BackgroundScope -> backgroundActionWaitingQueue.queue(action)
            is UnqueuedScope -> {
                try {
                    // TODO: support LastWins, FirstWins with UnqueuedActions
                    /*synchronized(unquedRunningActions) {
                        unquedRunningActions.forEach {
                            Logger.debug(this, "Unqued CheckQueue: Open Queued Action: ${it::class.java}")
                        }
                        unquedRunningActions.add(action)
                        Logger.debug(this, "Unqued CheckQueue: Running Queued Action: ${action::class.java}")
                    }*/
                    val job = action.scope.launch {
                        withContext(Dispatchers.Default) {
                            Logger.debug(this@ActionExecutor, "Starting Unqueued Action ${action::class.simpleName}")
                            withTimeout(UNQUEUED_TIMEOUT) {
                                action.run()
                            }
                        }
                    }
                    job.invokeOnCompletion {
                        /*synchronized(unquedRunningActions) {
                            unquedRunningActions.remove(action)
                        }*/
                        val runTime = Date().time - (action.startTime ?: 0)
                        Logger.debug(this@ActionExecutor, "Ending Unqueued Action ${action::class.simpleName}")
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
            else -> viewActionWaitingQueue.queue(action)
        }
    }

    // TODO: Later, create and cancel local scope, bc that is the android prefered way. launch {}
    //  will not start actions on canceled scope. Open Question: Queues working anyway?
    fun cancelActionsForContext(context: Context) {
        viewActionWaitingQueue.cancelActionsForContext(context)
        backgroundActionWaitingQueue.cancelActionsForContext(context)
        // TODO: unqueued tasks are not canceled
        // TODO: how does cancel affect tasks?
    }
}