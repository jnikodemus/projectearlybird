package de.ntbit.projectearlybird.ui

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.parse.ParseUser
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.connection.ParseConnection
import de.ntbit.projectearlybird.data.PebContract
import de.ntbit.projectearlybird.data.PebDbHelper
import de.ntbit.projectearlybird.helper.InputValidator
import de.ntbit.projectearlybird.manager.ParseManager
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.logging.Logger

class RegisterActivity : AppCompatActivity() {

    private val log = Logger.getLogger(this::class.java.simpleName)
    private var parseManager: ParseManager? = null
    private lateinit var mDbHelper: PebDbHelper
    private val inputValidator = InputValidator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initialize()
    }

    private fun initialize() {
        val toolbar: Toolbar = toolbar
        setSupportActionBar(toolbar)
        parseManager = ParseConnection.getParseManager()
        actRegisterBtnRegister.setOnClickListener{
            var username: String
            var email: String
            var password: String
            if(inputIsOK()) {
                username = actRegisterEditTxtUsername.text.toString()
                email = actRegisterEditTxtEmail.text.toString()
                password = actRegisterEditTxtPassword.text.toString()
                if(parseManager!!.registerUser(username, email, password, PebDbHelper(this))) {
                    log.fine("User successfully registered ")
                    finish()
                    // TODO activate automatic login after successful registration
                    //startActivity(Intent(this, HomeActivity::class.java))
                }
                else log.fine("User not registered")
            }
        }
    }

    private fun inputIsOK() : Boolean {
        return (isValidUsername() && isValidEmail() && isValidPassword())
    }

    private fun isValidUsername() : Boolean {
        return inputValidator.isValidUsername(actRegisterEditTxtUsername)
    }

    private fun isValidEmail() : Boolean {
        return inputValidator.isValidEmail(actRegisterEditTxtEmail)
    }

    private fun isValidPassword() : Boolean {
        return inputValidator.isValidPassword(actRegisterEditTxtPassword, actRegisterEditTxtPasswordRetry)
    }
}
