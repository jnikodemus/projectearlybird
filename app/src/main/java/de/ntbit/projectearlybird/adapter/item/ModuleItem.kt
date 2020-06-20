package de.ntbit.projectearlybird.adapter.item

import android.graphics.Color
import android.util.Log
import androidx.core.graphics.toColor
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
    val name = module.name
    val description = module.description
    val color = module.colorInt

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        //viewHolder.itemView.square_group_module_cv.background.setTint(module.colorInt)
        //viewHolder.itemView.square_group_module_cv.setCardBackgroundColor(color)
        viewHolder.itemView.square_group_module_tv_information_title.text = name
        //viewHolder.itemView.square_group_module_tv_information_body.text = description
    }

    override fun getLayout(): Int {
        return R.layout.square_group_module
    }

}