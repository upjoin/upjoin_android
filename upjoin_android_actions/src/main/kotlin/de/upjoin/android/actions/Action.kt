package de.upjoin.android.actions

import android.content.Context

interface Action {

    enum class QueuingMode {

        /**
         * Don't put this action into the queue if another equal
         * action is in the queue already.
         *
         */
        FirstWins,

        /**
         * Cancel previous same actions that are already waiting in
         * the queue, excluding the currently running action
         */
        LastWins,

        /**
         * Just add them to the queue
         */
        JustAdd
    }

    suspend fun run()

    val context: Context

    val scope: ActionExecutionPlan

    val queuingMode: QueuingMode

    fun cancel()
    fun isCancelled(): Boolean
    fun cancelIfForContext(c: Context)

    var startTime: Long?

    enum class SkipExecutionReason {
        JobCancelled, CancelledManually, CannotRun
    }
}