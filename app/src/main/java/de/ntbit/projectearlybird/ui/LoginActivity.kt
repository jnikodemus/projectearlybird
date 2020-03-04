package de.ntbit.projectearlybird.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.parse.ParseUser
import de.ntbit.projectearlybird.R

import de.ntbit.projectearlybird.connection.ParseConnection
import de.ntbit.projectearlybird.manager.ParseManager
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.logging.Logger

class LoginActivity : AppCompatActivity() {

    private val log = Logger.getLogger(this::class.java.simpleName)
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private var mParseManager: ParseManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initialize()
        ParseUser.logOut()
    }

    private fun initialize() {
        val toolbar: Toolbar = toolbar
        setSupportActionBar(toolbar)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        ParseConnection.initialize(this)
        mParseManager = ParseConnection.getParseManager()

        actLoginBtnLogin.setOnClickListener{
            if(actLoginEditTextUsername.text.isNotBlank() &&
                actLoginEditTextPassword.text.isNotBlank()) {
                mParseManager?.loginUser(
                    actLoginEditTextUsername.text.toString(),
                    actLoginEditTextPassword.text.toString(), this
                )
                log.fine("User " + actLoginEditTextUsername.text + " successfully logged in")
            }
        }

        actLoginBtnRegister.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mParseManager?.logOut()
    }
}
