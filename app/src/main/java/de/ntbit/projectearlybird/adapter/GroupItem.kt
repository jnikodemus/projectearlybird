package de.ntbit.projectearlybird.adapter

import com.parse.ParseObject
import com.parse.ParseUser
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.manager.ManagerFactory
import kotlinx.android.synthetic.main.row_crt_group_contact.view.*

open class GroupItem(val user: ParseUser) : Item<GroupieViewHolder>(){
    val mUserManager = ManagerFactory.getUserManager()
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.crt_group_row_tv_username.text = user.username
        mUserManager.loadAvatar(viewHolder.itemView.crt_group_row_cb_avatar, user)
    }

    override fun getLayout(): Int {
        return R.layout.row_crt_group_contact
    }


}