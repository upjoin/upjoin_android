package de.upjoin.android.actions.tasks

import de.upjoin.android.actions.tasks.AsyncTask.TaskResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

open class AsyncTask<U, V>(val taskA: AbstractTask<U>, val taskB: AbstractTask<V>): AbstractTask<TaskResult<U, V>>() {

    override suspend fun runSecure(): TaskResult<U, V>? = withContext(Dispatchers.Default) {
        val jobA = async {
            taskA.callStack.addAll(this@AsyncTask.callStack)
            taskA.callStack.add(this@AsyncTask.javaClass.simpleName)
            taskA.run()
        }
        val jobB = async {
            taskB.callStack.addAll(this@AsyncTask.callStack)
            taskB.callStack.add(this@AsyncTask.javaClass.simpleName)
            taskB.run()
        }

        val resultA = jobA.await()
        val resultB = jobB.await()
        collectedChangeEvents.addAll(taskA.collectedChangeEvents)
        collectedChangeEvents.addAll(taskB.collectedChangeEvents)
        return@withContext if (resultA==null || resultB == null) null else TaskResult(resultA, resultB)
    }

    class TaskResult<U, V>(val resultA: U, val resultB: V)

}
