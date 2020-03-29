package de.ntbit.projectearlybird.manager

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.Adapter
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.parse.*
import com.parse.ktx.whereExists
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.adapter.UserItem
import de.ntbit.projectearlybird.data.PebContract
import de.ntbit.projectearlybird.data.PebDbHelper
import de.ntbit.projectearlybird.model.Message
import de.ntbit.projectearlybird.ui.HomeActivity
import de.ntbit.projectearlybird.ui.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.logging.Logger
import kotlin.collections.HashSet


class UserManager() {
    private val log = Logger.getLogger(this::class.java.simpleName)

    private val allUsersSet: HashSet<ParseUser> = HashSet()
    private val pinnedContacts: HashSet<ParseUser> = HashSet()
    private val pinnedConversationContacts: HashSet<ParseUser> = HashSet()
    val IMAGE_USER_DEFAULT_URI = "android.resource://de.ntbit.projectearlybird/mipmap/ic_compass"

    init {
        val query = ParseQuery.getQuery(Message::class.java)
        query.fromLocalDatastore()
        log.info("UserManager - There are ${query.count()} items in LocalDatastore.")
        if(isLoggedIn()) {
            initMyContacts()
            initMyConversationContacts()
            initAllUsers()
        }
    }

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

    fun loginUser(username: String, password: String, activity: Activity) {
        ParseUser.logInInBackground(username, password) { user, e ->
            if (user != null) {
                updateLastLogin()
                //setUserOnline(user.username, activity.applicationContext)
                deleteLocalUsers(username, activity.applicationContext)
                syncLocalUser(username, activity.applicationContext)
                val intent = Intent(activity.applicationContext, HomeActivity::class.java)
                activity.startActivity(intent)
                user.pinInBackground()
                initAllUsers()
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

    private fun initAllUsers() {
        //allUsers.clear()
        val query = ParseUser.getQuery()
        query.findInBackground { users, e ->
            if (e == null) {
                users.remove(getCurrentUser())
                allUsersSet.addAll(users)
            } else {
                log.fine("Error")
            }
        }
    }

    fun getAllUsers(): Collection<ParseUser> {
        return allUsersSet
    }

    private fun initMyContacts() {
        val query = ParseUser.getQuery()
        query.fromLocalDatastore()
        query.findInBackground { ownContacts, e ->
            if(e == null) {
                ownContacts.remove(getCurrentUser())
                pinnedContacts.addAll(ownContacts)
            }
        }
    }

    fun getMyContacts() : Collection<ParseUser> {
        return pinnedContacts
    }

    fun addNewContact(contact : ParseUser) {
        pinnedContacts.add(contact)
        contact.pinInBackground()
    }

    private fun initMyConversationContacts() {
        // Query to get Messages
        val mQuery = ParseQuery.getQuery(Message::class.java).whereEqualTo("recipient", getCurrentUser())
        val messageCount = mQuery.count()
        Log.d("CUSTOMDEBUG", "$messageCount messages for ${getCurrentUser().username} online")
        val query = ParseUser.getQuery()
        query.whereMatchesKeyInQuery("objectId", "senderId", mQuery)
        query.findInBackground {
            convContacts, e ->
            if(e == null) {
                convContacts.remove(getCurrentUser())
                pinnedConversationContacts.addAll(convContacts)
            }
        }
    }

    private fun initMyConversationContacts2() {
        // Query to get Messages
        val mQuery = ParseQuery.getQuery<Message>(Message::class.java)
            .whereEqualTo("recipient", getCurrentUser())
        val query = ParseUser.getQuery()
        query.whereMatchesKeyInQuery("objectId", "senderId", mQuery)
        CoroutineScope(IO).launch {
            val convContacts = query.find()
            pinnedConversationContacts.addAll(convContacts)
        }
    }

    fun getMyConversationContacts(): HashSet<ParseUser> {
        return pinnedConversationContacts
    }

    fun addNewConversationContact(contact : ParseUser) {
        pinnedConversationContacts.add(contact)
        contact.pinInBackground()
    }

    /* TODO: create getAllLocalUsers() */
    /**
     * Checks if Parse.getCurrentUser() is null to determine if a user is already logged in and
     * returns true if so
     */
    fun isLoggedIn(): Boolean {
        return ParseUser.getCurrentUser() != null
    }

    // TODO: Delete ActivityStack? and del all pinned objects
    fun logOut() {
        ParseObject.unpinAll()
        ParseUser.unpinAll()
        ParseUser.logOut()
    }

    fun updateAvatar(bitmap: Bitmap) {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val image: ByteArray = stream.toByteArray()
        val avatar = ParseFile(image)
        avatar.save()
        getCurrentUser().put("avatar", avatar)
        getCurrentUser().saveEventually()
    }

    fun loadAvatar(img: ImageView) {
        loadAvatar(img, getCurrentUser())
    }

    // TODO: CHECK IMAGE_USER_DEFAULT_URI
    fun loadAvatar(img: ImageView, user: ParseUser) {
        var imageUri = Uri.parse(IMAGE_USER_DEFAULT_URI)
        val userAvatar: ParseFile? = user.getParseFile("avatar")
        Log.d("CUSTOMDEBUG","UserManager - loadAvatar(): userAvatar is null? ${userAvatar == null}")
        if(userAvatar != null)
            imageUri = Uri.parse(userAvatar.url)
        Picasso.get()
            .load(imageUri)
            .resize(400,400)
            .centerCrop()
            .into(img)
    }

    private fun showToast(message: String) {
        Toast.makeText(
            Parse.getApplicationContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    @Deprecated("Use Parse LocalDatastore")
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

    @Deprecated("Use Parse LocalDatastore")
    private fun syncLocalUser(username: String, ctx: Context) {
        saveUserLocal(ParseUser.getCurrentUser(), ctx)
    }

    @Deprecated("Use Parse LocalDatastore")
    private fun deleteLocalUsers(username: String, ctx: Context) {
        val mDbHelper = PebDbHelper(ctx)
        val userDatabase = mDbHelper.writableDatabase
        val whereClause = PebContract.UserEntry.COLUMN_USER_USERNAME + "!=?"
        val whereArgs = arrayOf("0")
        userDatabase.delete(PebContract.UserEntry.TABLE_NAME, whereClause, whereArgs)
        userDatabase.close()
    }
}