package de.ntbit.projectearlybird.adapter.item

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.parse.ParseObject
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.model.User
import kotlinx.android.synthetic.main.row_module_checklist.view.*
import kotlinx.android.synthetic.main.square_group_module.view.*
import org.json.JSONObject

/**
 * A [ModuleChecklistItem] for displaying the an item in the [ModuleChecklistActivity]
 *
 * @property mUserManager global [UserManager]
 * @property user current [User]
 * @property viewHolder viewholder from [ModuleChecklistActivity] activity
 * @param item holds an instance of [de.ntbit.projectearlybird.model.ModuleChecklistItem]
 * @constructor sets provided [item] to property [item]
 */
class ModuleChecklistItem() : Item<GroupieViewHolder>() {

    private val mUserManager = ManagerFactory.getUserManager()
    private val user = mUserManager.getCurrentUser()
    private lateinit var viewHolder: GroupieViewHolder

    private lateinit var item: de.ntbit.projectearlybird.model.ModuleChecklistItem

    internal constructor(item: de.ntbit.projectearlybird.model.ModuleChecklistItem) : this() {
            this.item = item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        this.viewHolder = viewHolder
        //this.viewHolder.itemView.row_module_checklist_tv_name.text = itemName
        this.viewHolder.itemView.row_module_checklist_tv_name.text = item.name
        this.viewHolder.itemView.row_module_checklist_tv_timestamp.text = item.timestamp.toString()
        this.viewHolder.itemView.row_module_checklist_cb_checked.isChecked = item.isAssigned
        if(item.isAssigned)
            this.viewHolder.itemView.row_module_checklist_tv_username.text = item.user!!.username
        setClicklistener()
    }

    override fun getLayout(): Int {
        return R.layout.row_module_checklist
    }
    /**
     * Sets click listener to every [ModuleChecklistItem]
     */
    private fun setClicklistener() {
        this.viewHolder.itemView.row_module_checklist_cb_checked.setOnClickListener {
            processItemClicked()
        }
    }
    /**
     * Applies the name of the user and a check to the item when it's clicked
     */
    private fun processItemClicked() {
        if (viewHolder.itemView.row_module_checklist_cb_checked.isChecked) {
            item.assign(user)
            viewHolder.itemView.row_module_checklist_tv_username.text = item.user!!.username
            viewHolder.itemView.row_module_checklist_tv_username.visibility = TextView.VISIBLE
        } else {
            if(item.user == user) {
                item.unassign()
                viewHolder.itemView.row_module_checklist_tv_username.visibility = TextView.INVISIBLE
            }
        }
    }

    override fun toString(): String {
        return item.toString()
    }
}