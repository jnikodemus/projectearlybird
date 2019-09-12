package de.ntbit.projectearlybird.model

import com.parse.ParseObject
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

    constructor() : super() {
        this.userId = ""
        this.objectId = ""
        this.username = ""
        this.email = ""
        this.firstName = ""
        this.lastName = ""
        this.birthday = 0
        this.sex = ""
        this.lastLogin = Date(0)
        groups = ArrayList()
    }

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

    constructor(parseObject: ParseObject, email: String) : super() {
        this.userId = parseObject.get("userId").toString()
        this.objectId = parseObject.objectId
        this.username = parseObject.get("username").toString()
        this.email = email
        this.firstName = parseObject.get("firstName").toString()
        this.lastName = parseObject.get("lastName").toString()
        this.birthday =  parseObject.get("birthday").toString().toLong()
        this.sex = parseObject.get("sex").toString()
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
