package de.ntbit.projectearlybird.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import de.ntbit.projectearlybird.R

import de.ntbit.projectearlybird.connection.ParseConnection
import de.ntbit.projectearlybird.manager.ParseManager
import kotlinx.android.synthetic.main.activity_login.*
import java.util.logging.Logger


class LoginActivity : AppCompatActivity() {
    private val logger : Logger = Logger.getLogger(this::class.toString())
    private var parseManager: ParseManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initialize()
    }

    private fun initialize() {
        ParseConnection.initialize(this)

        actLoginBtnLogin.setOnClickListener{
            parseManager!!.loginUser(actLoginEditTextEmail.text.toString(), actLoginEditTextPassword.text.toString(), this)
            if(parseManager!!.userIsLoggedIn())
                startActivity(Intent(this, HomeActivity::class.java))
        }

        actLoginBtnRegister.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        parseManager = ParseConnection.getParseManager()
    }
}
