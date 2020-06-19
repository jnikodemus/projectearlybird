package de.ntbit.projectearlybird.adapter.item

import android.util.Log
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.helper.DateFormatter
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.manager.UserManager
import de.ntbit.projectearlybird.model.Message
import de.ntbit.projectearlybird.model.User
import kotlinx.android.synthetic.main.row_new_message_user.view.*
import kotlinx.android.synthetic.main.row_latest_message.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * A [UserItem] for displaying users/contacts in different activities
 *
 * @param user contains the information for this item
 * @property mUserManager is the global [UserManager]
 */
open class UserItem(val user: User): Item<GroupieViewHolder>(){
    protected val mUserManager: UserManager = ManagerFactory.getUserManager()

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.row_new_message_user_tv_new_message.text = user.username
        viewHolder.itemView.row_new_message_user_tv_about.text = user.aboutMe
        /*Bilder zu den usernames*/
        mUserManager.loadAvatar(viewHolder.itemView.row_new_message_user_iv_new_message, user)
    }

    override fun getLayout(): Int {
        return R.layout.row_new_message_user
    }
}

class UserItemLatestMessage(user: User) : UserItem(user) {
    private val mMessageManager = ManagerFactory.getMessageManager()

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        Log.d("CUSTOMDEBUG", "UserItemLatesMessage - trying to get latestMessage for ${user.username}")
        val latestMessage: Message = mMessageManager.getLatestMessage(user)

        mUserManager.loadAvatar(viewHolder.itemView.latest_message_row_iv_avatar, user)
        viewHolder.itemView.latest_message_row_tv_username.text = user.username
        viewHolder.itemView.latest_message_row_tv_message.text =
            if(latestMessage.body.length > 73)
                latestMessage.body.subSequence(0,70).toString() + "..."
            else latestMessage.body
        viewHolder.itemView.latest_message_row_tv_timestamp.text = DateFormatter
            .formatDate(latestMessage)
    }

    override fun getLayout(): Int {
        return R.layout.row_latest_message
    }
}
