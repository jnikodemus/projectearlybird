package de.ntbit.projectearlybird.helper

import android.content.res.Resources
import android.text.TextUtils
import android.widget.EditText
import de.ntbit.projectearlybird.R
import java.util.logging.Logger

/**
 * Class for validating the input from specific input text fields. Globally reachable
 *
 * @property log for logging
 * @property errorText contains an error text
 * @property EMAIL_NOT_VALID contains a text for displaying if the email is not valid
 * @property ERROR_NOT_NULL_NOR_EMPTY contains a text for displaying if the text field is not empty nor blank
 */

// TODO: Get ErrorStrings from strings.xml
class InputValidator {
    private val log = Logger.getLogger(this::class.java.simpleName)
    private val errorText = "Check your input!"

    companion object {

        //val EMAIL_NOT_VALID: String = Resources.getSystem().getString(R.string.error_invalid_email)
        val EMAIL_NOT_VALID: String = "Provided email is not valid"
        //val ERROR_NOT_NULL_NOR_EMPTY: String = Resources.getSystem().getString(R.string.error_null_or_empty)
        val ERROR_NOT_NULL_NOR_EMPTY: String = "Must not be empty nor blank"

        /**
         * Checks the input for a valid [email]
         * @param email email that should be checked
         * @return [Boolean]: false if [email] is not valid, true else
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
         * @return [Boolean]: false if the [editText] is null or empty, true else
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
     * @return [Boolean]: false if [actRegisterEditTxtEmail] is not valid, true else
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
     * @param actRegisterEditTxtUsername username that should be checked
     * @return [Boolean]: false if [actRegisterEditTxtUsername] is not valid, true else
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
     * @param actRegisterEditTxtPassword contains the password
     * @param actRegisterEditTxtPasswordRetry contains the password in the retry field
     * @return [Boolean]: false if [actRegisterEditTxtPassword] does not match
     * [actRegisterEditTxtPasswordRetry], true else
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