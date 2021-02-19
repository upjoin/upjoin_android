package de.upjoin.android.actions

import de.upjoin.android.actions.tasks.Task

/**
 * This module grand hooks for the tasks/actions framework.
 */
interface ActionModule {

    /**
     * Called whenever a task is executed. Hook can be used for logging
     * or testing.
     */
    fun handleTaskExecuted(task: Task<*>)

    /**
     * Called whenever an action is executed. Hook can be used for logging
     * or testing.
     */
    fun handleActionExecuted(action: Action)

    /**
     * Called whenever a task fails bc. of no result. Hook can be used for logging
     * or testing.
     */
    fun handleTaskFailure(task: Task<*>)

    /**
     * Called whenever a task fails bc. of an exception. Hook can be used for logging
     * or testing.
     */
    fun handleTaskException(task: Task<*>, e: Exception)

    /**
     * Called whenever an action fails bc. of an exception. Hook can be used for logging
     * or testing.
     */
    fun handleActionException(action: Action, e: Exception)

    /**
     * Called whenever an action execution is skipped,
     */
    fun handleActionExecutionSkipped(action: Action, reason: Action.SkipExecutionReason)

    companion object {
        const val MODULE_ID = "ActionModule"
    }
}
/**
 * global singleton instance
 */
lateinit var actionModule: ActionModule