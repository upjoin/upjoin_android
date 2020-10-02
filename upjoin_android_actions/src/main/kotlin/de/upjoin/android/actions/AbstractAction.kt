package de.upjoin.android.actions

import android.content.Context
import de.upjoin.android.actions.tasks.*
import de.upjoin.android.actions.ActionChangeEventRegistry.ActionEvent
import de.upjoin.android.core.logging.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.util.*

abstract class AbstractAction(protected val context: Context) : Action {

    override var startTime: Long? = null
    private var isCancelledFlag = false
    protected var job: Job? = null

    val collectedChangeEvents = mutableSetOf<ObjectChangeEvent>()

    protected abstract suspend fun runSave()

    protected open suspend fun canRun(): Boolean = true

    override suspend fun run() {
        withContext(Dispatchers.Default) {
            job = coroutineContext[Job]
            startTime = Date().time

            if (isCancelled() || !canRun()) {
                job?.cancel()
            }
            yield()
            try {
                runSave()
            }
            catch (e: Exception) {
                // also catches (Timeout)CancellationException. If not wanted, rethrow this special Exception
                Logger.error(this, e.message, e)
            }
            finally {
                handleChangeEvents()
            }
        }
    }

    override fun cancel() {
        Logger.debug(this, "Action canceled: ${this::class.java}")
        isCancelledFlag = true
        job?.cancel()
    }

    suspend fun handleChangeEvents() {
        try {
            ActionChangeEventRegistry.sendEvent(ActionEvent(this, collectedChangeEvents))
        }
        catch (e: Exception) {
            Logger.error(this, "Exception handling action change event: ${e.message}", e)
        }

    }

    protected suspend fun async(vararg tasks: AbstractTask<*>, onAnyError: (suspend (tasks: Collection<Task<*>>) -> Unit)? = null, onAllSucceed: (suspend (tasks: Collection<Task<*>>) -> Unit)? = null) {
        val taskList = tasks.toList()
        runTask(
            AsyncListTask(taskList),
            onError = { onAnyError?.invoke(taskList) },
            onSuccess = { onAllSucceed?.invoke(taskList) }
        )
    }

    protected suspend fun <U,V> async(taskA: AbstractTask<U>, taskB: AbstractTask<V>, onAnyError: (suspend (taskA: Task<U>, taskB: Task<V>) -> Unit)? = null, onAllSucceed: (suspend (rA: U, rB: V) -> Unit)? = null) {
        runTask(
            AsyncTask(taskA, taskB),
            onError = { onAnyError?.invoke(taskA, taskB) },
            onSuccess = { tR -> onAllSucceed?.invoke(tR.resultA, tR.resultB) }
        )
    }

    suspend fun <V> runTask(task: AbstractTask<V>, onError: OnErrorCallback<V>? = null, onSuccess: OnSuccessCallback<V>? = null): V? {
        task.onSuccess {
            this@AbstractAction.collectedChangeEvents.addAll(task.collectedChangeEvents)
            onSuccess?.invoke(it)
        }.onError {
            this@AbstractAction.collectedChangeEvents.addAll(task.collectedChangeEvents)
            onError?.invoke(it)
        }
        return task.run()
    }

    /**
     * Runs the given boolean task
     *
     * @param task the task to run
     * @param onError callback for when the task execution fails or the result is false
     * @param onSuccess callback for when the task execution succeeds and the result is true
     */
    suspend fun runDecision(task: AbstractTask<Boolean>, onError: OnErrorCallback<Boolean>? = null, onSuccess: OnSuccessCallback<Boolean>? = null): Boolean {
        var result = false
        task.onSuccess {
            this@AbstractAction.collectedChangeEvents.addAll(task.collectedChangeEvents)
            if (it) onSuccess?.invoke(it)
            else onError?.invoke(task)
            result = it
        }.onError {
            this@AbstractAction.collectedChangeEvents.addAll(task.collectedChangeEvents)
            onError?.invoke(it)
            result = false
        }
        task.run()
        return result
    }

    override fun isCancelled() = isCancelledFlag || job?.isCancelled==true

    override fun cancelIfForContext(c: Context) {
        if (context::class == c::class) cancel()
    }

    override fun equals(other: Any?) = if (other==null) false else (other::class == this::class)

    override fun hashCode() = this::class.hashCode()
}