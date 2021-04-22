package de.upjoin.android.actions

object ForegroundActionExecutionPlan: ActionExecutionPlan {

    override val defaultTimeout = 30000L

    override fun executeAction(action: Action) {
        ForegroundActionExecutorScope.executeAction(action)
    }
}
