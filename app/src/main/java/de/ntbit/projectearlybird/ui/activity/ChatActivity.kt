package de.ntbit.projectearlybird.ui.activity

import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.adapter.item.ChatSelfItem
import de.ntbit.projectearlybird.helper.ParcelContract
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.manager.MessageManager
import de.ntbit.projectearlybird.manager.UserManager
import de.ntbit.projectearlybird.model.User
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.toolbar.*


class ChatActivity : AppCompatActivity() {

    companion object {
        val TAG = "Chatlog"
    }

    private val adapter = GroupAdapter<GroupieViewHolder>()
    private val mMessageManager: MessageManager = ManagerFactory.getMessageManager()
    private val mUserManager: UserManager = ManagerFactory.getUserManager()
    private lateinit var chatPartner: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        initialize()
    }

    override fun onResume() {
        super.onResume()
        toolbar_tv_root_title.visibility = TextView.GONE
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        toolbar_tv_root_title.visibility = TextView.VISIBLE
    }


    override fun onSupportNavigateUp(): Boolean {
        super.onSupportNavigateUp()
        onBackPressed()
        return true
    }

    private fun initialize() {
        chatPartner = intent.getParcelableExtra(ParcelContract.USER_KEY)
        placeToolbar()

        act_chat_rv_log.adapter = adapter
        /* Resize Recyclerview if SoftKeyboard is selected */
        (act_chat_rv_log.layoutManager as LinearLayoutManager).stackFromEnd = true

        setClickListener()

        listenForMessage(chatPartner)
    }

    private fun setClickListener() {
        act_chat_btn_send.setOnClickListener {
            sendMessage()
        }
    }

    private fun placeToolbar() {
        val thisToolbar = act_chat_toolbar
        setSupportActionBar(thisToolbar as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        mUserManager.loadAvatar(toolbar_iv_image, chatPartner)
        toolbar_iv_image.visibility = ImageView.VISIBLE
        toolbar_tv_title.text = chatPartner.username
        /*
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
         */
    }

    private fun listenForMessage(partner: User) {
        /* CoroutineScope(IO).launch{
             val messages = async{
                 mMessageManager.getMessagesByPartner(partner, act_chat_rv_log)
             }.await()
         }*/
        mMessageManager.getMessagesByPartner(partner, act_chat_rv_log)
        mMessageManager.subscribeToPartner(partner, act_chat_rv_log)
    }

    /* Sending a message from currentuser to chosen contact */
    private fun sendMessage() {
        val text = act_chat_et_message.text.toString()
        val message = mMessageManager.sendMessage(text, chatPartner)
        if (message != null) {
            adapter.add(
                ChatSelfItem(
                    message
                )
            )
            act_chat_rv_log.smoothScrollToPosition(adapter.itemCount - 1)
        }
        act_chat_et_message.text.clear()
    }
}