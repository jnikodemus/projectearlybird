package de.ntbit.projectearlybird.ui.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.manager.ManagerFactory
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    val mUserManager = ManagerFactory.getUserManager()
    val currentUser = mUserManager.getCurrentUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initialize()
    }

    override fun onPause() {
        super.onPause()
        if(act_profile_et_first_name.text.toString() != currentUser.firstName)
            currentUser.firstName = act_profile_et_first_name.text.toString()

        if(act_profile_et_last_name.text.toString() != currentUser.lastName)
            currentUser.lastName = act_profile_et_last_name.text.toString()

        if(act_profile_et_about_me.text.toString() != currentUser.aboutMe
            && act_profile_et_about_me.text.toString().length <= 42)
                currentUser.aboutMe = act_profile_et_about_me.text.toString()

        Log.d("CUSTOMDEBUG", "" + currentUser.aboutMe?.length)

        currentUser.saveEventually()
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onSupportNavigateUp()
        onBackPressed()
        return true
    }

    private fun initialize() {
        placeToolbar()
        placeUserProfile()
        setClickListener()
    }

    private fun placeToolbar() {
        val thisToolbar = act_profile_toolbar
        setSupportActionBar(thisToolbar as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Profile"
    }

    private fun placeUserProfile() {
        act_profile_et_username.text = currentUser.username
        act_profile_et_email.text = currentUser.email
        if(currentUser.firstName != null)
            act_profile_et_first_name.text = SpannableStringBuilder(currentUser.firstName)
        if(currentUser.lastName != null)
            act_profile_et_last_name.text = SpannableStringBuilder(currentUser.lastName)
        if(currentUser.aboutMe != null)
            act_profile_et_about_me.text = SpannableStringBuilder(currentUser.aboutMe)
    }

    private fun setClickListener() {
        act_profile_btn_logout.setOnClickListener { logout() }
        act_profile_btn_delete_account.setOnClickListener { deleteAccount() }
        /*
        act_profile_et_first_name.setOnFocusChangeListener {
                view, hasFocus ->
            if(!hasFocus && act_profile_et_first_name.text.toString() != currentUser.firstName) {
                currentUser.firstName = act_profile_et_first_name.text.toString()
                currentUser.saveEventually()
            }
        }
        act_profile_et_last_name.setOnFocusChangeListener {
                view, hasFocus ->
            if(!hasFocus && act_profile_et_last_name.text.toString() != currentUser.firstName) {
                currentUser.lastName = act_profile_et_last_name.text.toString()
                currentUser.saveEventually()
            }
        }
        act_profile_et_about_me.setOnFocusChangeListener {
                view, hasFocus ->
            if(!hasFocus && act_profile_et_about_me.text.toString() != currentUser.aboutMe) {
                currentUser.aboutMe = act_profile_et_about_me.text.toString()
                currentUser.saveEventually()
            }
        }
         */
    }

    private fun logout() {
        val dialog = LogoutDialogFragment(
            "If you proceed, all your data will be cleared from this device.",
            "logout",
            "cancel"
        )
        dialog.show(this.supportFragmentManager, "DIALOG_PROFILE_FRAGMENT_LOGOUT")
    }

    private fun deleteAccount() {
        Toast.makeText(this,"Not available", Toast.LENGTH_SHORT).show()
    }
}

class LogoutDialogFragment(
    private var message: String, private var positiveButtonText: String,
    private var negativeButtonText: String
) : DialogFragment() {

    val mUserManager = ManagerFactory.getUserManager()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage(message)
                //builder.setMessage(R.string.dialog_fire_missiles)
                .setPositiveButton(positiveButtonText
                ) { dialog, id ->
                    mUserManager.logOut()
                    val intent = Intent(this.context, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                .setNegativeButton(negativeButtonText
                ) { dialog, id ->
                    // User cancelled the dialog
                }
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}