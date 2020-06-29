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
            else remove("user")
        }

    var creator: User
        get() {
            return getParseUser("creator") as User
        }
        set(value) {
            put("creator", value)
            creatorId = value.objectId
        }

    var creatorId: String
        get() {
            return getString("creatorId")!!
        }
        private set(value) {
            put("creatorId", value)
        }

    var associatedModule: ModuleChecklist
        get() {
            return getParseObject("associatedModule") as ModuleChecklist
        }
        set(value) {
            put("associatedModule", value)
        }

    internal constructor() : super()

    internal constructor(name: String, creator: User, associatedModule: ModuleChecklist):
            this(name, creator, associatedModule, 0)

    internal constructor(name: String, creator: User,
                         associatedModule: ModuleChecklist, amount: Int) : super() {
        this.name = name
        this.amount = amount
        this.isAssigned = false
        this.user = null
        this.creator = creator
        this.associatedModule = associatedModule
        this.acl = associatedModule.acl
        this.timestamp = Date(System.currentTimeMillis())
    }

    internal constructor(name: String, creator: User,
                         associatedModule: ModuleChecklist, amount: Int, user: User):
            super() {
        this.name = name
        this.amount = amount
        this.isAssigned = true
        this.user = user
        this.creator = creator
        this.associatedModule = associatedModule
        this.timestamp = Date(System.currentTimeMillis())
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
        return "ChecklistItem [{objectId: $objectId}, {name: $name}, {amount: $amount}," +
                "{creator: ${creator.username}}, {isAssigned: $isAssigned}, " +
                "{user: ${user?.username}}, {timestamp: $timestamp}]"
    }
}