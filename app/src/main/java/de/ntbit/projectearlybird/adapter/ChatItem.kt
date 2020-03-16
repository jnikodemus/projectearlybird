package de.ntbit.projectearlybird.adapter

import com.parse.ParseUser
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.model.Message
import kotlinx.android.synthetic.main.chat_contact_row.view.*
import kotlinx.android.synthetic.main.chat_self_row.view.*
import java.text.SimpleDateFormat
import java.util.*


/* TODO: Change tv_self_row to something more useful */
open class ChatItem(val message: Message): Item<GroupieViewHolder>() {
    val mUserManager = ManagerFactory.getUserManager()
    /* TODO: CHANGE TO getCurrentLocale */
    private val userLocale = Locale("de")
    private val datePattern = "HH:mm"
    val format = SimpleDateFormat(datePattern, userLocale)

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tv_self_row.text = message.body
        viewHolder.itemView.chatSelfRow_tvTimestamp.text = format.format(message.timestamp)
        mUserManager.loadAvatar(viewHolder.itemView.chat_self_row_iv_avatar)
    }

    override fun getLayout(): Int {
        return R.layout.chat_self_row
    }
}

class ChatFromItem(message: Message, private val user: ParseUser): ChatItem(message) {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tv_contact_row.text = message.body
        viewHolder.itemView.chatContactRow_tvTimestamp.text = format.format(message.timestamp)
        mUserManager.loadAvatar(viewHolder.itemView.chat_contact_row_iv_avatar, user)
    }
    override fun getLayout(): Int {
        return R.layout.chat_contact_row
    }
}

class ChatSelfItem(message: Message): ChatItem(message)
