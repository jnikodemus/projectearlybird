package de.ntbit.projectearlybird.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.parse.ParseUser
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.manager.ParseManager
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.chat_contact_row.view.*
import kotlinx.android.synthetic.main.chat_self_row.view.*

class ChatActivity : AppCompatActivity() {

    companion object{
        val TAG = "Chatlog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val user = intent.getParcelableExtra<ParseUser>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = user.username

        createTestLayout()

        bt_chat_send.setOnClickListener {
            sendMessage()
        }

    }

    private fun sendMessage(){
        val parseManager = ParseManager()
        val text = et_chat_enterMessage.text.toString()
        parseManager.sendMessage(text, intent.getParcelableExtra<ParseUser>(NewMessageActivity.USER_KEY))
        et_chat_enterMessage.text.clear()
    }

    private fun createTestLayout() {
        val adapter = GroupAdapter<GroupieViewHolder>()
        adapter.add(ChatFromItem("FROM MESSAGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE"))
        adapter.add(ChatSelfItem("YESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS SELF MESSAGE"))
        adapter.add(ChatFromItem("YEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE"))
        adapter.add(ChatSelfItem("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"))

        rv_chat_log.adapter = adapter
    }
}

class ChatFromItem(val text: String): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tv_contact_row.text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_contact_row
    }
}

class ChatSelfItem(val text: String): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tv_self_row.text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_self_row
    }
}