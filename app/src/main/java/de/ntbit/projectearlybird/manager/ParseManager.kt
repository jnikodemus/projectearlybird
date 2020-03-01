package de.ntbit.projectearlybird.manager

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.parse.FindCallback
import com.parse.Parse.getApplicationContext
import com.parse.ParseException
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import de.ntbit.projectearlybird.model.UserProfile
import de.ntbit.projectearlybird.ui.HomeActivity
import java.util.*
import java.util.logging.Logger


class ParseManager {
    private val log = Logger.getLogger(this::class.java.simpleName)
    private var currentParseUser: ParseUser? = null
    private var currentUserProfile: UserProfile? = null
    private val allUsers: ArrayList<String> = ArrayList()

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
                initAllUserNames()
            } else {
                log.fine(e.message)
                showToast("Invalid username/password")
            }
        }
    }

    private fun updateLastLogin() {
        currentUserProfile = (ParseUser.getCurrentUser()
            .get("userProfilePtr") as UserProfile)
            .fetch()
        currentUserProfile?.lastLogin = Date(System.currentTimeMillis())
        currentUserProfile?.saveInBackground()
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

    fun getCurrentUser(): ParseUser? {
        return currentParseUser
    }

    /**
     * Returns all registered usernames
     *     @return ArrayList<String>
     */
    // TODO: Make observable?
    private fun initAllUserNames() {
        allUsers.clear()
        val query = ParseUser.getQuery()
        query.findInBackground { users, e ->
            if (e == null) {
                for (user in users) {
                    log.fine("CUSTOMDEBUG " + user.username)
                    if(!user.username.equals(currentParseUser?.username))
                        allUsers.add(user.username)
                }
            } else {
                log.fine("Error")
            }
        }
    }

    // TODO: merge into initAllUserNames()?
    fun getAllUserNames(): ArrayList<String> {
        return allUsers
    }

    fun getCurrentUserProfile(): UserProfile? {
        return currentUserProfile
    }

    fun userIsLoggedIn(): Boolean {
        return currentParseUser != null
    }

    fun logOut() {
        log.fine("logging out")
        currentParseUser = null
        currentUserProfile = null
        ParseUser.logOut()
    }

    private fun showToast(message: String) {
        Toast.makeText(
            getApplicationContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }
}
