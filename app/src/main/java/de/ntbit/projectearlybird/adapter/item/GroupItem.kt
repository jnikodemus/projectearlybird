package de.ntbit.projectearlybird.adapter.item


import android.util.Log
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.model.Group
import kotlinx.android.synthetic.main.row_group.view.*

class GroupItem(val group: Group) : Item<GroupieViewHolder>(){
    val mUserManager = ManagerFactory.getUserManager()
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        if(group.croppedImage != null)
            Picasso.get().load(group.croppedImage?.url).into(viewHolder.itemView.frgmt_groups_iv_image)
        else
            Picasso.get().load(group.groupImage.url).into(viewHolder.itemView.frgmt_groups_iv_image)
        viewHolder.itemView.frgmt_groups_tv_name.text = group.name
        
    }

    override fun getLayout(): Int {
        return R.layout.row_group
    }

}