package de.ntbit.projectearlybird.adapter

import android.graphics.Color
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.manager.UserManager
import de.ntbit.projectearlybird.model.Message
import de.ntbit.projectearlybird.model.User
import kotlinx.android.synthetic.main.row_new_message_user.view.*
import kotlinx.android.synthetic.main.row_latest_message.view.*
import java.text.SimpleDateFormat
import java.util.*

open class UserItem(val user: User): Item<GroupieViewHolder>(){
    protected val mUserManager: UserManager = ManagerFactory.getUserManager()

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_new_message.text = user.username
        /*Bilder zu den usernames*/
        mUserManager.loadAvatar(viewHolder.itemView.imageView_new_message, user)
    }

    override fun getLayout(): Int {
        return R.layout.row_new_message_user
    }
}

class UserItemLatestMessage(user: User) : UserItem(user) {
    private val mMessageManager = ManagerFactory.getMessageManager()

    /* TODO: CHANGE TO getCurrentLocale */
    private val userLocale = Locale("de")

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val latestMessage: Message = mMessageManager.getLatestMessage(user)

        Log.d("CUSTOMDEBUG", "UserItem - User: ${user.username} at position: $position viewHolder: $viewHolder")

        mUserManager.loadAvatar(viewHolder.itemView.latest_message_row_iv_avatar, user)
        viewHolder.itemView.latest_message_row_tv_username.text = user.username
        viewHolder.itemView.latest_message_row_tv_message.text =
            if(latestMessage.body.length > 73)
                latestMessage.body.subSequence(0,70).toString() + "..."
            else latestMessage.body
        viewHolder.itemView.latest_message_row_tv_timestamp.text = getDateFormat(latestMessage)
            .format(latestMessage.timestamp)
    }

    override fun getLayout(): Int {
        return R.layout.row_latest_message
    }

    private fun getDateFormat(latestMessage: Message): SimpleDateFormat {
        if(System.currentTimeMillis() - latestMessage.timestamp.time > 3600000)
            return SimpleDateFormat("dd.MM.yyyy",userLocale)
        return SimpleDateFormat("HH:mm",userLocale)
    }
}
