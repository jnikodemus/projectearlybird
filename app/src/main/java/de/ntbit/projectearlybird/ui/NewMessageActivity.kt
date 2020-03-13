package de.ntbit.projectearlybird.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.parse.ParseUser
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.manager.UserManager
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.new_message_user_row.view.*

class NewMessageActivity : AppCompatActivity() {

    private val mUserManager: UserManager = ManagerFactory.getUserManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        supportActionBar?.title = "Select User"

        fetchAllParseUser()
    }

    companion object{
        val USER_KEY = "USER_KEY"
    }

    private fun fetchAllParseUser() {
        val adapter = GroupAdapter<GroupieViewHolder>()
        for (user in mUserManager.getAllUsers())
            adapter.add(UserItem(user))
        /* TODO: send selected UserItem/User to intent */
        adapter.setOnItemClickListener { item, view ->
            val userItem = item as UserItem
            val intent = Intent(view.context, ChatActivity::class.java)
            intent.putExtra(USER_KEY, userItem.user)
            startActivity(intent)

            finish()
        }
        rv_newMessage_user.adapter = adapter
    }
}

class UserItem(val user: ParseUser): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_new_message.text = user.username
        /*Bilder zu den usernames*/
    }

    override fun getLayout(): Int {
        return R.layout.new_message_user_row
    }
}


