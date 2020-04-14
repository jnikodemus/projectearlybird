package de.ntbit.projectearlybird.helper

import android.text.TextUtils
import android.widget.EditText
import java.util.logging.Logger

/**
 * Class for validating the input from specific input text fields. Globally reachable
 *
 * @property log for logging
 * @property errorText contains an error text
 * @property emailRegex format how an email should look like
 * @property EMAIL_NOT_VALID contains a text for displaying if the email is not valid
 * @property ERROR_NOT_NULL_NOR_EMPTY contains a text for displaying if the text field is not empty nor blank
 */
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

    companion object {

        const val EMAIL_NOT_VALID = "Please provide a valid email"
        const val ERROR_NOT_NULL_NOR_EMPTY = "Must not be empty nor blank"

        /**
         * Checks the input for a valid [email]
         * @param email email that should be checked
         * @return false if [email] is not valid, true else
         */
        fun isValidEmail(email: String) : Boolean {
            if(email.isNotBlank()
                && !TextUtils.isEmpty(email)
                && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
                return true
            return false
        }

        /**
         * Checks the [edidtext] if the input is not null and not empty
         *
         * @param editText the edittext that should be checked
         * @return false if the [editText] is null or empty, true else
         */
        fun isValidInputNotNullNotEmpty(editText: EditText) : Boolean {
            if(editText.text.isNullOrBlank() || editText.text.isNullOrEmpty()) {
                editText.error = ERROR_NOT_NULL_NOR_EMPTY
                return false
            }
            return true
        }
    }



    /**
     * Checks the input for a valid email, used in [RegisterActivity]
     * @param actRegisterEditTxtEmail email that should be checked
     * @return false if [actRegisterEditTxtEmail] is not valid, true else
     */
    fun isValidEmail(actRegisterEditTxtEmail: EditText) : Boolean {
        if(isValidEmail(actRegisterEditTxtEmail.text.toString()))
            return true
        actRegisterEditTxtEmail.error = errorText
        return false
    }

    /**
     * Checks the input for a valid username
     * @param actRegisterEditTxtUsername username that should be checked
     * @return false if [actRegisterEditTxtUsername] is not valid, true else
     */
    fun isValidUsername(actRegisterEditTxtUsername: EditText) : Boolean {
        if(actRegisterEditTxtUsername.text.isNotBlank() && actRegisterEditTxtUsername.text.length >= 4)
            return true
        actRegisterEditTxtUsername.error = errorText
        return false
    }

    /**
     * Checks if [actRegisterEditTxtPassword] matches [actRegisterEditTxtPasswordRetry]
     * @param actRegisterEditTxtPassword contains the password
     * @param actRegisterEditTxtPasswordRetry contains the password in the retry field
     * @return false if [actRegisterEditTxtPassword] does not match [actRegisterEditTxtPasswordRetry], true else
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