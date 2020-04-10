package de.ntbit.projectearlybird.manager

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.parse.*
import com.squareup.picasso.Picasso
import de.ntbit.projectearlybird.data.PebContract
import de.ntbit.projectearlybird.data.PebDbHelper
import de.ntbit.projectearlybird.model.Message
import de.ntbit.projectearlybird.model.User
import de.ntbit.projectearlybird.ui.activity.HomeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

/**
 * A global manager for [User]
 *
 * This class manages all [User] interactions
 *
 * @property allUsersSet contains every [User] stored in the database
 * @property pinnedContacts contains all [User] stored in the local datastore
 * @property pinnedConversationContacts contains all [User] which sent a message to the current [User] stored in the local datastore
 * @property IMAGE_USER_DEFAULT_URI path to the default image
 * @constructor Creates a [UserManager] and initializes th contacts of the current [User] and also fetches all [User]
 */
class UserManager {

    private val allUsersSet: HashSet<User> = HashSet()
    private val pinnedContacts: HashSet<User> = HashSet()
    private val pinnedConversationContacts: HashSet<User> = HashSet()
    private val IMAGE_USER_DEFAULT_URI = "android.resource://de.ntbit.projectearlybird/drawable/icon_default_avatar"


    init {
        Log.d("CUSTOMDEBUG", "${this.javaClass.simpleName} - init executed")
        //val query = ParseQuery.getQuery(Message::class.java)
        //query.fromLocalDatastore()
        if(isLoggedIn()) {
            initMyContacts()
            //initMyConversationContacts()
            initAllUsers()
        }
    }

    /**
     * Registers the user
     *
     * Needs a [username], a correct [email] address and hashed [uHashedPassword]
     *
     *
     * @return either true or false if the register fails or succeeds
     */
    fun registerUser(username: String, email: String, uHashedPassword: String, ctx: Context): Boolean {
        Log.d("CUSTOMDEBUG", "UserManager - Registering new User $username")
        val user = User()
        var success = true
        user.username = username.toLowerCase(Locale.ROOT)
        user.email = email
        user.setPassword(uHashedPassword)
        user.contacts = ArrayList()

        user.signUpInBackground { e ->
            if (e == null) {
                //saveUserLocal(getCurrentUser(), ctx)
                showToast("Registration successful. Please verify your Email")
                // TODO activate automatic login after successful registration
            } else {
                success = false
                Log.d("CUSTOMDEBUG", "UserManager - Error: ${e.message}")
            }
        }
        return success
    }

    /**
     * Login the user with [username] and [password]
     *
     *
     */
    fun loginUser(username: String, password: String, activity: Activity) {
        ParseUser.logInInBackground(username, password) { user, e ->
            if (user != null) {
                updateLastLogin()
                //setUserOnline(user.username, activity.applicationContext)
                //deleteLocalUsers(username, activity.applicationContext)
                //syncLocalUser(username, activity.applicationContext)
                ManagerFactory.initializeAdapter()
                val intent = Intent(activity.applicationContext, HomeActivity::class.java)
                activity.startActivity(intent)
                //user.pinInBackground()
                initAllUsers()
            } else {
                showToast("Invalid username/password")
            }
        }
    }
    /**
     * Updates the last login of the current user to the database
     *
     */
    private fun updateLastLogin() {
        val mCurrentUser = getCurrentUser()
        mCurrentUser.put(PebContract.UserEntry.COLUMN_USER_LASTLOGIN, Date(System.currentTimeMillis()))
        mCurrentUser.saveInBackground()
    }

    /**
     * Getter for the current [User]
     *
     *
     * @return the [User] that is currently logged in on his own device
     */
    fun getCurrentUser(): User {
        return ParseUser.getCurrentUser() as User
    }
    /**
     * Fetches all [User] from the database and saves it to [allUsersSet]
     *
     */
    private fun initAllUsers() {
        //allUsers.clear()
        val query = ParseQuery.getQuery(User::class.java)
        query.findInBackground { users, e ->
            if (e == null) {
                users.remove(getCurrentUser())
                allUsersSet.addAll(users)
            } else {
                Log.d("CUSTOMDEBUG", "${this.javaClass.simpleName} - Error: ${e.message}")
            }
        }
    }

