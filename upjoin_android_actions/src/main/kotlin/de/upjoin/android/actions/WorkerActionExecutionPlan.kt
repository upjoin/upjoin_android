package de.upjoin.android.actions

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import kotlin.reflect.KClass

class WorkerActionExecutionPlan<T: Action>(private val clazz: KClass<out WorkerActionDescription<T>>): ActionExecutionPlan {

    override val defaultTimeout = 600000L // ten minutes

    override fun executeAction(action: Action) {
        WorkerActionExecutorScope.executeAction(action as T, clazz)
    }

    abstract class WorkerActionDescription<T: Action> {

        abstract fun provideInputData(action: T, inputData: Data.Builder)
        abstract fun createAction(context: Context, inputData: Data): T

        fun getConstraints(): Constraints = Constraints.Builder().build()
    }
}
