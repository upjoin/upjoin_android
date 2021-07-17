package de.upjoin.android.actions.tasks

import java.util.*

/**
 * Interface for a task
 *
 * @param R return type of the task execution
 */
interface Task<R> {

    val callStack: MutableList<String>

    /**
     * A set of change events collected while running this task
     */
    val collectedChangeEvents: Set<ObjectChangeEvent>

    /**
     * Registers a success callback for this task. All success callbacks will be called
     * if the task succeeds
     */
    suspend fun onSuccess(block: OnSuccessCallback<R>): Task<R>

    /**
     * Registers an error callback for this task. All error callbacks will be called
     * if the task fails
     */
    suspend fun onError(block: OnErrorCallback<R>): Task<R>

}

typealias OnErrorCallback<R> = suspend (task: Task<R>) -> Unit
typealias OnSuccessCallback<R> = suspend (result: R) -> Unit
