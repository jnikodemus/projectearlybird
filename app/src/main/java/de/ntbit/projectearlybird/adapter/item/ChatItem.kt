package de.ntbit.projectearlybird.adapter.item


import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.model.Message
import de.ntbit.projectearlybird.model.User
import kotlinx.android.synthetic.main.row_chat_contact.view.*
import kotlinx.android.synthetic.main.row_chat_self.view.*
import java.text.SimpleDateFormat
import java.util.*


/* TODO: Change tv_self_row to something more useful */
/**
 * A [ChatItem] for the ChatActivity.
 *
 * Root class for the ChatActivity bubbles.
 *
 * @param message content of the chat bubble.
 * @property userLocale displays the time in userlocale. Currently set to 'de'
 * @property datePattern displays the date in HH:mm
 * @property format formats the timestamp
 * @constructor Creates an empty ChatItem.
 */
open class ChatItem(val message: Message): Item<GroupieViewHolder>() {
    //val mUserManager = ManagerFactory.getUserManager()
    /* TODO: CHANGE TO getCurrentLocale and check if now -24h = gestern*/
    private val userLocale = Locale("de")
    private val datePattern = "HH:mm"
    val format = SimpleDateFormat(datePattern, userLocale)

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.row_chat_self_tv_message.text = message.body
        viewHolder.itemView.row_chat_self_tv_timestamp.text = format.format(message.timestamp)
        //mUserManager.loadAvatar(viewHolder.itemView.chat_self_row_iv_avatar)
    }

    override fun getLayout(): Int {
        return R.layout.row_chat_self
    }
}

/**
 * A [ChatItem] from partner to user for the ChatActivity.
 *
 */
class ChatFromItem(message: Message, private val user: User): ChatItem(message) {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.row_chat_contact_tv_message.text = message.body
        viewHolder.itemView.row_chat_contact_tv_timestamp.text = format.format(message.timestamp)
        //mUserManager.loadAvatar(viewHolder.itemView.chat_contact_row_iv_avatar, user)
    }
    override fun getLayout(): Int {
        return R.layout.row_chat_contact
    }
}

/**
 * A [ChatItem] from user to partner for the ChatActivity.
 */
class ChatSelfItem(message: Message): ChatItem(message)
