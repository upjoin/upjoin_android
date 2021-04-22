package de.upjoin.android.actions

object UnqueuedActionExecutionPlan: ActionExecutionPlan {

    override val defaultTimeout = 60000L

    override fun executeAction(action: Action) {
        UnqueuedActionExecutorScope.executeAction(action)
    }

}
