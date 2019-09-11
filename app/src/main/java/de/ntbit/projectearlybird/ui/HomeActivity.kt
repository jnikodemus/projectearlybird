package de.ntbit.projectearlybird.ui

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

import de.ntbit.projectearlybird.R
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.logging.Logger

class HomeActivity : AppCompatActivity() {

    private val log = Logger.getLogger(this::class.java.simpleName)
    private var drawer: DrawerLayout? = null
    //private val navController : NavController = NavController(this)
    //private val appBarConfiguration = AppBarConfiguration(navController.graph)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initialize()
    }

    private fun initialize() {
        val toolbar: Toolbar = toolbar
        setSupportActionBar(toolbar)
        drawer = drawer_layout
        log.info("drawer is null? " + (drawer == null))
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.menu_drawer_open,
            R.string.menu_drawer_close)
        drawer?.addDrawerListener(toggle)
        toggle.syncState()

        /*
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.full_screen_destination) {
                toolbar.visibility = View.GONE
                bottomNavigationView.visibility = View.GONE
            } else {
                toolbar.visibility = View.VISIBLE
                bottomNavigationView.visibility = View.VISIBLE
            }
        }
        */
    }

    override fun onBackPressed() {
        if(drawer?.isDrawerOpen(GravityCompat.START)!!)
            drawer?.closeDrawer(GravityCompat.START)
        else super.onBackPressed()
    }
}
