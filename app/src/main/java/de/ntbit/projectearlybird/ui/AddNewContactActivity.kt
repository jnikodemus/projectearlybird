package de.ntbit.projectearlybird.ui

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.View.OnFocusChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.parse.ParseUser
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.adapter.UserItem
import kotlinx.android.synthetic.main.activity_add_new_contact.*


class AddNewContactActivity : AppCompatActivity() {

    private val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_contact)

        if(Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { username ->
                val query = ParseUser.getQuery()
                query.whereContains("username", username)
                adapter.add(UserItem(query.first))
                adapter.notifyDataSetChanged()
            }
        }
        add_new_contact_rv.adapter = adapter
    }
}
