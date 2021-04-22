package de.upjoin.android.actions

interface ActionExecutionPlan {

    val defaultTimeout: Long

    fun executeAction(action: Action)
}
