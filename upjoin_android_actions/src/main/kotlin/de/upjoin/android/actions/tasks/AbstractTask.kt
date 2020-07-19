package de.upjoin.android.actions.tasks

import de.upjoin.android.core.logging.Logger
import de.upjoin.android.actions.tasks.web.APITask

// TODO: Temporary extends APITask
abstract class AbstractTask<R>: APITask<R> {

    override val collectedChangeEvents = mutableSetOf<ObjectChangeEvent>()

    private val onSuccessActions = mutableListOf<OnSuccessCallback<R>>()
    private val onErrorActions = mutableListOf<OnErrorCallback<R>>()

    var exception: Exception? = null
        protected set

    override var httpCode: Int? = null

    abstract suspend fun runSecure(): R?

    internal suspend fun run(): R? {
        try {
            val result = runSecure()
            if (result!=null) {
                onSuccessActions.forEach { it.invoke(result) }
                return result
            }
        }
        catch (e: Exception) {
            handleException(e)
        }
        onErrorActions.forEach { it.invoke(this) }
        return null
    }

    protected open fun handleException(e: Exception) {
        Logger.error(this, e.message ?: "", e)
        exception = e
    }

    //@Beta
    override suspend fun onSuccess(block: OnSuccessCallback<R>): AbstractTask<R> {
        onSuccessActions.add(block)
        return this
    }

    //@Beta
    override suspend fun onError(block: OnErrorCallback<R>): AbstractTask<R> {
        onErrorActions.add(block)
        return this
    }

    protected suspend fun <V> runTask(task: AbstractTask<V>, onError: OnErrorCallback<V>? = null, onSuccess: OnSuccessCallback<V>? = null): V? {
        return task.onSuccess {
            this@AbstractTask.collectedChangeEvents.addAll(task.collectedChangeEvents)
            onSuccess?.invoke(it)
        }.onError {
            this@AbstractTask.collectedChangeEvents.addAll(task.collectedChangeEvents)
            onError?.invoke(it)
        }.run()
    }

    protected suspend fun async(vararg tasks: AbstractTask<*>, onAnyError: (suspend (tasks: Collection<Task<*>>) -> Unit)? = null, onAllSucceed: (suspend (tasks: Collection<Task<*>>) -> Unit)? = null) {
        val taskList = tasks.toList()
        runTask(
            AsyncListTask(taskList),
            onError = { onAnyError?.invoke(taskList) },
            onSuccess = { onAllSucceed?.invoke(taskList) }
        )
    }
}