package de.ntbit.projectearlybird.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.connection.ParseConnection
import de.ntbit.projectearlybird.manager.ParseManager
import de.ntbit.projectearlybird.model.User
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.new_message_user_row.view.*

class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)



        supportActionBar?.title = "Select User"

        fetchAllParseUser()
    }

    private fun fetchAllParseUser() {
        val adapter = GroupAdapter<GroupieViewHolder>()
        val mParseManager = ParseConnection.getParseManager()
        if (mParseManager != null) {
            for (user in mParseManager.getAllUsers())
                adapter.add(UserItem(user.username))
        }
        /* TODO: send selected UserItem/User to intent */
        adapter.setOnItemClickListener { item, view ->
            val intent = Intent(view.context, ChatActivity::class.java)
            startActivity(intent)

            finish()
        }
        rv_newMessage_user.adapter = adapter
    }
}

class UserItem(val username: String): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_new_message.text = username
        /*Bilder zu den usernames*/
    }

    override fun getLayout(): Int {
        return R.layout.new_message_user_row
    }
}


