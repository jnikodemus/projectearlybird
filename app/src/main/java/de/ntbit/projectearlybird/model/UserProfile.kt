package de.ntbit.projectearlybird.model

import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseUser

import java.util.Date
import java.util.logging.Logger

// TODO change to extend ParseObject as in https://guides.codepath.com/android/Building-Data-driven-Apps-with-Parse

@ParseClassName("UserProfile")
class UserProfile : ParseObject {
    private val log = Logger.getLogger(this::class.java.simpleName)

    var userFk: String
        get() = getString("userFk")!!
        private set(userFk) {
            put("userFk", userFk)
        }
    var username: String
        get() = getString("username")!!
        private set(username) {
            put("username", username)
        }
    var firstName: String
        get() = getString("firstName")!!
        set(firstName) {
            put("firstName", firstName)
        }
    var lastName: String
        get() = getString("lastName")!!
        set(lastName) {
            put("lastName", lastName)
        }
    var email: String
        get() = getString("email")!!
        private set(email) {
            put("email", email)
        }
    var birthday: Long
        get() = getLong("birthday")
        set(birthday) {
            put("birthday", birthday)
        }
    var sex: String
        get() = getString("sex")!!
        set(sex) {
            put("sex", sex)
        }
    var lastLogin: Date
        get() = getDate("lastLogin")!!
        set(lastLogin) {
            put("lastLogin", lastLogin)
        }

    /* TODO add groups
    var groups: Collection<Group> = ArrayList()
        private set
     */

    constructor() : super() {
        this.userFk = ""
        this.username = ""
        this.email = ""
        this.firstName = ""
        this.lastName = ""
        this.birthday = 0
        this.sex = ""
        this.lastLogin = Date(0)
        //groups = ArrayList()
    }

    constructor(user: ParseUser) : super() {
        this.userFk = user.objectId
        this.username = user.username
        this.email = user.email
        this.firstName = ""
        this.lastName = ""
        this.birthday = 0
        this.sex = ""
        this.lastLogin = Date(System.currentTimeMillis())
        //groups = ArrayList()
    }

    constructor(parseObject: ParseObject) : super() {
        this.userFk = parseObject.get("userId").toString()
        //this.username = parseObject.get("username").toString()
        //this.email = email
        this.firstName = parseObject.get("firstName").toString()
        this.lastName = parseObject.get("lastName").toString()
        this.birthday = parseObject.get("birthday").toString().toLong()
        this.sex = parseObject.get("sex").toString()
        this.lastLogin = Date(System.currentTimeMillis())
        //groups = ArrayList()
    }

    constructor(other: UserProfile) : super() {
        this.userFk = other.userFk
        this.username = other.username
        this.email = other.email
        this.firstName = other.firstName
        this.lastName = other.lastName
        this.birthday = other.birthday
        this.sex = other.sex
        this.lastLogin = other.lastLogin
        //this.groups = other.groups
    }

    override fun toString(): String {
        return ("ID: " + this.userFk
                + "\nUsername: " + this.username
                + "\nEmail: " + this.email
                + "\nName: " + this.firstName + " " + this.lastName
                + "\nDay of birth: " + this.birthday
                + "\nSex: " + this.sex
                + "\nLast login: " + this.lastLogin.toString())
        //+ "\nGroups: " + this.groups)
    }
}
