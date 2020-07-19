package de.upjoin.android.actions.tasks.web

import de.upjoin.android.actions.tasks.Task

interface APITask<R>: Task<R> {

    var httpCode: Int?
}