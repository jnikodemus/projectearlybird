package de.ntbit.projectearlybird.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.parse.ParseInstallation
import com.parse.ParsePush
import com.parse.ParseQuery
import com.parse.ParseUser
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.manager.MessageManager
import de.ntbit.projectearlybird.manager.UserManager
import de.ntbit.projectearlybird.model.Message
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.chat_contact_row.view.*
import kotlinx.android.synthetic.main.chat_self_row.view.*
import kotlinx.android.synthetic.main.new_message_user_row.view.*


class ChatActivity : AppCompatActivity() {

    companion object{
        val TAG = "Chatlog"
    }

    private val adapter = GroupAdapter<GroupieViewHolder>()
    private val mMessageManager: MessageManager = ManagerFactory.getMessageManager()
    private lateinit var chatPartner: ParseUser
    //private val mUserManager: UserManager = ManagerFactory.getUserManager()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        rv_chat_log.adapter = adapter

        chatPartner = intent.getParcelableExtra<ParseUser>(NewMessageActivity.USER_KEY)
        //supportActionBar?.title = user.username
        listenForMessage(chatPartner)
        bt_chat_send.setOnClickListener {
            sendMessage()
        }
    }

    private fun listenForMessage(partner: ParseUser) {
        mMessageManager.getMessagesByPartner(partner, adapter)
    }
    /*Sending a message from currentuser to chosen contact*/
    private fun sendMessage(){
        val text = et_chat_enterMessage.text.toString()
        mMessageManager.sendMessage(text, chatPartner)
        adapter.add(ChatSelfItem(text))
        et_chat_enterMessage.text.clear()
    }
}

class ChatFromItem(val text: String, private val user: ParseUser): Item<GroupieViewHolder>(){
    private val mUserManager = ManagerFactory.getUserManager()
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tv_contact_row.text = text
        mUserManager.loadAvatar(viewHolder.itemView.chat_contact_row_iv_avatar, user)
    }

    override fun getLayout(): Int {
        return R.layout.chat_contact_row
    }
}

class ChatSelfItem(val text: String): Item<GroupieViewHolder>(){
    private val mUserManager = ManagerFactory.getUserManager()
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tv_self_row.text = text
        mUserManager.loadAvatar(viewHolder.itemView.chat_self_row_iv_avatar)
    }

    override fun getLayout(): Int {
        return R.layout.chat_self_row
    }
}