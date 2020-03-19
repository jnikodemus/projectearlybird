package de.ntbit.projectearlybird.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.parse.ParseQuery
import com.parse.ParseUser
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.adapter.ChatSelfItem
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.manager.MessageManager
import de.ntbit.projectearlybird.model.Message
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


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

        chatPartner = intent.getParcelableExtra(NewMessageActivity.USER_KEY)

        /* Resize Recyclerview if SoftKeyboard is selected */
        (rv_chat_log.layoutManager as LinearLayoutManager).stackFromEnd = true

        //supportActionBar?.title = user.username
        listenForMessage(chatPartner)

        bt_chat_send.setOnClickListener {
            sendMessage()
        }

        val queryUser = ParseQuery.getQuery(Message::class.java)
        queryUser.fromLocalDatastore()
        Log.d("CUSTOMDEBUG","there are ${queryUser.count()} items in localDatastore.")
    }

    private fun listenForMessage(partner: ParseUser) {
        mMessageManager.getMessagesByPartner(partner, rv_chat_log)
        mMessageManager.subscribeToPartner(partner, rv_chat_log)
    }

    /*Sending a message from currentuser to chosen contact*/
    private fun sendMessage() {
        val text = et_chat_enterMessage.text.toString()
        val message = mMessageManager.sendMessage(text, chatPartner)
        if(message != null) {
            adapter.add(ChatSelfItem(message))
            rv_chat_log.smoothScrollToPosition(adapter.itemCount - 1)
        }
        et_chat_enterMessage.text.clear()
    }
}