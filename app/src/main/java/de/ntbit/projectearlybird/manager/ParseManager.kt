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
import com.parse.ParseObject
import com.parse.ParseQuery

class ParseManager {
    private val log = Logger.getLogger(this::class.java.simpleName)
    private var currentParseUser: ParseUser? = null
    private var currentUserProfile: UserProfile? = null

    fun registerUser(username: String, email: String, uHashedPassword: String): Boolean {
        val user = ParseUser()
        var success = true
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
                saveUserProfile(UserProfile(user))
                //createUserProfile(UserProfile(ParseUser.getCurrentUser()))
                // TODO activate automatic login after successful registration
                //loginUser(username, uHashedPassword)
            } else {
                Toast.makeText(getApplicationContext(), e.message, Toast.LENGTH_SHORT).show()
                log.fine(e.message)
                success = false
            }
        }
        return success
    }

    fun loginUser(username: String, password: String) {
        ParseUser.logInInBackground(username, password) { user, _ ->
            if (user != null) {
                currentParseUser = user
                updateUserProfile()
            } else {
                Toast.makeText(
                    getApplicationContext(),
                    "Wrong username/password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun loginUser(username: String, password: String, activity: Activity) {
        ParseUser.logInInBackground(username, password) { user, _ ->
            if (user != null) {
                currentParseUser = user
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
            val profileUserId = ParseUser.getCurrentUser().get("userProfileFk")!!.toString()
            query.getInBackground(
                profileUserId.substring(1, profileUserId.length - 1)) {
                    entity, e -> if (e == null) {
                        // Update the fields we want to
                        entity.put(column, Date(System.currentTimeMillis()))

                        // All other fields will remain the same
                        entity.saveInBackground()
                    } else {
                        log.fine(e.message)
                    }
                }
            }
    }

    fun logOut() {
        log.fine("logging out")
        currentParseUser = null
        currentUserProfile = null
        ParseUser.logOut()
    }

    fun getCurrentUser(): ParseUser? {
        return currentParseUser
    }

    fun userIsLoggedIn(): Boolean {
        return currentParseUser != null
    }
/*
    fun getUserProfile() : UserProfile? {
        if(currentUserProfile == null)
            retrieveUserProfile()
        log.info("User:\n" + currentUserProfile.toString())
        return currentUserProfile
    }

    fun retrieveUserProfile() {
        val query = ParseQuery.getQuery<ParseObject>("UserProfile")
        query.getInBackground((currentParseUser?.getJSONArray("userProfileId")?.
            get(0)).toString()) { result, e -> if (e == null) {
                currentUserProfile = UserProfile(result, currentParseUser!!.email)
                log.info("User:\n" + currentUserProfile.toString())
            } else {
                log.info("Something went terribly wrong")
            }
        }
    }
    */

    // TODO change to getInBackground
    /*
    fun getUserProfile(): UserProfile? {
        if (currentUserProfile == null) {
            val query = ParseQuery.getQuery<ParseObject>("UserProfile")
            return UserProfile(
                query.get((currentParseUser?.getJSONArray("userProfileId")?.get(0) as String?))
            )
        } else return currentUserProfile
    }
     */

    private fun saveUserProfile(userProfile: UserProfile) {
        userProfile.saveInBackground { e ->
            if(e != null)
                e.printStackTrace()
            else updateUserUnique("userProfileFk", userProfile.objectId)
        }
    }
    /*
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
    */


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
