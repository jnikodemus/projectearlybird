package de.ntbit.projectearlybird.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.parse.ParseUser
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.adapter.UserItem
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.manager.UserManager
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.toolbar.*

class NewMessageActivity : AppCompatActivity() {

    private val mUserManager: UserManager = ManagerFactory.getUserManager()
    private val adapter = GroupAdapter<GroupieViewHolder>()

    companion object{
        val USER_KEY = "USER_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        initialize()
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
    }

    private fun connectAdapter() {
        rv_newMessage_user.adapter = adapter
    }

    private fun setClickListener() {
        adapter.setOnItemClickListener { item, view ->
            val userItem = item as UserItem
            userItem.user.pin()
            val intent = Intent(view.context, ChatActivity::class.java)
            intent.putExtra(USER_KEY, userItem.user)
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

/*class UserItem(val user: ParseUser): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_new_message.text = user.username
        /*Bilder zu den usernames*/
    }

    override fun getLayout(): Int {
        return R.layout.new_message_user_row
    }
}*/


