package de.upjoin.android.actions

object BackgroundActionExecutionPlan: ActionExecutionPlan {

    override val defaultTimeout = 90000L

    override fun executeAction(action: Action) {
        BackgroundActionExecutorScope.executeAction(action)
    }
}
