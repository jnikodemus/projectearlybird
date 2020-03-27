package de.ntbit.projectearlybird.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import com.parse.ParseUser
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.model.Group
import de.ntbit.projectearlybird.model.Test
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
        //val group = intent.getParcelableExtra<Group>(CreateGroupActivity.GROUP_KEY)
    }

    private fun placeToolbar() {
        // TODO: Set toolbar.title
        val toolbar = actGroupToolbar
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.title = "GROUPNAME"
    }
}
