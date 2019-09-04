package de.ntbit.projectearlybird.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.connection.ParseConnection
import de.ntbit.projectearlybird.manager.ParseManager
import kotlinx.android.synthetic.main.activity_register.*
import java.util.logging.Logger

class RegisterActivity : AppCompatActivity() {

    private val log = Logger.getLogger(this::class.java.simpleName)
    private var parseManager: ParseManager? = null
    private val emailRegex : String = "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=" +
            "?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]" +
            "|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])" +
            "?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|" +
            "[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*" +
            "[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-" +
            "\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initialize()
    }

    private fun initialize() {
        parseManager = ParseConnection.getParseManager()
        actRegisterBtnRegister.setOnClickListener{
            if(inputIsOK()) {
                parseManager?.registerUser(
                    actRegisterEditTxtUsername.text.toString(),
                    actRegisterEditTxtEmail.text.toString(),
                    actRegisterEditTxtPassword.text.toString()
                )
                finish()
            }
        }
    }

    private fun inputIsOK() : Boolean {
        return (usernameIsOK() && emailIsOK() && passwordsMatch())
    }

    private fun usernameIsOK() : Boolean {
        if(actRegisterEditTxtUsername.text.isNotBlank())
            return true
        actRegisterEditTxtUsername.error = resources.getString(R.string.app_check_input)
        return false
    }

    private fun emailIsOK() : Boolean {
        if(actRegisterEditTxtEmail.text.isNotBlank() &&
            emailRegex.toRegex().matches(actRegisterEditTxtEmail.text))
            return true
        actRegisterEditTxtEmail.error = resources.getString(R.string.app_check_input)
        return false
    }

    private fun passwordsMatch() : Boolean {
        if(actRegisterEditTxtPassword.text.isNotBlank() &&
                actRegisterEditTxtPassword.text.toString() ==
                actRegisterEditTxtPasswordRetry.text.toString())
            return true
        actRegisterEditTxtPassword.error = resources.getString(R.string.app_check_input)
        actRegisterEditTxtPasswordRetry.error = resources.getString(R.string.app_check_input)
        return false
    }
}
