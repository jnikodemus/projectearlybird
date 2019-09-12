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

    var userId: String
        get() = getString("userId")!!
        set(userId) {
            addUnique("userId", userId)
        }
    /*
    var username: String
        get() = getString("username")!!
        private set
     */
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
    /*
    var email: String
        get() = getString("email")!!
        private set
     */
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
        this.userId = ""
        //this.username = ""
        //this.email = ""
        this.firstName = ""
        this.lastName = ""
        this.birthday = 0
        this.sex = ""
        this.lastLogin = Date(0)
        //groups = ArrayList()
    }

    constructor(user: ParseUser) : super() {
        this.userId = user.objectId
        //this.username = user.username
        //this.email = user.email
        this.firstName = ""
        this.lastName = ""
        this.birthday = 0
        this.sex = ""
        this.lastLogin = Date(System.currentTimeMillis())
        //groups = ArrayList()
    }

    /*
    constructor(parseObject: ParseObject, email: String) : super() {
        this.userId = parseObject.get("userId").toString()
        //this.username = parseObject.get("username").toString()
        //this.email = email
        this.firstName = parseObject.get("firstName").toString()
        this.lastName = parseObject.get("lastName").toString()
        this.birthday = parseObject.get("birthday").toString().toLong()
        this.sex = parseObject.get("sex").toString()
        this.lastLogin = Date(System.currentTimeMillis())
        //groups = ArrayList()
    }
    */

    constructor(other: UserProfile) : super() {
        this.userId = other.userId
        //this.username = other.username
        this.firstName = other.firstName
        this.lastName = other.lastName
        //this.email = other.email
        this.birthday = other.birthday
        this.sex = other.sex
        this.lastLogin = other.lastLogin
        //this.groups = other.groups
    }

    override fun toString(): String {
        return ("ID: " + this.userId
                //+ "\nUsername: " + this.username
                + "\nName: " + this.firstName + " " + this.lastName
                //+ "\nEmail: " + this.email
                + "\nDay of birth: " + this.birthday
                + "\nSex: " + this.sex
                + "\nLast login: " + this.lastLogin.toString())
        //+ "\nGroups: " + this.groups)
    }
}
