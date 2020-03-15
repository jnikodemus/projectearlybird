package de.ntbit.projectearlybird.manager

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import android.widget.Toast
import com.parse.*
import de.ntbit.projectearlybird.data.PebContract
import de.ntbit.projectearlybird.data.PebDbHelper
import de.ntbit.projectearlybird.ui.HomeActivity
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.logging.Logger


class UserManager {
    private val log = Logger.getLogger(this::class.java.simpleName)

    private val allUsernames: ArrayList<String> = ArrayList()
    private val allUsers: ArrayList<ParseUser> = ArrayList()

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
        userDatabase.close()
    }

    fun loginUser(username: String, password: String, activity: Activity) {
        ParseUser.logInInBackground(username, password) { user, e ->
            if (user != null) {
                updateLastLogin()
                //setUserOnline(user.username, activity.applicationContext)
                deleteLocalUsers(username, activity.applicationContext)
                syncLocalUser(username, activity.applicationContext)
                subscribeToPersonalChannel(user)
                val intent = Intent(activity.applicationContext, HomeActivity::class.java)
                activity.startActivity(intent)
                initAllUserNames()
                initAllUsers()
            } else {
                log.fine(e.message)
                showToast("Invalid username/password")
            }
        }
    }

    private fun subscribeToPersonalChannel(user: ParseUser) {
        ParsePush.subscribeInBackground(user.objectId)
    }

    private fun deleteLocalUsers(username: String, ctx: Context) {
        val mDbHelper = PebDbHelper(ctx)
        val userDatabase = mDbHelper.writableDatabase
        val whereClause = PebContract.UserEntry.COLUMN_USER_USERNAME + "!=?"
        val whereArgs = arrayOf("0")
        userDatabase.delete(PebContract.UserEntry.TABLE_NAME, whereClause, whereArgs)
        userDatabase.close()
    }

    private fun syncLocalUser(username: String, ctx: Context) {
        saveUserLocal(ParseUser.getCurrentUser(), ctx)
    }

    private fun setUserOnline(username: String, ctx: Context) {
        val mDbHelper = PebDbHelper(ctx)
        val userDatabase = mDbHelper.writableDatabase
        val valuesToInsert = ContentValues()
        valuesToInsert.put(PebContract.UserEntry.COLUMN_USER_IS_ONLINE, PebContract.UserEntry.IS_ONLINE)
        userDatabase.update(
            PebContract.UserEntry.TABLE_NAME, valuesToInsert,
            "username=?", arrayOf(username))
        userDatabase.close()
    }

    private fun updateLastLogin() {
        val mCurrentUser = ParseUser.getCurrentUser()
        mCurrentUser.put(PebContract.UserEntry.COLUMN_USER_LASTLOGIN, Date(System.currentTimeMillis()))
        mCurrentUser.saveInBackground()
    }

    fun getCurrentUser(): ParseUser {
        return ParseUser.getCurrentUser()
    }

    /**
     * Returns all registered usernames
     *     @return ArrayList<String>
     */
    // TODO: Make observable?
    private fun initAllUserNames() {
        allUsernames.clear()
        val query = ParseUser.getQuery()
        query.findInBackground { users, e ->
            if (e == null) {
                for (user in users) {
                    log.fine("CUSTOMDEBUG " + user.username)
                    if(!user.username.equals(getCurrentUser().username))
                        allUsernames.add(user.username)
                }
            } else {
                log.fine("Error")
            }
        }
    }

    private fun initAllUsers() {
        allUsers.clear()
        val query = ParseUser.getQuery()
        query.findInBackground { users, e ->
            if (e == null) {
                for (user in users) {
                    log.fine("CUSTOMDEBUG - User: " + user.username + " Id: " + user.objectId)
                    if(!user.objectId.equals(getCurrentUser().objectId))
                        allUsers.add(user)
                }
            } else {
                log.fine("Error")
            }
        }
    }

    // TODO: merge into initAllUserNames()?
    fun getAllUserNames(): ArrayList<String> {
        return allUsernames
    }

    fun getAllUsers(): Collection<ParseUser> {
        return allUsers
    }

    // TODO: Check what exactly is isAuthenticated
    fun userIsLoggedIn(): Boolean {
        return getCurrentUser().isAuthenticated
    }

    fun logOut() {
        log.fine("logging out")
        ParseUser.logOut()
    }

    fun updateAvatar(bitmap: Bitmap) {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val image: ByteArray = stream.toByteArray()
        getCurrentUser().put("avatar", ParseFile(image))
        getCurrentUser().saveInBackground()
    }

    fun loadAvatar(img: ImageView) {
        loadAvatar(img, getCurrentUser())
    }

    fun loadAvatar(img : ImageView, user: ParseUser) {
        user.getParseFile("avatar")?.getDataInBackground { data, e ->
            if (e == null) {
                val bmp = BitmapFactory.decodeByteArray(data, 0, data.size)
                img.setImageBitmap(bmp)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(
            Parse.getApplicationContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

}