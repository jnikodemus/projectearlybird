package de.ntbit.projectearlybird.model

import com.parse.ParseClassName
import com.parse.ParseObject
import java.util.*

/**
 * Represents a single item in the [ModuleChecklist]
 */
@ParseClassName("ModuleChecklistItem")
class ModuleChecklistItem : ParseObject {

    private var name: String
        get() {
            return getString("name")!!
        }
        set(value) {
            put("name", value)
        }

    private var amount: Int
        get() {
            return getInt("amount")
        }
        set(value) {
            put("amount", value)
        }

    private var timestamp: Date
        get() {
            return getDate("timestamp")!!
        }
        set(value) {
            put("timestamp", value)
        }

    private var isAssigned: Boolean
        get() {
            return getBoolean("isAssigned")
        }
        set(value) {
            put("isAssigned", value)
        }

    private var user: User
        get() {
            return getParseUser("user") as User
        }
        set(value) {
            put("user", value)
        }

    internal constructor() : super()

    internal constructor(name: String, isAssigned: Boolean, user: User) : super() {

    }

    /**
     * Checks this equals [other] by using the objectId.
     *
     * @return true if [other] is [ModuleChecklistItem] and objectIds are the same, false else.
     */
    override fun equals(other: Any?): Boolean {
        if(other is ModuleChecklistItem) return this.objectId == other.objectId
        return false
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}