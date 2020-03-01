package de.ntbit.projectearlybird.ui

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.view.MenuItem

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.navigation_header.view.*
import kotlinx.android.synthetic.main.toolbar.*

import de.ntbit.projectearlybird.R
import com.google.android.material.navigation.NavigationView
import de.ntbit.projectearlybird.connection.ParseConnection
import de.ntbit.projectearlybird.manager.ParseManager
import de.ntbit.projectearlybird.model.UserProfile
import java.util.logging.Logger


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val log = Logger.getLogger(this::class.java.simpleName)
    private val parseManager: ParseManager? = ParseConnection.getParseManager()
    private var userProfile: UserProfile? = null
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initialize()
    }

    private fun initialize() {
        /* Set toolbar */
        placeToolbar()
        /* Assemble and set Navigation Drawer */
        buildNavigation()
        /* Get profile of currentUser and place it in the application */
        placeUserInformation()
        /* Place information about app */
        placeAppInformation()
        /* Select and inflate specific Fragment */
        selectMenuItem(0)
    }

    private fun placeToolbar() {
        val toolbar = toolbar
        setSupportActionBar(toolbar)
    }

    private fun buildNavigation() {
        drawer = navigation_drawer_layout
        navigation_menu_view.setNavigationItemSelectedListener(this)
        toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.menu_drawer_open,
            R.string.menu_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun placeUserInformation() {
        val navigationHeader = navigation_menu_view.getHeaderView(0)
        userProfile = parseManager?.getUserProfile()
        navigationHeader.navigation_username.text = parseManager?.getCurrentUser()?.username
        navigationHeader.navigation_email.text = parseManager?.getCurrentUser()?.email
    }

    private fun placeAppInformation() {
    }

    private fun selectMenuItem(itemIndex: Int) {
        onNavigationItemSelected(navigation_menu_view.menu.getItem(itemIndex).setChecked(true))
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when(p0.itemId) {
            R.id.nav_groups -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, GroupsFragment()).commit()
            R.id.nav_contacts -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, GroupsFragment()).commit()
            R.id.nav_messages -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ConversationsFragment()).commit()
            R.id.nav_profile -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProfileFragment()).commit()
            R.id.nav_settings -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SettingsFragment()).commit()
            R.id.nav_info -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, InfoFragment()).commit()
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START)
        else super.onBackPressed()
    }
}
