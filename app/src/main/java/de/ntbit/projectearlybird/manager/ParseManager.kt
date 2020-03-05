package de.ntbit.projectearlybird.manager

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.parse.Parse.getApplicationContext
import com.parse.ParseUser
import de.ntbit.projectearlybird.data.PebContract
import de.ntbit.projectearlybird.data.PebDbHelper
import de.ntbit.projectearlybird.model.User
import de.ntbit.projectearlybird.model.UserProfile
import de.ntbit.projectearlybird.ui.HomeActivity
import java.util.*
import java.util.logging.Logger


class ParseManager {
    private val log = Logger.getLogger(this::class.java.simpleName)
    private var mCurrentParseUser: ParseUser? = null
    //private var mCurrentUserProfile: UserProfile? = null
    private val allUsers: ArrayList<String> = ArrayList()

    fun registerUser(username: String, email: String, uHashedPassword: String, ctx: Context): Boolean {
        val user = ParseUser()
        var success = true
        user.username = username
        user.email = email
        user.setPassword(uHashedPassword)

        user.signUpInBackground { e ->
            if (e == null) {
                saveUserLocal(ParseUser.getCurrentUser(), ctx)
                showToast("Registration successful. Please verify your Email")
                // TODO activate automatic login after successful registration
            } else {
                log.fine(e.message)
                success = false
            }
        }
        return success
    }

    private fun saveUserLocal(user: ParseUser, ctx: Context) {
        val mDbHelper = PebDbHelper(ctx)
        val userDatabase = mDbHelper.writableDatabase
        val valuesToInsert = ContentValues()
        valuesToInsert.put(PebContract.UserEntry._ID, user.objectId)
        valuesToInsert.put(PebContract.UserEntry.COLUMN_USER_EMAIL_VERIFIED, 0)
        //valuesToInsert.put(PebContract.UserEntry.COLUMN_USER_ACL, user.acl)
        //valuesToInsert.put(PebContract.UserEntry.COLUMN_USER_UPDATED_AT, user.updatedAt)
        //valuesToInsert.put(PebContract.UserEntry.COLUMN_USER_AUTHDATA, user.auth)
        valuesToInsert.put(PebContract.UserEntry.COLUMN_USER_USERNAME, user.username)
        //valuesToInsert.put(PebContract.UserEntry.COLUMN_USER_CREATED_AT, user.createdAt)
        //valuesToInsert.put(PebContract.UserEntry.COLUMN_USER_PASSWORD, user.password)
        valuesToInsert.put(PebContract.UserEntry.COLUMN_USER_EMAIL, user.email)
        valuesToInsert.put(PebContract.UserEntry.COLUMN_USER_FIRSTNAME, user.getString("firstName"))
        valuesToInsert.put(PebContract.UserEntry.COLUMN_USER_LASTNAME, user.getString("lastName"))
        valuesToInsert.put(PebContract.UserEntry.COLUMN_USER_GENDER, PebContract.UserEntry.GENDER_UNKNOWN)
        //valuesToInsert.put(PebContract.UserEntry.COLUMN_USER_LASTLOGIN, user.getInt("lastLogin"))
        //valuesToInsert.put(PebContract.UserEntry.COLUMN_USER_BIRTHDAY, null)
        valuesToInsert.put(PebContract.UserEntry.COLUMN_USER_AVATAR, user.getBytes("avatar"))
        valuesToInsert.put(PebContract.UserEntry.COLUMN_USER_IS_ONLINE, PebContract.UserEntry.IS_OFFLINE)
        userDatabase.insert(PebContract.UserEntry.TABLE_NAME, null, valuesToInsert)
    }

    fun loginUser(username: String, password: String, activity: Activity) {
        ParseUser.logInInBackground(username, password) { user, e ->
            if (user != null) {
                mCurrentParseUser = user
                updateLastLogin()
                setUserOnline(user.username, activity.applicationContext)
                val intent = Intent(activity.applicationContext, HomeActivity::class.java)
                activity.startActivity(intent)
                initAllUserNames()
            } else {
                log.fine(e.message)
                showToast("Invalid username/password")
            }
        }
    }

    private fun setUserOnline(username: String, ctx: Context) {
        val mDbHelper = PebDbHelper(ctx)
        val userDatabase = mDbHelper.writableDatabase
        val valuesToInsert = ContentValues()
        valuesToInsert.put(PebContract.UserEntry.COLUMN_USER_IS_ONLINE, 1)
        userDatabase.insert(PebContract.UserEntry.TABLE_NAME, null, valuesToInsert)
    }

    private fun updateLastLogin() {
        val mCurrentUser = ParseUser.getCurrentUser()
        mCurrentUser.put("lastLogin", Date(System.currentTimeMillis()))
        mCurrentUser.saveInBackground()
    }

    @Deprecated("UserProfile is being deleted")
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
        return mCurrentParseUser
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
                    if(!user.username.equals(mCurrentParseUser?.username))
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

    fun userIsLoggedIn(): Boolean {
        return mCurrentParseUser != null
    }

    fun logOut() {
        log.fine("logging out")
        mCurrentParseUser = null
        //mCurrentUserProfile = null
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
