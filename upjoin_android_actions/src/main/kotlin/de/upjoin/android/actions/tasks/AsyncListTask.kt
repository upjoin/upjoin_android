package de.upjoin.android.actions.tasks

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

open class AsyncListTask(val tasks: Collection<AbstractTask<*>>): AbstractTask<Boolean>() {

    override suspend fun runSecure(): Boolean? = withContext(Dispatchers.Default) {

        val jobs = tasks.map { async {
            it.callStack.addAll(this@AsyncListTask.callStack)
            it.callStack.add(this@AsyncListTask.javaClass.simpleName)
            it.run()
        }}

        val results = jobs.awaitAll()
        tasks.forEach { collectedChangeEvents.addAll(it.collectedChangeEvents) }

        var foundNull = false
        for (result in results) {
            if (result==null) {
                foundNull = true
                break
            }
        }

        return@withContext if (foundNull) null else true
    }

}
