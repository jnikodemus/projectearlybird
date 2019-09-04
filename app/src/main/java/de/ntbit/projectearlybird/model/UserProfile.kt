package de.ntbit.projectearlybird.model

import com.parse.ParseUser

import java.util.Date
import java.util.logging.Logger
import kotlin.collections.ArrayList

class UserProfile {
    private val log = Logger.getLogger(this::class.java.simpleName)

    var userId: String
        private set
    var objectId: String
    var username: String
        private set
    var firstName: String = "not set"
    var lastName: String = "not set"
    var email: String
        private set
    var birthday: Long = 0
    var sex: String = "undefined"
    var lastLogin: Date
    var groups: Collection<Group> = ArrayList()
        private set

    constructor(user: ParseUser) : super() {
        this.userId = user.objectId
        this.objectId = ""
        this.username = user.username
        this.email = user.email
        this.firstName = "not set"
        this.lastName = "not set"
        this.birthday = 0
        this.sex = "undefined"
        this.lastLogin = Date(System.currentTimeMillis())
        groups = ArrayList()
    }

    constructor(other: UserProfile) : super() {
        this.userId = other.userId
        this.objectId = other.objectId
        this.username = other.username
        this.firstName = other.firstName
        this.lastName = other.lastName
        this.email = other.email
        this.birthday = other.birthday
        this.sex = other.sex
        this.lastLogin = other.lastLogin
        this.groups = other.groups
    }

    override fun toString(): String {
        return ("ID: " + this.userId
                + "\nObjectID: " + this.objectId
                + "\nUsername: " + this.username
                + "\nName: " + this.firstName + " " + this.lastName
                + "\nEmail: " + this.email
                + "\nDay of birth: " + this.birthday
                + "\nSex: " + this.sex
                + "\nLast login: " + this.lastLogin.toString()
                + "\nGroups: " + this.groups)
    }
}
