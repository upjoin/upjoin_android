package de.upjoin.android.actions

object UnqueuedActionExecutionPlan: ActionExecutionPlan {

    override fun executeAction(action: Action) {
        UnqueuedActionExecutorScope.executeAction(action)
    }

}