package de.ntbit.projectearlybird.adapter.item

import android.util.Log
import android.widget.CheckBox
import android.widget.TextView
import com.parse.ParseObject
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.model.User
import kotlinx.android.synthetic.main.row_module_checklist.view.*
import org.json.JSONObject

/**
 * A [ModuleChecklistItem] for displaying the an item in the [ModuleChecklistActivity]
 *
 * @property mUserManager global [UserManager]
 * @property user current [User]
 * @property viewHolder viewholder from [ModuleChecklistActivity] activity
 * @property itemName name of the row in the checklist
 * @property isAssigned ???
 * @property assignedUser ???
 * @constructor ???
 */
class ModuleChecklistItem() : Item<GroupieViewHolder>() {

    companion object {
        fun convertModuleChecklistItemToJsonObject(module: ModuleChecklistItem): JSONObject {
            val json: JSONObject = JSONObject()
            json.put("itemName", module.itemName)
            json.put("isAssigned", module.isAssigned)
            if(module.isAssigned)
                json.put("assignedUser", module.assignedUser!!)
            return json
        }
    }

    private val mUserManager = ManagerFactory.getUserManager()
    private val user = mUserManager.getCurrentUser()
    private lateinit var viewHolder: GroupieViewHolder

    private var itemName: String = ""
    private var isAssigned: Boolean = false
    private var assignedUser: User? = null

    internal constructor(itemName: String) : this() {
        this.itemName = itemName
    }

    internal constructor(jsonObject: JSONObject) : this() {
        this.itemName = jsonObject.getString("itemName")
        this.isAssigned = jsonObject.getBoolean("isAssigned")
        if(isAssigned)
            this.assignedUser = jsonObject.get("assignedUser") as User
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        this.viewHolder = viewHolder
        this.viewHolder.itemView.row_module_checklist_tv_name.text = itemName
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
        if(viewHolder.itemView.row_module_checklist_cb_checked.isChecked) {
            viewHolder.itemView.row_module_checklist_tv_username.text = user.username
            viewHolder.itemView.row_module_checklist_tv_username.visibility = TextView.VISIBLE
        }

        else
            viewHolder.itemView.row_module_checklist_tv_username.visibility = TextView.INVISIBLE
    }
}