package de.ntbit.projectearlybird.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.analytics.FirebaseAnalytics
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.connection.ParseConnection
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.manager.UserManager

class LoadingActivity : AppCompatActivity() {

    private lateinit var mFirebaseAnalytics: FirebaseAnalytics
    private lateinit var mUserManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        initialize()
    }

    private fun initialize() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        ParseConnection.initialize(this)
        mUserManager = ManagerFactory.getUserManager()
        checkLoggedIn()
    }

    /**
     * Checks if a user is already logged in and starts HomeActivity if true
     * right after calling [ManagerFactory.initializeAdapter]
     */
    private fun checkLoggedIn() {
        lateinit var intent: Intent
        var delay: Long = 0
        if(mUserManager.isLoggedIn()) {
            ManagerFactory.initializeAdapter()
            intent = Intent(this, HomeActivity::class.java)
            delay = 500
        }
        else {
            intent = Intent(this, LoginActivity::class.java)
        }
        startDelayed(intent, delay)
    }

    private fun startDelayed(intent: Intent, ms: Long) {
        val handler = Handler()
        handler.postDelayed(Runnable {
            run {
                startActivity(intent)
                finish()
            }
        }, ms)
    }
}
