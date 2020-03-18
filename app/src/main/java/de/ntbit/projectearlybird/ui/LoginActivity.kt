package de.ntbit.projectearlybird.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.Toolbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.parse.Parse
import com.parse.ParseInstallation
import com.parse.ParseSession
import com.parse.ParseUser
import de.ntbit.projectearlybird.R

import de.ntbit.projectearlybird.connection.ParseConnection
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.manager.UserManager
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.logging.Logger

class LoginActivity : AppCompatActivity() {

    private val log = Logger.getLogger(this::class.java.simpleName)
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private lateinit var mUserManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initialize()
        ParseUser.logOut()
    }

    private fun initialize() {
        //val toolbar: Toolbar = toolbar
        //setSupportActionBar(toolbar)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        ParseConnection.initialize(this)
        mUserManager = ManagerFactory.getUserManager()

        setClickListener()
    }

    private fun checkLoggedIn() {
        if(mUserManager.getCurrentUser().isAuthenticated) {
            val intent = Intent(this, HomeActivity::class.java)
            this.startActivity(intent)
        }
    }

    private fun setClickListener() {
        actLoginBtnLogin.setOnClickListener{
            tryLogin(it)
        }
        actLoginBtnRegister.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun tryLogin(view: View) {
        if(actLoginEditTextUsername.text.isNotBlank() &&
            actLoginEditTextPassword.text.isNotBlank()) {
            mUserManager.loginUser(
                actLoginEditTextUsername.text.toString(),
                actLoginEditTextPassword.text.toString(), this
            )
            (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view.windowToken, 0)
            actLoginEditTextUsername.text.clear()
            actLoginEditTextPassword.text.clear()
            log.fine("User " + actLoginEditTextUsername.text + " successfully logged in")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mUserManager.logOut()
    }
}
