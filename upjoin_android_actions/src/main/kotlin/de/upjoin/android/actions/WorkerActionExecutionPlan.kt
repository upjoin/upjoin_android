package de.upjoin.android.actions

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

class WorkerActionExecutionPlan<T: Action>(private val clazz: KClass<out WorkerActionDescription<T>>): ActionExecutionPlan {

    override val defaultTimeout = 600000L // ten minutes

    override fun executeAction(action: Action) {
        WorkerActionExecutorScope.executeAction(action as T, clazz)
    }

    abstract class WorkerActionDescription<T: Action> {

        open val tags: List<String> = emptyList()

        open fun createWorkRequest(action: T): WorkRequest {

            val className = this::class.java.name ?: "WorkerActionDescription cannot be an anonymous class"
            val inputDataBuilder = Data.Builder().putString(WorkerActionExecutorScope.WORKER_ACTION_DESCRIPTION_CLASS, className)
            provideInputData(action, inputDataBuilder)
            val inputData = inputDataBuilder.build()

            val builder = OneTimeWorkRequestBuilder<WorkerActionExecutorScope.ActionWorker>()
                .setConstraints(getConstraints())
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS)
                .setInputData(inputData)

            tags.forEach { tag ->
                builder.addTag(tag)
            }

            return builder.build()
        }

        abstract fun provideInputData(action: T, inputData: Data.Builder)
        abstract fun createAction(context: Context, coroutineWorker: CoroutineWorker): T

        fun getConstraints(): Constraints = Constraints.Builder().build()
    }
}
