package de.ntbit.projectearlybird.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.manager.UserManager
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.navigation_header.*
import kotlinx.android.synthetic.main.navigation_header.view.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.logging.Logger


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val log = Logger.getLogger(this::class.java.simpleName)
    private val mUserManager: UserManager = ManagerFactory.getUserManager()
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

    /**
     * Loads Avatar from database
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            val uri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            navigation_avatar.setImageBitmap(bitmap)
            mUserManager.updateAvatar(bitmap)
        }
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
        navigationHeader.navigation_username.text = mUserManager.getCurrentUser().username
        navigationHeader.navigation_email.text = mUserManager.getCurrentUser().email
        mUserManager.loadAvatar(navigationHeader.navigation_avatar)
        navigationHeader.navigation_avatar.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
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
                .replace(R.id.fragment_container, ContactsFragment()).commit()
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

    /**
     * Closes [drawer] menu if opened, else minimizes the application
     */
    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START)
        else minimize()
    }

    /**
     * Minimizes the application
     */
    private fun minimize() {
        val startMain = Intent(Intent.ACTION_MAIN)
        startMain.addCategory(Intent.CATEGORY_HOME)
        startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(startMain)
    }

}
