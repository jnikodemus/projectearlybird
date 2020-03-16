package de.ntbit.projectearlybird.adapter

import android.widget.ImageView
import android.widget.TextView
import com.parse.ParseUser
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.manager.UserManager
import kotlinx.android.synthetic.main.new_message_user_row.view.*

class UserItem(val user: ParseUser): Item<GroupieViewHolder>(){
    private val mUserManager: UserManager = ManagerFactory.getUserManager()
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_new_message.text = user.username
        /*Bilder zu den usernames*/
        mUserManager.loadAvatar(viewHolder.itemView.imageView_new_message, user)
    }

    override fun getLayout(): Int {
        return R.layout.new_message_user_row
    }
}