package de.ntbit.projectearlybird.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.helper.ApplicationContextProvider.Companion.context
import kotlinx.android.synthetic.main.activity_information_third_parties.*
import kotlinx.android.synthetic.main.activity_information_version.*
import kotlinx.android.synthetic.main.toolbar.*

class InformationVersionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information_version)
        initialize()
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onSupportNavigateUp()
        onBackPressed()
        return true
    }

    private fun initialize() {
        placeToolbar()
        placeVersionInformation()
    }

    private fun placeToolbar() {
        val toolbar = act_information_version_toolbar
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar_tv_root_title.text = "Version"
    }

    private fun placeVersionInformation() {
        val majorVersion = context.getString(R.string.app_major_version)
        val subVersion = context.getString(R.string.app_sub_version)
        val revVersion = context.getString(R.string.app_rev_version)
        var versionTemplate = context.getString(R.string.app_version_template)
        versionTemplate = versionTemplate.replace("MAJOR", majorVersion.toString())
        versionTemplate = versionTemplate.replace("SUB", subVersion.toString())
        versionTemplate = versionTemplate.replace("REV", revVersion.toString())
        act_information_version_tv_version.text = versionTemplate
    }
}