package de.ntbit.projectearlybird.manager

import android.app.Activity
import android.widget.Toast
import com.parse.*
import com.parse.Parse.getApplicationContext
import de.ntbit.projectearlybird.model.UserProfile
import java.util.logging.Logger

class ParseManager {
    private val logger: Logger = Logger.getLogger(this::class.toString())
    private var currentParseUser: ParseUser? = null

    public fun registerUser(username: String, email: String, uHashedPassword: String) : Unit {
        val user = ParseUser()
        user.username = username
        user.email = email
        user.setPassword(uHashedPassword)

        user.signUpInBackground { e ->
            if (e == null) {
                Toast.makeText(
                    getApplicationContext(),
                    "Registration successful. Please verify your Email",
                    Toast.LENGTH_SHORT
                ).show()
                createUserProfile(UserProfile(ParseUser.getCurrentUser()))
            } else {
                Toast.makeText(getApplicationContext(), e.message, Toast.LENGTH_SHORT).show()
                logger.fine(e.message)
            }
        }
    }

    public fun loginUser(username: String, password: String, activity: Activity) : Unit {

    }

    public fun logOut() : Unit {
        currentParseUser = null
        ParseUser.logOut()
    }

    public fun getCurrentUser() : ParseUser? {
        return currentParseUser
    }

    public fun userIsLoggedIn() : Boolean {
        return currentParseUser != null
    }

    public fun createUserProfile(userProfile: UserProfile) {
        val newUserProfile = ParseObject("UserProfile")
        newUserProfile.addUnique("userId", userProfile.userId)
        newUserProfile.put("username", userProfile.username)
        newUserProfile.put("firstName", userProfile.firstName)
        newUserProfile.put("lastName", userProfile.lastName)
        newUserProfile.put("birthday", userProfile.birthday)
        newUserProfile.put("sex", userProfile.sex)
        newUserProfile.put("lastLogin", userProfile.lastLogin)
        newUserProfile.put("groups", userProfile.groups)
        // Saves the new object.
        // Notice that the SaveCallback is totally optional!
        newUserProfile.saveInBackground { e ->
            if (e != null)
                logger.fine(e.message)
            else
                updateUserUnique("userProfileId", newUserProfile.objectId)
        }
    }

    private fun updateUserUnique(column: String, userProfileId: String) {
        val currentUser = ParseUser.getCurrentUser()
        if (currentUser != null) {
            // Other attributes than "email" will remain unchanged!
            currentUser.addUnique(column, userProfileId)

            // Saves the object.
            // Notice that the SaveCallback is totally optional!
            currentUser.saveInBackground {
            }
        }
    }
}
