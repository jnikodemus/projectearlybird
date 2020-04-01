package de.ntbit.projectearlybird.ui.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.ui.activity.LoginActivity
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private val mUserManager = ManagerFactory.getUserManager()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        frgmProfileBtnLogout.setOnClickListener {logout()}
        frmProfileBtnDeleteAccount.setOnClickListener { deleteAccount() }
    }

    private fun logout() {
        val dialog = LogoutDialogFragment(
            "If you proceed, all your data will be cleared from this device.",
            "logout",
            "cancel"
        )
        dialog.show(this.parentFragmentManager, "DIALOG_PROFILE_FRAGMENT_LOGOUT")

    }

    private fun deleteAccount() {
        Toast.makeText(this.context,"Not available",Toast.LENGTH_SHORT).show()
    }
}

class LogoutDialogFragment(message: String, positiveButtonText: String, negativeButtonText: String) : DialogFragment() {

    val mUserManager = ManagerFactory.getUserManager()

    private var message: String = message
    private var positiveButtonText: String = positiveButtonText
    private var negativeButtonText: String = negativeButtonText

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