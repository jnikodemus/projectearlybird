package de.ntbit.projectearlybird.ui.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initialize()
    }

    private fun initialize() {
        placeToolbar()
        setClickListener()
    }

    private fun placeToolbar() {
        val thisToolbar = act_profile_toolbar
        setSupportActionBar(thisToolbar as Toolbar)
        supportActionBar?.title = "Profile"
    }

    private fun setClickListener() {
        act_profile_btn_logout.setOnClickListener { logout() }
        act_profile_btn_delete_account.setOnClickListener { deleteAccount() }
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