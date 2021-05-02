package de.upjoin.android.actions

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance


object WorkerActionExecutorScope {

    fun <T: Action> executeAction(action: T,
      clazz: KClass<out WorkerActionExecutionPlan.WorkerActionDescription<T>>
    ) {
        val workerActionDescription = clazz.createInstance()
        val workRequest = workerActionDescription.createWorkRequest(action)
        WorkManager.getInstance(action.context).enqueue(workRequest)
    }

    class ActionWorker(context: Context, workerParameters: WorkerParameters): CoroutineWorker(
        context,
        workerParameters
    ) {

        override suspend fun doWork(): Result {
            val clazz = inputData.getString(WORKER_ACTION_DESCRIPTION_CLASS) ?: return Result.failure()
            val workerActionDescription = Class.forName(clazz).newInstance() as WorkerActionExecutionPlan.WorkerActionDescription<*>

            val action = workerActionDescription.createAction(applicationContext,this)
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
