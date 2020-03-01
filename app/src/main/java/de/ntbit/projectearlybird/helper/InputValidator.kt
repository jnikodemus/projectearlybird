package de.ntbit.projectearlybird.helper

import android.content.res.Resources
import android.text.TextUtils
import android.widget.EditText
import de.ntbit.projectearlybird.R
import java.util.logging.Logger

class InputValidator {
    private val log = Logger.getLogger(this::class.java.simpleName)
    // TODO: Change from fix String to R.string
    //private val errorText = Resources.getSystem().getString(R.string.app_check_input)
    private val errorText = "Check your Input!"
    private val emailRegex : String = "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=" +
            "?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]" +
            "|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])" +
            "?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|" +
            "[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*" +
            "[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-" +
            "\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"

    fun isValidEmail(actRegisterEditTxtEmail: EditText) : Boolean {
        if(actRegisterEditTxtEmail.text.isNotBlank()
                && !TextUtils.isEmpty(actRegisterEditTxtEmail.text.toString())
                && android.util.Patterns.EMAIL_ADDRESS.matcher(actRegisterEditTxtEmail.text.toString()).matches())
            return true
        actRegisterEditTxtEmail.error = errorText
        return false
    }

    fun isValidUsername(actRegisterEditTxtUsername: EditText) : Boolean {
        if(actRegisterEditTxtUsername.text.isNotBlank() && actRegisterEditTxtUsername.text.length >= 4)
            return true
        actRegisterEditTxtUsername.error = errorText
        return false
    }

    fun isValidPassword(actRegisterEditTxtPassword: EditText, actRegisterEditTxtPasswordRetry: EditText) : Boolean {
        if(actRegisterEditTxtPassword.text.isNotBlank()
            && actRegisterEditTxtPassword.text.toString()
            == actRegisterEditTxtPasswordRetry.text.toString())
            return true
        actRegisterEditTxtPasswordRetry.error = errorText
        return false
    }
}