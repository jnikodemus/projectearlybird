package de.ntbit.projectearlybird.model

import android.util.Log
import com.parse.ParseClassName
import com.parse.ParseObject
import de.ntbit.projectearlybird.adapter.item.ChecklistItem

/**
 * Model corresponding to table "ModuleChecklist" in Parse Database extends [ParseObject]
 *
 * @property items contains the items created in the checklist ui
 */
@ParseClassName("ModuleChecklist")
class ModuleChecklist: Module {

    internal constructor() : super()

    internal constructor(itemList: ArrayList<ChecklistItem>, group: Group) {
        this.name = "Checklist"
        this.description = "A module to manage a checklist"
        this.colorInt = -65281
        this.associatedGroup = group
        this.items = itemList
        Log.d("CUSTOMDEBUG", "ModuleChecklist - $this")
    }

    internal constructor(other: ModuleChecklist) {
        this.name = other.name
        this.description = other.description
        this.colorInt = other.colorInt
        this.items = ArrayList()
        for(i in other.items)
            this.items.add(i)
    }

    var associatedGroup: Group
        get() {
            return get("associatedGroup") as Group
        }
        set(value) {
            put("associatedGroup", value)
        }

    var items: ArrayList<ChecklistItem>
        get() {
            val itemList = ArrayList<ChecklistItem>()
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

    override fun toString(): String {
        val stringifiedModule = "$name, $description, colorInt: $colorInt, " +
                "${associatedGroup.name}, items: ${items.size}"
        return stringifiedModule
    }
}