package de.ntbit.projectearlybird.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.adapter.item.UserItem
import de.ntbit.projectearlybird.helper.ParcelContract
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.manager.UserManager
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.toolbar.*

class NewMessageActivity : AppCompatActivity() {

    private val mUserManager: UserManager = ManagerFactory.getUserManager()
    private val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        initialize()
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onSupportNavigateUp()
        onBackPressed()
        return true
    }

    private fun initialize() {
        placeToolbar()
        connectAdapter()
        setClickListener()
        fetchAllParseUser()
    }

    private fun placeToolbar() {
        val toolbar = toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun connectAdapter() {
        rv_newMessage_user.adapter = adapter
    }

    private fun setClickListener() {
        adapter.setOnItemClickListener { item, view ->
            val userItem = item as UserItem
            //userItem.user.pin()
            val intent = Intent(view.context, ChatActivity::class.java)
            intent.putExtra(ParcelContract.USER_KEY, userItem.user)
            startActivity(intent)
            finish()
        }
    }

    private fun fetchAllParseUser() {
        for (user in mUserManager.getAllUsers())
            adapter.add(UserItem(user))
        adapter.notifyDataSetChanged()
    }
}


