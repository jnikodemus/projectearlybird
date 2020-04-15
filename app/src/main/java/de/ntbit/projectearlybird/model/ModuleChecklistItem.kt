package de.ntbit.projectearlybird.model

import com.parse.ParseClassName
import com.parse.ParseObject
import java.util.*

/**
 * Represents a single item in the [ModuleChecklist]
 */
@ParseClassName("ModuleChecklistItem")
class ModuleChecklistItem : ParseObject {

    var name: String
        get() {
            return getString("name")!!
        }
        set(value) {
            put("name", value)
        }

    var amount: Int
        get() {
            return getInt("amount")
        }
        set(value) {
            put("amount", value)
        }

    var timestamp: Date
        get() {
            return getDate("timestamp")!!
        }
        set(value) {
            put("timestamp", value)
        }

    var isAssigned: Boolean
        get() {
            return getBoolean("isAssigned")
        }
        set(value) {
            put("isAssigned", value)
        }

    var user: User?
        get() {
            return getParseUser("user") as User?
        }
        set(value) {
            if (value != null) {
                put("user", value)
            }
        }

    internal constructor() : super()

    internal constructor(name: String) : this(name, 1)

    internal constructor(name: String, amount: Int) : super() {
        this.name = name
        this.amount = amount
        this.isAssigned = false
        this.user = null
    }

    internal constructor(name: String, amount: Int, user: User) : super() {
        this.name = name
        this.amount = amount
        this.timestamp = timestamp
        this.isAssigned = true
        this.user = user
    }

    fun assign(user: User) {
        this.user = user
        this.isAssigned = true
    }

    fun unassign() {
        this.user = null
        this.isAssigned = false
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

    override fun toString(): String {
        return "ChecklistItem [{name: $name}, {amount: $amount}, {isAssigned: $isAssigned}, " +
                "{user: $user}, {timestamp: $timestamp}]"
    }
}