package de.upjoin.android.actions

import android.content.Context
import android.os.Handler

object ActionExecutor {

    fun executeActionDelayed(action: Action, delayMillis: Long) {
        Handler().postDelayed({ executeAction(action) }, delayMillis)
    }

    //private val unquedRunningActions = mutableListOf<Action>()

    fun executeAction(action: Action) {
        action.scope.executeAction(action)
    }

    // TODO: Later, create and cancel local scope, bc that is the android preferred way. launch {}
    //  will not start actions on canceled scope. Open Question: Queues working anyway?
    fun cancelActionsForContext(context: Context) {
        ForegroundActionExecutorScope.cancelActionsForContext(context)
        UnqueuedActionExecutorScope.cancelActionsForContext(context)
        BackgroundActionExecutorScope.cancelActionsForContext(context)
    }
}