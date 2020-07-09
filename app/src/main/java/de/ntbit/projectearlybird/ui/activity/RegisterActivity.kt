package de.ntbit.projectearlybird.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.helper.InputValidator
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.manager.UserManager
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.logging.Logger

class RegisterActivity : AppCompatActivity() {

    private val log = Logger.getLogger(this::class.java.simpleName)
    private val mUserManager: UserManager = ManagerFactory.getUserManager()
    private val mInputValidator = InputValidator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initialize()
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onSupportNavigateUp()
        onBackPressed()
        return true
    }

    private fun initialize() {
        placeToolbar()
        setClickListener()
    }

    private fun placeToolbar() {

        val toolbar: Toolbar = toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar_tv_root_title.text = "Register"
    }

    private fun setClickListener() {
        actRegisterBtnRegister.setOnClickListener{
            val username: String
            val email: String
            val password: String
            if(inputIsOK()) {
                username = actRegisterEditTxtUsername.text.toString()
                email = actRegisterEditTxtEmail.text.toString()
                password = actRegisterEditTxtPassword.text.toString()
                if(mUserManager.registerUser(username, email, password, this)) {
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
        if(InputValidator.isValidEmail(actRegisterEditTxtEmail.text.toString()))
            return true
        actRegisterEditTxtEmail.error = InputValidator.EMAIL_NOT_VALID
        return false
    }

    private fun isValidEmail() : Boolean {
        return mInputValidator.isValidEmail(actRegisterEditTxtEmail)
    }

    private fun isValidPassword() : Boolean {
        return mInputValidator.isValidPassword(actRegisterEditTxtPassword, actRegisterEditTxtPasswordRetry)
    }
}
