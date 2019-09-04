package de.ntbit.projectearlybird.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import de.ntbit.projectearlybird.R

import de.ntbit.projectearlybird.connection.ParseConnection
import java.util.logging.Logger


class LoginActivity : AppCompatActivity() {

    private val log = Logger.getLogger(this.localClassName)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ParseConnection.initialize(this)
    }
}
