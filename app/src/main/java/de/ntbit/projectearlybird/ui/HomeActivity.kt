package de.ntbit.projectearlybird.ui

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import androidx.annotation.ContentView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

import de.ntbit.projectearlybird.R
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.logging.Logger

class HomeActivity : AppCompatActivity() {

    private val log = Logger.getLogger(this::class.java.simpleName)
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initialize()
    }

    private fun initialize() {
        /* Set toolbar */
        val toolbar = toolbar
        setSupportActionBar(toolbar)

        /* Set Navigation Drawer */
        drawer = drawer_layout
        toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.menu_drawer_open,
            R.string.menu_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START)
        else super.onBackPressed()
    }
}
