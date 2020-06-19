package de.ntbit.projectearlybird.adapter.item

import android.widget.TextView
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.helper.DateFormatter
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.model.ModuleChecklistItem
import de.ntbit.projectearlybird.model.User
import kotlinx.android.synthetic.main.row_module_checklist.view.*

/**
 * A [ChecklistItem] for displaying the an item in the [ModuleChecklistActivity]
 *
 * @property mUserManager global [UserManager]
 * @property user current [User]
 * @property viewHolder viewholder from [ModuleChecklistActivity]
 * @param item holds an instance of [ModuleChecklistItem]
 * @constructor sets provided [item] to property [item]
 */
class ChecklistItem() : Item<GroupieViewHolder>() {

    private val simpleClassName = this.javaClass.simpleName

    private val mUserManager = ManagerFactory.getUserManager()
    private val moduleChecklistManager = ManagerFactory.getModuleChecklistManager()
    private val user = mUserManager.getCurrentUser()
    private lateinit var viewHolder: GroupieViewHolder

    private lateinit var item: ModuleChecklistItem

    internal constructor(item: ModuleChecklistItem) : this() {
            this.item = item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        //Log.d("CUSTOMDEBUG", "$simpleClassName - bind()")
        this.viewHolder = viewHolder
        //this.viewHolder.itemView.row_module_checklist_tv_name.text = itemName
        this.viewHolder.itemView.row_module_checklist_tv_name.text = item.name
        this.viewHolder.itemView.row_module_checklist_tv_timestamp.text = DateFormatter.formatDate(item)
        this.viewHolder.itemView.row_module_checklist_cb_assigned.isChecked = item.isAssigned

        if(item.isAssigned) {
            this.viewHolder.itemView.row_module_checklist_tv_username.text = item.user!!.username
            this.viewHolder.itemView.row_module_checklist_tv_username.visibility = TextView.VISIBLE
            if(item.user != mUserManager.getCurrentUser())
                viewHolder.itemView.row_module_checklist_cb_assigned.isEnabled = false
        }
        else {
            viewHolder.itemView.row_module_checklist_tv_username.visibility = TextView.GONE
            viewHolder.itemView.row_module_checklist_cb_assigned.isEnabled = true
        }
        setClicklistener()
    }

    override fun getLayout(): Int {
        return R.layout.row_module_checklist
    }
    /**
     * Sets click listener to every [ChecklistItem]
     */
    private fun setClicklistener() {
        this.viewHolder.itemView.row_module_checklist_cb_assigned.setOnClickListener {
            processItemClicked()
        }
    }
    /**
     * Applies the name of the user and a check to the item when it's clicked
     */
    private fun processItemClicked() {
        if (viewHolder.itemView.row_module_checklist_cb_assigned.isChecked) {
            item.assign(user)
            viewHolder.itemView.row_module_checklist_tv_username.text = item.user!!.username
            viewHolder.itemView.row_module_checklist_tv_username.visibility = TextView.VISIBLE
            moduleChecklistManager.saveItemState(item)
        } else {
            if(item.user == user) {
                item.unassign()
                viewHolder.itemView.row_module_checklist_tv_username.visibility = TextView.GONE
                moduleChecklistManager.saveItemState(item)
            }
        }
    }

    fun getModuleChecklistItem(): ModuleChecklistItem {
        return item
    }

    override fun toString(): String {
        return item.toString()
    }
}