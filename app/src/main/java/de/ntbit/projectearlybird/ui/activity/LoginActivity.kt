package de.ntbit.projectearlybird.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import de.ntbit.projectearlybird.R

import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.manager.UserManager
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mUserManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initialize()
    }

    private fun initialize() {
        mUserManager = ManagerFactory.getUserManager()
        setClickListener()
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
                actLoginEditTextPassword.text.toString(), this)
            (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view.windowToken, 0)
            actLoginEditTextUsername.text.clear()
            actLoginEditTextPassword.text.clear()
        }
    }
}
