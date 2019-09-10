package de.ntbit.projectearlybird.manager

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.parse.*
import com.parse.Parse.getApplicationContext
import de.ntbit.projectearlybird.model.UserProfile
import de.ntbit.projectearlybird.ui.HomeActivity
import java.util.*
import java.util.logging.Logger

class ParseManager {
    private val log = Logger.getLogger(this::class.java.simpleName)
    private var currentParseUser: ParseUser? = null

    fun registerUser(username: String, email: String, uHashedPassword: String) {
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
                log.fine(e.message)
            }
        }
    }

    fun loginUser(username: String, password: String, activity: Activity) {
        ParseUser.logInInBackground(username, password) { user, _ ->
            if (user != null) {
                currentParseUser = user
                println("UserProfile logged in: " + userIsLoggedIn())
                Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT)
                    .show()

                updateUserProfile()

                val intent = Intent(activity.applicationContext, HomeActivity::class.java)
                activity.startActivity(intent)
            } else {
                Toast.makeText(
                    getApplicationContext(),
                    "Wrong username/password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun updateUserProfile() {
        updateUserProfile("lastLogin", null)
    }

    fun updateUserProfile(column: String, value: String?) {
        if (value == null) {
            val query = ParseQuery.getQuery<ParseObject>("UserProfile")

            // Retrieve the object by id
            val profileUserId = ParseUser.getCurrentUser().get("userProfileId")!!.toString()
            query.getInBackground(
                profileUserId.substring(1, profileUserId.length - 1)
            ) { entity, e ->
                if (e == null) {
                    // Update the fields we want to
                    entity.put(column, Date(System.currentTimeMillis()))

                    // All other fields will remain the same
                    entity.saveInBackground()
                } else {
                    Log.d("CustomLog", e.message)
                }
            }
        }
    }

    fun logOut() {
        currentParseUser = null
        ParseUser.logOut()
    }

    fun getCurrentUser(): ParseUser? {
        return currentParseUser
    }

    fun userIsLoggedIn(): Boolean {
        return currentParseUser != null
    }

    fun createUserProfile(userProfile: UserProfile) {
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
                log.fine(e.message)
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
