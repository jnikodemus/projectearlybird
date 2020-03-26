package de.ntbit.projectearlybird.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import de.ntbit.projectearlybird.R
import kotlinx.android.synthetic.main.toolbar.*

class GroupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        initialize()
    }

    private fun initialize() {
        placeToolbar()
    }

    private fun placeToolbar() {
        // TODO: Set toolbar.title
        val toolbar = toolbar
        setSupportActionBar(toolbar)
    }
}
