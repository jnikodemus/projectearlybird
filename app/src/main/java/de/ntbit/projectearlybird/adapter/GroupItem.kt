package de.ntbit.projectearlybird.adapter


import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.model.Group
import kotlinx.android.synthetic.main.row_group.view.*

class GroupItem(private val group: Group) : Item<GroupieViewHolder>(){
    val mUserManager = ManagerFactory.getUserManager()
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        Picasso.get().load(group.croppedImage?.url).into(viewHolder.itemView.row_group_banner)
    }

    override fun getLayout(): Int {
        return R.layout.row_group
    }


}