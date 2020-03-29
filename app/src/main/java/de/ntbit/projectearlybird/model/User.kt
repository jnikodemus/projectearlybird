package de.ntbit.projectearlybird.model

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseUser

import java.util.Date
import java.util.logging.Logger

@ParseClassName("_User")
class User: ParseUser {

    private val log = Logger.getLogger(this::class.java.simpleName)

    internal constructor() : super()

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

    var avatar: ParseFile
        get() = getParseFile("avatar")!!
        set(avatar) {
            avatar.save()
            put("avatar", avatar)
        }
}
