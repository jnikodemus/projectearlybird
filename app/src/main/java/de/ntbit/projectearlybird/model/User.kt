package de.ntbit.projectearlybird.model

import android.os.Parcelable
import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseUser
import kotlinx.android.parcel.Parcelize

import java.util.Date
import java.util.logging.Logger

/* TODO change to extend ParseObject as in
    https://guides.codepath.com/android/Building-Data-driven-Apps-with-Parse
 */

@Parcelize
@ParseClassName("User")
class User : ParseUser(), Parcelable {

    private val log = Logger.getLogger(this::class.java.simpleName)

    var firstName: String?
        get() = getString("firstName")
        set(firstName) {
            if (firstName != null) {
                put("firstName", firstName)
            }
        }

    var emailVerified: Boolean?
        get() = getBoolean("emailVerified")
        set(emailVerified) {
            if (emailVerified != null) {
                put("emailVerified", emailVerified)
            }
        }

    var lastName: String?
        get() = getString("lastName")
        set(lastName) {
            if (lastName != null) {
                put("lastName", lastName)
            }
        }
    var birthday: Date?
        get() = getDate("birthday")
        set(birthday) {
            if (birthday != null) {
                put("birthday", birthday)
            }
        }
    var gender: Int
        get() = getInt("gender")
        set(gender) {
            put("gender", gender)
        }
    var lastLogin: Date
        get() = getDate("lastLogin")!!
        set(lastLogin) {
            put("lastLogin", lastLogin)
        }

    var avatar: ParseFile?
        get() = getParseFile("avatar")
        set(avatar) {
            if (avatar != null) {
                put("avatar", avatar)
            }
        }

    /* TODO add groups
    var groups: Collection<Group> = ArrayList()
        private set
     */

    fun fillUnset() {
        this.firstName = "unset"
        this.lastName = "unset"
        this.birthday = Date(0)
        this.gender = 2
        //this.messages = ArrayList()
        this.lastLogin = Date(System.currentTimeMillis())
        //this.avatar = ParseFile(File(R.drawable.ic_launcher_foreground.toString()))
    }

    /* TODO fetchIfNeeded()
    override fun toString(): String {
        return ("ID: " + this.objectId
                + "\nName: " + this.firstName + " " + this.lastName
                + "\nDay of birth: " + this.birthday
                + "\nGender: " + this.gender
                + "\nLast login: " + this.lastLogin.toString())
        //+ "\nGroups: " + this.groups)
    }
    */
}
