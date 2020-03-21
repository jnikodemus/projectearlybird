package de.ntbit.projectearlybird.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.parse.ParseUser
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.manager.ManagerFactory
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
        mUserManager.logOut()
        val intent = Intent(this.context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun deleteAccount() {
        Toast.makeText(this.context,"Not available",Toast.LENGTH_SHORT).show()
    }
}