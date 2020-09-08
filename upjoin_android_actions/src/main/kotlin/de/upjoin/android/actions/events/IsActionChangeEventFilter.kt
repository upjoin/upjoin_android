package de.upjoin.android.actions.events

import de.upjoin.android.actions.Action
import de.upjoin.android.actions.ActionChangeEventRegistry
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

/**
 * Filters events of a specific given type
 */
class IsActionChangeEventFilter(val clazz: KClass<out Action>): ActionChangeEventRegistry.ActionEventFilter {

    override fun matches(event: ActionChangeEventRegistry.ActionEvent): Boolean {
        return event.action::class.isSubclassOf(clazz)
    }

}