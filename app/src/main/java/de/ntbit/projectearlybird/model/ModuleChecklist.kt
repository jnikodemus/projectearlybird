package de.ntbit.projectearlybird.model

import android.widget.Adapter
import com.parse.ParseClassName
import com.parse.ParseObject
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.adapter.item.ModuleChecklistItem
import org.json.JSONObject

/**
 * Model corresponding to table "ModuleChecklist" in Parse Database extends [ParseObject]
 *
 * @property items contains the items created in the checklsit ui
 */
@ParseClassName("ModuleChecklist")
class ModuleChecklist: Module {

    internal constructor() : super()

    internal constructor(itemList: ArrayList<ModuleChecklistItem>) {
        this.name = "Checklist"
        this.description = "A module to manage a checklist"
        this.colorInt = -65281
        this.items = itemList
    }

    internal constructor(other: ModuleChecklist) {
        this.name = other.name
        this.description = other.description
        this.colorInt = other.colorInt
        this.items = ArrayList<ModuleChecklistItem>()
        for(i in other.items)
            this.items.add(i)
    }

    var items: ArrayList<ModuleChecklistItem>
        get() {
            val itemList = ArrayList<ModuleChecklistItem>()
            //for(json in this.getList<JSONObject>("items")!!)
            //    itemList.add(ModuleChecklistItem(json))
            return itemList
        }
        set(items) {
            //val jsonList = ArrayList<JSONObject>()
            val stringArr = ArrayList<String>()
            stringArr.add("Test")
            //for(item in items) {
            //    jsonList.add(ModuleChecklistItem.convertModuleChecklistItemToJsonObject(item))
            //}
            this.put("items", stringArr)
        }
}