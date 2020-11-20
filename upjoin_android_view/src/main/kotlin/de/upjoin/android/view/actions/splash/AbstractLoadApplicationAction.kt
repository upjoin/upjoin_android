package de.upjoin.android.view.actions.splash

import android.content.Context
import de.upjoin.android.actions.AbstractAction
import de.upjoin.android.actions.Action.QueuingMode
import de.upjoin.android.actions.ForegroundActionExecutionPlan

/**
 * LoadApplicationAction is ViewAction, bc. loading the application is not expected to
 * make server calls
 */
abstract class AbstractLoadApplicationAction(c: Context) : AbstractAction(c) {

    override val scope = ForegroundActionExecutionPlan
    override val queuingMode = QueuingMode.LastWins

}