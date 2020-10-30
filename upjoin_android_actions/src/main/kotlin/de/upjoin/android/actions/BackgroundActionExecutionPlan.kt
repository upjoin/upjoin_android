package de.upjoin.android.actions

object BackgroundActionExecutionPlan: ActionExecutionPlan {

    override fun executeAction(action: Action) {
        BackgroundActionExecutorScope.executeAction(action)
    }
}