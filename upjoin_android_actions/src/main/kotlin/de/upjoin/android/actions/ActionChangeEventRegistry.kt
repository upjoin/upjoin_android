package de.upjoin.android.actions

import de.upjoin.android.actions.tasks.ObjectChangeEvent
import kotlinx.coroutines.*

object ActionChangeEventRegistry {

    private val listeners = mutableListOf<ActionEventListener>()

    @Synchronized
    fun registerListener(listener: ActionEventListener) {
        listeners.add(listener)
    }

    @Synchronized
    fun unregisterListener(listener: ActionEventListener) {
        listeners.remove(listener)
    }

    @Synchronized
    private fun getSyncListeners() = ArrayList<ActionEventListener>(listeners)

    suspend fun sendEvent(event: ActionEvent) = withContext(Dispatchers.Default) {
        val jobs = mutableListOf<Job>()
        getSyncListeners().forEach {
            jobs += launch {
                it.receiveEvent(event)
            }
        }
        jobs.joinAll()
    }

    class ActionEvent(val action: Action, val collectedEvents: Set<ObjectChangeEvent>)

    interface ActionEventListener {

        suspend fun receiveEvent(event: ActionEvent)

    }

    interface ActionEventFilter {

        fun matches(event: ActionEvent): Boolean

    }

    fun filteredEventListener(vararg filters: ActionEventFilter, block: suspend (event: ActionEvent) -> Unit): ActionEventListener =

        object: ActionEventListener {

            override suspend fun receiveEvent(event: ActionEvent) {
                if (filters.find { it.matches(event) } != null) {
                    block.invoke(event)
                }
            }
        }

    open class CombinedFilter(private val filters: Collection<ActionEventFilter>): ActionEventFilter {

        override fun matches(event: ActionEvent) = filters.find { it.matches(event) } != null

    }
}