    /**
     * Getter for the current [allUsersSet]
     *
     *
     * @return the set filled with all [User]
     */
    fun getAllUsers(): Collection<User> {
        return allUsersSet
    }

    /**
     * Initializes the contacts of the current [User]
     *
     */
    private fun initMyContacts() {
        val mQuery = ParseQuery.getQuery(Message::class.java)
            .whereContains("threadId", getCurrentUser().objectId)
        val query = ParseQuery.getQuery(User::class.java)
        query.whereMatchesKeyInQuery("objectId", "senderId", mQuery)
        query.findInBackground { ownContacts, e ->
            if(e == null) {
                ownContacts.remove(getCurrentUser())
                pinnedContacts.addAll(ownContacts)
            }
        }
    }

    /**
     * Getter for the contacts of the current [User]
     *
     *
     * @return a set filled with the own contacts of the current [User]
     */
    fun getMyContacts() : Collection<User> {
        return getCurrentUser().contacts
    }

    /**
     * Adds a [User] contact to the local datastore and stores it into the database
     *
     */
    fun addContact(contact : User) {
        if(!contact.equals(getCurrentUser())) {
            pinnedContacts.add(contact)
            getCurrentUser().addContact(contact)
        }
    }

    private fun initMyConversationContacts() {
        Log.d("CUSTOMDEBUG", "${this.javaClass.simpleName} - initMyConversationContacts() executed")
        // Query to get Messages
        val messageQuery = ParseQuery.getQuery(Message::class.java).whereContains("threadId", getCurrentUser().objectId)
        val userQuery = ParseQuery.getQuery(User::class.java)
        userQuery.whereMatchesKeyInQuery("objectId", "senderId", messageQuery)
        userQuery.whereMatchesKeyInQuery("objectId", "recipientId", messageQuery)
        userQuery.findInBackground {
            convContacts, e ->
            if(e == null) {
                convContacts.remove(getCurrentUser())
                pinnedConversationContacts.addAll(convContacts)
            }
        }
    }


    private fun initMyConversationContacts2() {
        // Query to get Messages
        val mQuery = ParseQuery.getQuery(Message::class.java)
            .whereEqualTo("recipient", getCurrentUser())
        val query = ParseQuery.getQuery(User::class.java)
        query.whereMatchesKeyInQuery("objectId", "senderId", mQuery)
        CoroutineScope(IO).launch {
            val convContacts = query.find()
            pinnedConversationContacts.addAll(convContacts)
        }
    }

    fun getMyConversationContacts(): HashSet<User> {
        return pinnedConversationContacts
    }

    fun addNewConversationContact(contact: User) {
        pinnedConversationContacts.add(contact)
        //contact.pinInBackground()
    }

    /**
     * Checks if Parse.getCurrentUser() is null to determine if a user is already logged in and
     *@return true if so
     */
    fun isLoggedIn(): Boolean {
        return ParseUser.getCurrentUser() != null
    }

    // TODO: Delete ActivityStack? and del all pinned objects
    /**
     * Deletes the content of the local datastore and logout the [User]
     *
     *
     */
    fun logOut() {
        ParseObject.unpinAll()
        ParseUser.unpinAll()
        ParseUser.logOut()
    }

    /**
     * Updates the Avatar of the [User] in the database
     *
     *
     */
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
        if(userAvatar != null) {
            imageUri = Uri.parse(userAvatar.url)
            Log.d("CUSTOMDEBUG", "UserManager - ${userAvatar.url}")
        }
        Picasso.get()
            .load(imageUri)
            .resize(400, 400)
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

    /*
    @Deprecated("Use Parse LocalDatastore")
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

 */
}