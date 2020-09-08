package de.upjoin.android.actions.tasks.web

import de.upjoin.android.actions.tasks.Task

/**
 * Interface for an API task
 *
 * @param R return type of the task execution
 */
interface HTTPTask<R>: Task<R> {

    /**
     * the http response code
     */
    var httpCode: Int?
}