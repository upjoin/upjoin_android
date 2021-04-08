package de.upjoin.android.actions

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import de.upjoin.android.actions.tasks.Task
import de.upjoin.android.core.application.appInfoModule
import de.upjoin.android.core.logging.Logger

/**
 * This module grand hooks for the tasks/actions framework.
 */
interface ActionModule {

    /**
     * Called whenever a task is executed. Hook can be used for logging
     * or testing.
     */
    fun handleTaskExecuted(task: Task<*>)

    /**
     * Called whenever an action is executed. Hook can be used for logging
     * or testing.
     */
    fun handleActionExecuted(action: Action)

    /**
     * Called whenever a task fails bc. of no result. Hook can be used for logging
     * or testing.
     */
    fun handleTaskFailure(task: Task<*>)

    /**
     * Called whenever a task fails bc. of an exception. Hook can be used for logging
     * or testing.
     */
    fun handleTaskException(task: Task<*>, e: Exception)

    /**
     * Called whenever an action fails bc. of an exception. Hook can be used for logging
     * or testing.
     */
    fun handleActionException(action: Action, e: Exception)

    /**
     * Called whenever an action execution is skipped,
     */
    fun handleActionExecutionSkipped(action: Action, reason: Action.SkipExecutionReason)

    fun debugRequest(task: Task<*>, requestAsString: String) {
        Logger.debug(this, requestAsString)
        //OutputStreamWriter(context.openFileOutput("request.txt", Context.MODE_PRIVATE)).use { it.write(requestAsString) }
    }

    fun debugResponse(task: Task<*>, responseAsString: String) {
        Logger.debug(this, responseAsString)
        //OutputStreamWriter(context.openFileOutput("response.txt", Context.MODE_PRIVATE)).use { it.write(responseAsString) }
    }

    fun getDefaultTaskType() = DefaultTaskType()

    class DefaultTaskType: TaskType {

        override fun debugTask(task: Task<*>) = appInfoModule.isDebug
        override fun createObjectMapper(): ObjectMapper {
            val factory = JsonFactory()

            // needed for Multipart HTTP Task, otherwise, when writing
            // a json part to the request body, the underlying input
            // stream would be automatically closed by the mapper.
            factory.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET)
            return ObjectMapper(factory)
                // in many cases only the part of an REST API that is needed will
                // be implemented, for that reason per default ignore unknown properties
                // and subtypes
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false)
                .registerKotlinModule()
        }
    }

    interface TaskType {

        /**
         * Checks if the given task should be debugged or not.
         * Debugging a task may be time consuming and should
         * used sparely in production.
         *
         * @return true if this task should be debugged
         */
        fun debugTask(task: Task<*>): Boolean

        /**
         * Creates a new object mapper for this task type.
         * Can be used to configure the object mapper.
         *
         * @return new object mapper
         */
        fun createObjectMapper(): ObjectMapper

    }

    companion object {
        const val MODULE_ID = "ActionModule"
    }
}
/**
 * global singleton instance
 */
lateinit var actionModule: ActionModule
