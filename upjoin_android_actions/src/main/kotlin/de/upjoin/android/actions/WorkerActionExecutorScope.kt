package de.upjoin.android.actions

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance


object WorkerActionExecutorScope {

    fun <T: Action> executeAction(action: T,
      clazz: KClass<WorkerActionExecutionPlan.WorkerActionDescription<T>>
    ) {
        val workerActionDescription = clazz.createInstance()
        val className = clazz.qualifiedName ?: "WorkerActionDescription cannot be an abstract class"
        val inputDataBuilder = Data.Builder().putString(WORKER_ACTION_DESCRIPTION_CLASS, className)
        workerActionDescription.provideInputData(action, inputDataBuilder)
        val inputData = inputDataBuilder.build()

        val oneTimeWorkRequest = OneTimeWorkRequestBuilder<ActionWorker>()
            .setConstraints(workerActionDescription.getConstraints())
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .build()
            //.setInitialDelay(2, TimeUnit.SECONDS).build()
        WorkManager.getInstance(action.context).enqueue(oneTimeWorkRequest)
    }

    class ActionWorker(context: Context, workerParameters: WorkerParameters): CoroutineWorker(
        context,
        workerParameters
    ) {

        override suspend fun doWork(): Result {
            val clazz = inputData.getString(WORKER_ACTION_DESCRIPTION_CLASS) ?: return Result.failure()
            val workerActionDescription = Class.forName(clazz).newInstance() as WorkerActionExecutionPlan.WorkerActionDescription<*>

            val action = workerActionDescription.createAction(inputData)
            try {
                action.run()
            }
            catch (e: Exception) {
                return Result.failure()
            }

            return Result.success()
        }

    }

    const val WORKER_ACTION_DESCRIPTION_CLASS = "WorkerActionDescription"
}