package de.ntbit.projectearlybird.adapter

import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.ntbit.projectearlybird.R

open class GroupItem : Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    }

    override fun getLayout(): Int {
        return R.layout.row_crt_group_contact
    }
}