package de.ntbit.projectearlybird.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.parse.ParseUser
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.model.Group
import kotlinx.android.synthetic.main.activity_group.*
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
        val toolbar = actGroupToolbar
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.title = "GROUPNAME"
    }
}
