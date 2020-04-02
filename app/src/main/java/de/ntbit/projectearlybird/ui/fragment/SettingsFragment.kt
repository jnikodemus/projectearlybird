package de.ntbit.projectearlybird.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.ui.activity.ProfileActivity
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    private val mUserManager = ManagerFactory.getUserManager()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    override fun onResume() {
        super.onResume()
        frgmt_settings_tv_about_me.text = mUserManager.getCurrentUser().aboutMe
    }

    private fun initialize() {
        setClicklistener()
        placeUserInformation()
    }

    private fun placeUserInformation() {
        mUserManager.loadAvatar(frgmt_settings_iv_avatar)
        frgmt_settings_tv_username.text = mUserManager.getCurrentUser().username
        frgmt_settings_tv_email.text = mUserManager.getCurrentUser().email
        frgmt_settings_tv_about_me.text = mUserManager.getCurrentUser().aboutMe
    }

    private fun setClicklistener() {
        frgmt_settings_cv_profile.setOnClickListener { showProfileSettings() }
    }

    private fun showProfileSettings() {
        val intent = Intent(this.context, ProfileActivity::class.java)
        startActivity(intent)
    }
}