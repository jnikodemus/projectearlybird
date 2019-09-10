package de.ntbit.projectearlybird.ui

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration

import de.ntbit.projectearlybird.R
import java.util.logging.Logger

class HomeActivity : AppCompatActivity() {

    private val log = Logger.getLogger(this::class.java.simpleName)
    //private val navController : NavController = NavController(this)
    //private val appBarConfiguration = AppBarConfiguration(navController.graph)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        init()
    }

    private fun init() {
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
}
