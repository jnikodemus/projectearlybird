package de.ntbit.projectearlybird.ui

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

        val mParseManager = ParseConnection.getParseManager()

        supportActionBar?.title = "Select User"

        val adapter = GroupAdapter<GroupieViewHolder>()

        if (mParseManager != null) {
            for (name in mParseManager.getAllUserNames())
                adapter.add(UserItem(name))
        }
        rv_newMessage_user.adapter = adapter

        fetchAllParseUser()

    }

    private fun fetchAllParseUser() {
        val parseManager = ParseManager()
        val ref = parseManager.getAllUserNames()

        ref.forEach{
            Log.d("New Message", it.toString())
        }

    }
}

class UserItem(val username: String): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_new_message.text = username
    }

    override fun getLayout(): Int {
        return R.layout.new_message_user_row
    }
}


