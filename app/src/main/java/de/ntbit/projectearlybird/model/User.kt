package de.ntbit.projectearlybird.model

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.helper.Converter

import java.util.Date

/**
 * Model corresponding to table "Group" in Parse Database extends [ParseObject]
 *
 * @property emailVerified verification of a valid email
 * @property firstname of the user
 * @property lastName of the user
 * @property birthday of the user
 * @property gender 0 - MALE, 1 - FEMALE, 2 - DIVERSE
 * @property lastLogin when was the last time the user was logged in
 * @property isOnline is the current user online right now
 * @property aboutMe small information the user can write
 * @property avatar of the user
 * @property contacts of the user
 */
@ParseClassName("_User")
class User: ParseUser {

    companion object {
        const val MALE = 0
        const val FEMALE = 1
        const val UNKNOWN = 2
    }

    internal constructor() : super()

    internal constructor(username: String, email: String) {
        this.username = username
        this.email = email
        contacts = ArrayList()
        emailVerified = false
        isOnline = false
        isActive = true
        gender = UNKNOWN
    }

    var emailVerified: Boolean?
        get() = getBoolean("emailVerified")
        set(emailVerified) {
            if (emailVerified != null) {
                put("emailVerified", emailVerified)
            }
        }

    var firstName: String?
        get() = getString("firstName")
        set(firstName) {
            if (firstName != null) {
                put("firstName", firstName)
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

    var isOnline: Boolean
        get() = getBoolean("isOnline")
        set(isOnline) {
            put("isOnline", isOnline)
        }

    var aboutMe: String?
        get() = getString("aboutMe")
        set(aboutMe) {
            if (aboutMe != null) {
                put("aboutMe", aboutMe)
            }
        }

    var isActive: Boolean
        get() = getBoolean("isActive")
        set(isActive) {
            put("isActive", isActive)
        }

    var avatar: ParseFile?
        get() = getParseFile("avatar")
        set(avatar) {
            avatar?.save()
            if (avatar != null) {
                put("avatar", avatar)
            }
        }

    var contacts: ArrayList<User>
        get() = getList<User>("contacts")!! as ArrayList<User>
        set(contacts) {
            put("contacts", contacts)
        }

    /**
     * Adds a new contact to [contacts] and saves it to the parse database
     *
     * @param contact that will be added to the [User] collection [contacts]
     */
    fun addContact(contact: User) {
        addUnique("contacts", contact)
        saveEventually()
    }

    override fun equals(other: Any?): Boolean {
        if(other is User)
            return this.objectId == other.objectId
        return false
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun getUsername(): String {
        return if(isActive) super.getUsername()
        else "disabledAccount"
    }


}
