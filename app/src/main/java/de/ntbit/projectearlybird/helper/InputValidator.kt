package de.ntbit.projectearlybird.helper

import android.text.TextUtils
import android.widget.EditText
import java.util.logging.Logger

/**
 * Provides static methods to validate userinput
 */
class InputValidator {
    private val log = Logger.getLogger(this::class.java.simpleName)

    companion object {

        const val EMAIL_NOT_VALID = "Please provide a valid email"
        const val ERROR_NOT_NULL_NOR_EMPTY = "Must not be empty or blank"

        /**
         * Returns whether the given [email] is valid or not
         * using [android.util.Patterns.EMAIL_ADDRESS]
         */
        fun isValidEmail(email: String) : Boolean {
            if(email.isNotBlank()
                && !TextUtils.isEmpty(email)
                && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
                return true
            return false
        }

        /**
         * Returns whether the provided [editText] [isNullOrBlank] or [isNullOrEmpty]
         */
        fun isValidInputNotNullNotEmpty(editText: EditText) : Boolean {
            if(editText.text.isNullOrBlank() || editText.text.isNullOrEmpty()) {
                editText.error = ERROR_NOT_NULL_NOR_EMPTY
                return false
            }
            return true
        }
    }

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

    /**
     * Returns whether the given [email] is valid or not using [isValidEmail]
     */
    @Deprecated("Use companion object!")
    fun isValidEmail(actRegisterEditTxtEmail: EditText) : Boolean {
        if(isValidEmail(actRegisterEditTxtEmail.text.toString()))
            return true
        actRegisterEditTxtEmail.error = errorText
        return false
    }

    /**
     * Checks if the passed [actRegisterEditTxtUsername] has more than
     * three chars and therefore is not null nor blank
     */
    fun isValidUsername(actRegisterEditTxtUsername: EditText) : Boolean {
        if(actRegisterEditTxtUsername.text.isNotBlank() && actRegisterEditTxtUsername.text.length >= 4)
            return true
        actRegisterEditTxtUsername.error = errorText
        return false
    }

    /**
     * Returns whether the content of the passed [actRegisterEditTxtPassword]
     * corresponds to the [actRegisterEditTxtPasswordRetry] and is neither empty nor blank
     */
    fun isValidPassword(actRegisterEditTxtPassword: EditText, actRegisterEditTxtPasswordRetry: EditText) : Boolean {
        if(actRegisterEditTxtPassword.text.isNotBlank()
            && actRegisterEditTxtPassword.text.toString()
            == actRegisterEditTxtPasswordRetry.text.toString())
            return true
        actRegisterEditTxtPasswordRetry.error = errorText
        return false
    }
}