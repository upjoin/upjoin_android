package de.upjoin.android.actions

import android.content.Context

interface ActionExecutorScope {

    fun executeAction(action: Action)
    fun cancelActionsForContext(context: Context)
}