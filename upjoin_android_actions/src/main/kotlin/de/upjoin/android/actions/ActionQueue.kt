package de.upjoin.android.actions

import android.content.Context
import de.upjoin.android.actions.Action.QueuingMode
import de.upjoin.android.core.logging.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.util.*

internal class ActionQueue(private val name: String, private val timeout: Long): LinkedList<de.upjoin.android.actions.Action>() {

    private var runningAction: de.upjoin.android.actions.Action? = null

    @Synchronized
    fun queue(action: de.upjoin.android.actions.Action) {
        if (action.queuingMode== QueuingMode.FirstWins && (runningAction == action || find { it == action } != null)) return

        if (action.queuingMode== QueuingMode.LastWins) {
            // API Level 24: removeIf({ it::class == action::class })
            forEach { if (it == action) it.cancel() }
            removeAll(filter { it.isCancelled() })
        }
        add(action)
        checkQueue()
    }

    @Synchronized
    private fun checkQueue() {

        /*if (runningAction!=null) {
            //Logger.debug(this, "$name CheckQueue: Queued Action currently running: ${runningAction!!::class.java}, isCancelled=$${runningAction!!.isCancelled()}")

            if (runningAction?.isCancelled() == true) {
                // TODO: Not quite clear why this happens in the autotests, actionCompleted() should be called when cancelled
                runningAction = null
            }

        }*/
        /*forEach {
            Logger.debug(this, "$name CheckQueue: Open Queued Action: ${it::class.java}")
        }*/

        if (runningAction != null) return

        var nextAction = poll() ?: return
        while (nextAction.isCancelled()) {
            nextAction = poll() ?: return
        }
        runningAction = nextAction
        try {
            /*val handler = CoroutineExceptionHandler { _, exception ->
                Logger.error(this, "$name CoroutineExceptionHandler got $exception", exception)
            }*/
            val job = nextAction.scope.launch {
                asyncAction(nextAction)
            }
            job.invokeOnCompletion {
                val runTime = Date().time - (nextAction.startTime ?: 0)
                Logger.debug(this@ActionQueue, "$name Ending Queued Action ${nextAction::class.simpleName}")

                if (it!=null) {
                    Logger.error(this, "$name Action '${nextAction::class.simpleName}' finished with Exception: ${it.message}, runtime ${runTime}ms", it)
                }

                actionCompleted()
            }
        }
        catch (e: Exception) {
            // This Try-Catch is crucial, without it, the app randomly restarted during Registration
            Logger.error(this, e.message, e)
        }
    }

    @Synchronized
    private fun actionCompleted() {
        runningAction = null
        checkQueue()
    }

    private suspend fun asyncAction(action: de.upjoin.android.actions.Action) = withContext(Dispatchers.Default) {
        Logger.debug(this@ActionQueue, "$name Starting Queued Action ${action::class.simpleName}")
        withTimeout(timeout) {
            action.run()
        }
    }

    @Synchronized
    fun cancelActionsForContext(context: Context) {
        for (a in this) a.cancelIfForContext(context)
        removeAll(filter { it.isCancelled() })

        val rA = runningAction ?: return
        rA.cancelIfForContext(context)
    }
}