package de.upjoin.android.actions.tasks

interface Task<R> {

    val collectedChangeEvents: Set<ObjectChangeEvent>

    suspend fun onSuccess(block: OnSuccessCallback<R>): Task<R>

    suspend fun onError(block: OnErrorCallback<R>): Task<R>

}

typealias OnErrorCallback<R> = suspend (task: Task<R>) -> Unit
typealias OnSuccessCallback<R> = suspend (result: R) -> Unit
