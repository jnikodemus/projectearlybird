package de.ntbit.projectearlybird.model

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser
import de.ntbit.projectearlybird.R
import java.io.File

import java.util.Date
import java.util.logging.Logger

/* TODO change to extend ParseObject as in
    https://guides.codepath.com/android/Building-Data-driven-Apps-with-Parse
 */

@ParseClassName("UserProfile")
class UserProfile : ParseObject() {

    private val log = Logger.getLogger(this::class.java.simpleName)

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
    var birthday: Date
        get() = getDate("birthday")!!
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
    var messages: List<Message>
        get() = getList<Message>("messages")!!
        set(messages) {
            put("messages", messages)
        }
    var groups: List<Group>
        get() = getList<Group>("groups")!!
        set(groups) {
            put("groups", groups)
        }
    var userPtr: ParseUser
        get() = getParseUser("userPtr")!!
        set(userPtr) {
            put("userPtr", userPtr)
        }
    var avatar: ParseFile
        get() = getParseFile("avatar")!!
        set(avatar) {
            put("avatar", avatar)
        }

    /* TODO add groups
    var groups: Collection<Group> = ArrayList()
        private set
     */

    fun fillUnset(userPtr: ParseUser) {
        this.firstName = "unset"
        this.lastName = "unset"
        this.birthday = Date(0)
        this.sex = "unset"
        this.messages = ArrayList()
        this.groups = ArrayList()
        this.lastLogin = Date(System.currentTimeMillis())
        this.userPtr = userPtr
        //this.avatar = ParseFile(File(R.drawable.ic_launcher_foreground.toString()))
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
