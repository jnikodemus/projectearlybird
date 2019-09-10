package de.ntbit.projectearlybird.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import de.ntbit.projectearlybird.R

import de.ntbit.projectearlybird.connection.ParseConnection
import de.ntbit.projectearlybird.manager.ParseManager
import kotlinx.android.synthetic.main.activity_login.*
import java.util.logging.Logger

class LoginActivity : AppCompatActivity() {
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private val log = Logger.getLogger(this::class.java.simpleName)
    private var parseManager: ParseManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initialize()
        parseManager?.logOut()
    }

    private fun initialize() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        ParseConnection.initialize(this)
        parseManager = ParseConnection.getParseManager()

        actLoginBtnLogin.setOnClickListener{
            if(actLoginEditTextUsername.text.toString() != "" &&
                actLoginEditTextPassword.text.toString() != "") {
                parseManager?.loginUser(
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
}
