package de.ntbit.projectearlybird.adapter

import android.util.Log
import com.parse.ParseUser
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.manager.UserManager
import de.ntbit.projectearlybird.model.Message
import kotlinx.android.synthetic.main.row_new_message_user.view.*
import kotlinx.android.synthetic.main.row_latest_message.view.*
import java.text.SimpleDateFormat
import java.util.Locale

open class UserItem(val user: ParseUser): Item<GroupieViewHolder>(){
    protected val mUserManager: UserManager = ManagerFactory.getUserManager()

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_new_message.text = user.username
        /*Bilder zu den usernames*/
        mUserManager.loadAvatar(viewHolder.itemView.imageView_new_message, user)
        /*
        viewHolder.itemView.setOnClickListener{
            Log.d("CUSTOMDEBUG","Clicked $position for $user.username")
        }
         */
    }

    override fun getLayout(): Int {
        return R.layout.row_new_message_user
    }
}

class UserItemLatestMessage(user: ParseUser) : UserItem(user) {
    /* TODO: CHANGE TO getCurrentLocale */
    private val userLocale = Locale("de")
    private val datePattern = "HH:mm"
    val format = SimpleDateFormat(datePattern, userLocale)
    private val mMessageManager = ManagerFactory.getMessageManager()
    private val latestMessage: Message = mMessageManager.getLatestMessage(user)

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        mUserManager.loadAvatar(viewHolder.itemView.latest_message_row_iv_avatar, user)
        viewHolder.itemView.latest_message_row_tv_username.text = user.username
        viewHolder.itemView.latest_message_row_tv_message.text = if(latestMessage.body.length > 73) latestMessage.body.subSequence(0,70).toString() + "..." else latestMessage.body
        viewHolder.itemView.latest_message_row_tv_timestamp.text = format.format(latestMessage.timestamp)
    }

    override fun getLayout(): Int {
        return R.layout.row_latest_message
    }
}
