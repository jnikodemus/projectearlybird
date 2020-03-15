package de.ntbit.projectearlybird.adapter

import com.parse.ParseUser
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.manager.ManagerFactory
import kotlinx.android.synthetic.main.chat_contact_row.view.*
import kotlinx.android.synthetic.main.chat_self_row.view.*


/* TODO: Change tv_self_row to something more useful */
open class ChatItem(val text: String): Item<GroupieViewHolder>() {
    val mUserManager = ManagerFactory.getUserManager()
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tv_self_row.text = text
        mUserManager.loadAvatar(viewHolder.itemView.chat_self_row_iv_avatar)
    }

    override fun getLayout(): Int {
        return R.layout.chat_self_row
    }
}

class ChatFromItem(text:String, private val user: ParseUser): ChatItem(text) {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tv_contact_row.text = text
        mUserManager.loadAvatar(viewHolder.itemView.chat_contact_row_iv_avatar, user)
    }
}

class ChatSelfItem(text: String): ChatItem(text)
