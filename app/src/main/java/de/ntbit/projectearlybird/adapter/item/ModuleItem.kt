package de.ntbit.projectearlybird.adapter.item

import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.model.Module
import kotlinx.android.synthetic.main.square_group_module.view.*

/**
 * A [ModuleItem] for displaying the [Module] in the [GroupActivity]
 *
 * @property name name of the [ModuleItem]
 * @constructor creates an empty [ModuleItem]
 */
class ModuleItem(val module: Module): Item<GroupieViewHolder>() {
    public val name = module.name
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        //viewHolder.itemView.square_group_module_cv.background.setTint(module.colorInt)
        viewHolder.itemView.square_group_module_tv_information.text = "${module.name}\nInfoText der zum Modul gehört"
    }

    override fun getLayout(): Int {
        return R.layout.square_group_module
    }

}