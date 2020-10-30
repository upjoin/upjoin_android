package de.upjoin.android.actions

object ForegroundActionExecutionPlan: ActionExecutionPlan {

    override fun executeAction(action: Action) {
        ForegroundActionExecutorScope.executeAction(action)
    }
}