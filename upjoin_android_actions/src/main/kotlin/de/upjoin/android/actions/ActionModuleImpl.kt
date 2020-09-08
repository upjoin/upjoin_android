package de.upjoin.android.actions

import de.upjoin.android.actions.tasks.Task
import de.upjoin.android.core.application.ModulizedApplication
import de.upjoin.android.core.logging.Logger
import de.upjoin.android.core.modules.ModuleLiveCycle

class ActionModuleImpl: ActionModule, ModuleLiveCycle {

    override fun onCreate(application: ModulizedApplication) {
        actionModule = this
    }

    override fun handleTaskException(task: Task<*>, e: Exception) {
        Logger.error(this, e.message, e)
    }

}