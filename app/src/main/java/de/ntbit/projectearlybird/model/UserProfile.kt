package de.ntbit.projectearlybird.model

import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseUser

import java.util.Date
import java.util.logging.Logger

/* TODO change to extend ParseObject as in
    https://guides.codepath.com/android/Building-Data-driven-Apps-with-Parse
 */

@ParseClassName("UserProfile")
class UserProfile : ParseObject() {
    private val log = Logger.getLogger(this::class.java.simpleName)

    private var firstName: String
        get() = getString("firstName")!!
        set(firstName) {
            put("firstName", firstName)
        }
    private var lastName: String
        get() = getString("lastName")!!
        set(lastName) {
            put("lastName", lastName)
        }
    private var birthday: Long
        get() = getLong("birthday")
        set(birthday) {
            put("birthday", birthday)
        }
    private var sex: String
        get() = getString("sex")!!
        set(sex) {
            put("sex", sex)
        }
    private var lastLogin: Date
        get() = getDate("lastLogin")!!
        set(lastLogin) {
            put("lastLogin", lastLogin)
        }

    /* TODO add groups
    var groups: Collection<Group> = ArrayList()
        private set
     */

    fun fillUnset() {
        this.firstName = "unset"
        this.lastName = "unset"
        this.birthday = 0
        this.sex = "unset"
    }

    /* TODO fetchIfNeeded()
    override fun toString(): String {
        return ("ID: " + this.objectId
                + "\nName: " + this.firstName + " " + this.lastName
                + "\nDay of birth: " + this.birthday
                + "\nSex: " + this.sex
                + "\nLast login: " + this.lastLogin.toString())
        //+ "\nGroups: " + this.groups)
    }
    */
}
