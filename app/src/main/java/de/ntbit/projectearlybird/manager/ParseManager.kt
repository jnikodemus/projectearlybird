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
    //private var currentUserProfile: UserProfile? = null

    fun registerUser(username: String, email: String, uHashedPassword: String): Boolean {
        val user = ParseUser()
        var success = true
        user.username = username
        user.email = email
        user.setPassword(uHashedPassword)

        user.signUpInBackground { e ->
            if (e == null) {
                val userProfile = UserProfile()
                userProfile.fillUnset(user)
                saveUserProfile(userProfile)
                showToast("Registration successful. Please verify your Email")
                // TODO activate automatic login after successful registration
            } else {
                log.fine(e.message)
                success = false
            }
        }
        return success
    }

    fun loginUser(username: String, password: String, activity: Activity) {
        ParseUser.logInInBackground(username, password) { user, e ->
            if (user != null) {
                currentParseUser = user
                updateLastLogin()
                val intent = Intent(activity.applicationContext, HomeActivity::class.java)
                activity.startActivity(intent)
            } else {
                log.fine(e.message)
                showToast("Invalid username/password")
            }
        }
    }

    private fun updateLastLogin() {
        val userProfile: UserProfile = (ParseUser.getCurrentUser()
            .get("userProfilePtr") as UserProfile)
            .fetch()
        userProfile.lastLogin = Date(System.currentTimeMillis())
        userProfile.saveInBackground()
    }

    fun getUserProfile(): UserProfile? {
        val query = ParseQuery.getQuery<ParseObject>("UserProfile")
        query.getInBackground(
            this.currentParseUser?.getString("userProfileFk")) {
                result, e -> if (e == null) {
                    log.fine(result.toString())
                } else {
                    log.fine(e.message)
                }
            }
        return null
    }

    private fun saveUserProfile(userProfile: UserProfile) {
        userProfile.saveInBackground { e ->
            if(e != null)
                e.printStackTrace()
            else {
                setUserToProfileRelation(userProfile)
            }
        }
    }

    private fun setUserToProfileRelation(userProfile: UserProfile){
        val currentUser = ParseUser.getCurrentUser()
        if(currentUser != null) {
            currentUser.put("userProfileFk", userProfile.objectId)
            currentUser.put("userProfilePtr", userProfile)
            currentUser.saveInBackground()
        }
        else log.info("currentUser is NULL")
    }

    fun logOut() {
        log.fine("logging out")
        currentParseUser = null
        //currentUserProfile = null
        ParseUser.logOut()
    }

    fun getCurrentUser(): ParseUser? {
        return currentParseUser
    }

    fun userIsLoggedIn(): Boolean {
        return currentParseUser != null
    }

    private fun showToast(message: String) {
        Toast.makeText(
            getApplicationContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }
}
