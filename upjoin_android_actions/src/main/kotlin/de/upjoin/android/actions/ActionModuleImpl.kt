package de.upjoin.android.actions

import de.upjoin.android.actions.Action.SkipExecutionReason.*
import de.upjoin.android.actions.tasks.Task
import de.upjoin.android.actions.tasks.web.HTTPTask
import de.upjoin.android.core.application.ModulizedApplication
import de.upjoin.android.core.logging.Logger
import de.upjoin.android.core.modules.ModuleLiveCycle

class ActionModuleImpl: ActionModule, ModuleLiveCycle {

    override fun onCreate(application: ModulizedApplication) {
        actionModule = this
    }

    override fun handleTaskExecuted(task: Task<*>) {
        Logger.info(this, "Task $task executed.")
    }

    override fun handleActionExecuted(action: Action) {
        Logger.info(this, "Action $action executed.")
    }

    override fun handleTaskFailure(task: Task<*>) {
        if (task is HTTPTask) {
            Logger.error(this, "Task $task failed with httpcode ${task.httpCode}.")

        }
        else {
            Logger.error(this, "Task $task finished without result.")
        }
    }

    override fun handleTaskException(task: Task<*>, e: Exception) {
        Logger.error(this, "Task $task failed with exception", e)
    }

    override fun handleActionException(action: Action, e: Exception) {
        Logger.error(this, "Action $action failed with exception", e)
    }

    override fun handleActionExecutionSkipped(action: Action, reason: Action.SkipExecutionReason) {
        when (reason) {
            JobCancelled -> Logger.info(this, "Skipped Action $action bc job was canceled implicitly.")
            CancelledManually -> Logger.info(this, "Skipped Action $action bc job was canceled manually.")
            CannotRun -> Logger.info(this, "Skipped Action $action bc it was not allowed to run.")
        }
    }

}
