package de.upjoin.android.actions.tasks

import de.upjoin.android.actions.actionModule

/**
 * Abstract task superclass. Most tasks will inherit from this superclass because only AbstractTasks
 * subclasses can be used with AbstractAction. The main reason for this is the wish to hide the
 * task's run method, so it cannot be called in the wrong context.
 */
abstract class AbstractTask<R>: Task<R> {

    override val collectedChangeEvents = mutableSetOf<ObjectChangeEvent>()

    private val onSuccessActions = mutableListOf<OnSuccessCallback<R>>()
    private val onErrorActions = mutableListOf<OnErrorCallback<R>>()

    var exception: Exception? = null
        protected set

    /**
     * Runs the tasks code block. The task is considered as failed if it returns null or
     * throws any exception.
     */
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
        exception = e
        actionModule.handleTaskException(this, e)
    }

    override suspend fun onSuccess(block: OnSuccessCallback<R>): AbstractTask<R> {
        onSuccessActions.add(block)
        return this
    }

    override suspend fun onError(block: OnErrorCallback<R>): AbstractTask<R> {
        onErrorActions.add(block)
        return this
    }

    /**
     * Runs the given task
     *
     * @param task the task to run
     * @param onError callback for when the task execution fails
     * @param onSuccess callback for when the task execution succeeds
     */
    protected suspend fun <V> runTask(task: AbstractTask<V>, onError: OnErrorCallback<V>? = null, onSuccess: OnSuccessCallback<V>? = null): V? {
        return task.onSuccess {
            this@AbstractTask.collectedChangeEvents.addAll(task.collectedChangeEvents)
            onSuccess?.invoke(it)
        }.onError {
            this@AbstractTask.collectedChangeEvents.addAll(task.collectedChangeEvents)
            onError?.invoke(it)
        }.run()
    }

    /**
     * Runs the given tasks asynchronously
     *
     * @param tasks the task to run
     * @param onAnyError callback for when at least one task execution fails
     * @param onAllSuccess callback for when all tasks executed successfully
     */
    protected suspend fun async(vararg tasks: AbstractTask<*>, onAnyError: (suspend (tasks: Collection<Task<*>>) -> Unit)? = null, onAllSucceed: (suspend (tasks: Collection<Task<*>>) -> Unit)? = null) {
        val taskList = tasks.toList()
        runTask(
            AsyncListTask(taskList),
            onError = { onAnyError?.invoke(taskList) },
            onSuccess = { onAllSucceed?.invoke(taskList) }
        )
    }
}