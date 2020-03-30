package de.ntbit.projectearlybird.adapter

import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.model.Module
import kotlinx.android.synthetic.main.square_group_module.view.*

class ModuleItem(val module: Module): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        //viewHolder.itemView.square_group_module_cv.background.setTint(module.colorInt)
        viewHolder.itemView.square_group_module_tv_information.text = "${module.name}\nInfoText der zum Modul gehört"
    }

    override fun getLayout(): Int {
        return R.layout.square_group_module
    }

}