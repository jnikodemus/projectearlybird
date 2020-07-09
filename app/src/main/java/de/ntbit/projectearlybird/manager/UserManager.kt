package de.ntbit.projectearlybird.manager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import com.parse.*
import com.squareup.picasso.Picasso
import de.ntbit.projectearlybird.helper.ApplicationContextProvider
import de.ntbit.projectearlybird.helper.Converter
import de.ntbit.projectearlybird.model.Message
import de.ntbit.projectearlybird.model.User
import de.ntbit.projectearlybird.ui.activity.HomeActivity
import kotlinx.android.synthetic.main.activity_login.*
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
     * @param username of the current [User]
     * @param password of the current [User]
     *
     */
    fun loginUser(username: String, password: String, activity: Activity) {
        val checkedUser = isActive(username)
        if(checkedUser != null && !checkedUser.isActive)
            showToast("Account has been disabled")
        else {
            ParseUser.logInInBackground(username, password) { user, _ ->
                if (user != null) {
                    clearAndHideLoginResources(activity)
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
    }

    private fun clearAndHideLoginResources(activity: Activity) {
        activity.actLoginEditTextUsername.isVisible = false
        activity.actLoginEditTextUsername.text.clear()
        activity.actLoginEditTextPassword.isVisible = false
        activity.actLoginEditTextPassword.text.clear()
        activity.actLoginBtnLogin.isVisible = false
        activity.actLoginBtnRegister.isVisible = false
    }

    /**
     * Updates the last login of the current user to the database
     *
     */
    private fun updateLastLogin() {
        val mCurrentUser = getCurrentUser()
        mCurrentUser.saveInBackground()
    }

    /**
     * Getter for the current [User]
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
        val query = ParseQuery.getQuery(User::class.java).whereEqualTo("isActive", true)
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

    /**
     * Checks whether the [username] is present on parse and if so,
     * if the associated account is active.
     */
    fun isActive(username: String): User? {
        val userQuery = ParseQuery.getQuery(User::class.java).whereEqualTo("username", username)
        var suspiciousUser: User? = null
        try {
            suspiciousUser = userQuery.first
        }
        catch (e: ParseException) {
            Log.d("EXCEPTIONDEBUG", e.message)
        }
        finally {
            return suspiciousUser
        }
    }

    // TODO: Delete ActivityStack and everything else what is userspecific!
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
            //Log.d("CUSTOMDEBUG", "UserManager - ${userAvatar.url}")
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

    /**
     * Sets isActive of currentUser to false and calls saveEventually()
     */
    private fun disableAccount() {
        getCurrentUser().isActive = false
        getCurrentUser().saveEventually()
    }

    // TODO: check how to delete the avatar file

    /**
     * Deletes avatar of currentUser and calls saveEventually()
     */
    private fun clearAccount() {
        getCurrentUser().avatar = Converter
            .convertBitmapToParseFileByUri(ApplicationContextProvider
                .context
                .contentResolver, Uri.parse(IMAGE_USER_DEFAULT_URI))
        getCurrentUser().saveEventually()
    }

    /**
     * Calls disableAccount(), clearAccount() and logout() in this sequence
     */
    fun deleteUserAccount() {
        disableAccount()
        clearAccount()
        logOut()
    }
}