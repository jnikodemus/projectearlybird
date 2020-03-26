package de.ntbit.projectearlybird.ui

import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.parse.ParseUser
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.adapter.ChatSelfItem
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.manager.MessageManager
import de.ntbit.projectearlybird.manager.UserManager
import kotlinx.android.synthetic.main.activity_chat.*


class ChatActivity : AppCompatActivity() {

    companion object{
        val TAG = "Chatlog"
    }

    private val adapter = GroupAdapter<GroupieViewHolder>()
    private val mMessageManager: MessageManager = ManagerFactory.getMessageManager()
    private val mUserManager: UserManager = ManagerFactory.getUserManager()
    private lateinit var chatPartner: ParseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        initialize()
    }

    private fun initialize() {
        chatPartner = intent.getParcelableExtra(NewMessageActivity.USER_KEY)
        placeToolbar()

        rv_chat_log.adapter = adapter
        /* Resize Recyclerview if SoftKeyboard is selected */
        (rv_chat_log.layoutManager as LinearLayoutManager).stackFromEnd = true

        setClickListener()

        listenForMessage(chatPartner)
    }

    private fun setClickListener() {
        actChatButtonSend.setOnClickListener {
            sendMessage()
        }
    }

    private fun placeToolbar() {
        // TODO: Set toolbar.title to ChatPartner.username
        val thisToolbar = actChatToolbar
        setSupportActionBar(thisToolbar as Toolbar)
        supportActionBar?.title = chatPartner.username

        supportActionBar?.displayOptions = (supportActionBar?.displayOptions?.or(ActionBar.DISPLAY_SHOW_CUSTOM)!!)
        val imageView = ImageView(supportActionBar!!.themedContext)
        imageView.scaleType = ImageView.ScaleType.CENTER
        mUserManager.loadAvatar(imageView, chatPartner)
        val layoutParams: ActionBar.LayoutParams = ActionBar.LayoutParams(
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT, Gravity.END
        )
        imageView.layoutParams = layoutParams
        supportActionBar?.customView = imageView
    }

    private fun listenForMessage(partner: ParseUser) {
        mMessageManager.getMessagesByPartner(partner, rv_chat_log)
        mMessageManager.subscribeToPartner(partner, rv_chat_log)
    }

    /* Sending a message from currentuser to chosen contact */
    private fun sendMessage() {
        val text = actChatEditTextMessage.text.toString()
        val message = mMessageManager.sendMessage(text, chatPartner)
        if(message != null) {
            adapter.add(ChatSelfItem(message))
            rv_chat_log.smoothScrollToPosition(adapter.itemCount - 1)
        }
        actChatEditTextMessage.text.clear()
    }
}