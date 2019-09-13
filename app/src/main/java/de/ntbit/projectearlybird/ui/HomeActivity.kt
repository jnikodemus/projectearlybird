package de.ntbit.projectearlybird.ui

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import kotlinx.android.synthetic.main.toolbar.*

import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.connection.ParseConnection
import de.ntbit.projectearlybird.manager.ParseManager
import de.ntbit.projectearlybird.model.UserProfile
import java.util.logging.Logger


class HomeActivity : AppCompatActivity() {

    private val log = Logger.getLogger(this::class.java.simpleName)
    private val parseManager: ParseManager? = ParseConnection.getParseManager()
    private var userProfile: UserProfile? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initialize()
    }

    private fun initialize() {
        /* Set toolbar */
        setToolbar()
    }

    private fun setToolbar() {
        val toolbar = toolbar
        setSupportActionBar(toolbar)
    }
}
