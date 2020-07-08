package de.ntbit.projectearlybird.manager

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.parse.ParseQuery
import com.parse.livequery.ParseLiveQueryClient
import com.parse.livequery.SubscriptionHandling
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.adapter.item.ChecklistItem
import de.ntbit.projectearlybird.connection.ParseConnection
import de.ntbit.projectearlybird.model.Group
import de.ntbit.projectearlybird.model.ModuleChecklist
import de.ntbit.projectearlybird.model.ModuleChecklistItem
import java.net.URI

/**
 * TODO: Comment
 */
class ModuleChecklistManager {

    private val simpleClassName = this.javaClass.simpleName

    private val mGroupManager = ManagerFactory.getGroupManager()
    private val mUserManager = ManagerFactory.getUserManager()


//    private val checklistItemMap = HashMap<Group, ArrayList<ModuleChecklistItem>>()
    private val adapterMap = HashMap<Group, GroupAdapter<GroupieViewHolder>>()

    // TODO: Implement isInitialized logic to initialize/fetch only groups that are not in the maps yet!
    private var isInitialized = false
    //private lateinit var adapter: GroupAdapter<GroupieViewHolder>
    //private val checklist = ArrayList<ModuleChecklistItem>()

    init {
        Log.d("CUSTOMDEBUG","$simpleClassName - init()")
        //getAllChecklists()
        //getChecklistItemsFromParse()
        //listenForNewChecklistItem()
    }

    private fun getAllChecklists(group: Group) {
        Log.d("CUSTOMDEBUG", "$simpleClassName.getAllChecklists() - adapterMap: ${adapterMap.size}")
//        Log.d("CUSTOMDEBUG", "$simpleClassName.getAllChecklists() - checklistItemMap: ${checklistItemMap.size}")
        adapterMap[group] = GroupAdapter()
//        checklistItemMap[group] = ArrayList()
        //Log.d("CUSTOMDEBUG", "$simpleClassName.getAllChecklists() - adapterMapContains ${group.name}: ${adapterMap.containsKey(group)}")
        //Log.d("CUSTOMDEBUG", "$simpleClassName.getAllChecklists() - checklistItemMapContains ${group.name}: ${checklistItemMap.contains(group)}")
        getChecklistItemsFromParse(group)
        listenForNewChecklistItem()
        listenForUpdateChecklistItem()
        listenForDeleteChecklistItem()
    }

    private fun getAllChecklists() {
        val groups = mGroupManager.getGroups()
        Log.d("CUSTOMDEBUG","$simpleClassName - got ${groups.size} groups")
        for(group in groups) {
            Log.d("CUSTOMDEBUG","$simpleClassName - found ${group.name}")
            adapterMap[group] = GroupAdapter()
//            checklistItemMap[group] = ArrayList()
        }
        Log.d("CUSTOMDEBUG","$simpleClassName - adapterMap: ${adapterMap.size}")
//      Log.d("CUSTOMDEBUG","$simpleClassName - checklistItemMap: ${checklistItemMap.size}")
        getChecklistItemsFromParse()
    }

    private fun getChecklistItemsFromParse(group: Group) {
        val moduleChecklistItemQuery = ParseConnection.getModuleChecklistItemQuery()
        val checklist = group.getModuleByName("Checklist")
        if(checklist != null) {
            checklist as ModuleChecklist
            ParseConnection.getModuleChecklistItemQuery()
            moduleChecklistItemQuery?.whereEqualTo("associatedModule", checklist)
            moduleChecklistItemQuery?.findInBackground { items, _ ->
                // Add to checklistItemMap
//                checklistItemMap[group]?.addAll(items)
                Log.d("CUSTOMDEBUG", "$simpleClassName - added ${items.size}")
                // Add to adapterMap
                /*
                TODO: Remove Workaround
                Workaround for mulitple new Items if User navigates back and forth at checklistModule
                 */
                adapterMap[group]?.clear()
                for (item in items) {
                    adapterMap[group]?.add(ChecklistItem(item))
                    Log.d("CUSTOMDEBUG", "$simpleClassName - added $item")
                }
            }
        }
        /*
        listenForNewChecklistItem()
        listenForUpdateChecklistItem()
        listenForDeleteChecklistItem()
         */
    }

    /*
     * groupAdapter: all indizes in adapterMap
     * key: group
     * value: GroupAdapter<GroupieViewHolder>
     */
    @Deprecated("Use getChecklistItemsFromParse(Group)")
    val moduleChecklistItemQuery = ParseConnection.getModuleChecklistItemQuery()
    private fun getChecklistItemsFromParse() {
        for(groupAdapter in adapterMap) {
            val checklist = groupAdapter.key.getModuleByName("Checklist")
            if(checklist != null) {
                checklist as ModuleChecklist
                moduleChecklistItemQuery?.whereEqualTo("associatedModule", checklist)
                moduleChecklistItemQuery?.findInBackground { items, _ ->
                    // Add to checklistItemMap
//                    checklistItemMap[groupAdapter.key]?.addAll(items)
                    Log.d("CUSTOMDEBUG", "$simpleClassName - added ${items.size}")
                    // Add to adapterMap
                    for (item in items) {
                        groupAdapter.value.add(ChecklistItem(item))
                        Log.d("CUSTOMDEBUG", "$simpleClassName - added ${item.name}")
                    }
                }
            }
        }

        //listenForNewChecklistItem()
        listenForUpdateChecklistItem()
    }

    private fun listenForNewChecklistItem() {
        //Log.d("CUSTOMDEBUG", "ModuleChecklistManager - listenForNewChecklistItem()")
        val newItemHandling = ParseConnection.getModuleChecklistItemNewHandling()
        newItemHandling?.handleEvent(SubscriptionHandling.Event.CREATE) { _, item ->
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                Log.d(
                    "CUSTOMDEBUG", "$simpleClassName - " +
                            "CurrentUser: ${mUserManager.getCurrentUser().objectId}, " +
                            "got a new item:\n$item"
                )
                //processNewChecklistItem(item)
                getChecklistItemsFromParse(item.associatedModule.associatedGroup)
            }
        }
    }

    private fun listenForUpdateChecklistItem() {
        val updateItemHandling = ParseConnection.getModuleChecklistItemUpdateHandling()
        updateItemHandling?.handleEvent(SubscriptionHandling.Event.UPDATE) {_, item ->
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                Log.d("CUSTOMDEBUG", "$simpleClassName - " +
                        "CurrentUser: ${mUserManager.getCurrentUser().objectId}, " +
                        "got update on item:\n$item")
                processUpdateOnChecklistItem(item)
            }
        }
    }

    private fun listenForDeleteChecklistItem() {
        val deleteItemHandling = ParseConnection.getModuleChecklistItemDeleteHandling()
        deleteItemHandling?.handleEvent(SubscriptionHandling.Event.DELETE) {_, item ->
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                deleteChecklistItem(ChecklistItem(item), false)
            }
        }
    }

    private fun processUpdateOnChecklistItem(item: ModuleChecklistItem) {
        val group = item.associatedModule.associatedGroup
        adapterMap[group]?.notifyDataSetChanged()
    }

    private fun processNewChecklistItem(item: ModuleChecklistItem) {
        val group = item.associatedModule.associatedGroup
        adapterMap[group]!!.add(ChecklistItem(item))
        adapterMap[group]!!.notifyDataSetChanged()
//        checklistItemMap[group]!!.add(item)
    }

    fun addItem(item: ModuleChecklistItem) {
        //processNewChecklistItem(item)
        saveItemState(item)
    }

    fun saveItemState(item: ModuleChecklistItem) {
        item.saveEventually()
    }

    fun deleteChecklistItem(checklistItem: ChecklistItem, deleteFromDatabase: Boolean) {
        val item = checklistItem.getModuleChecklistItem()
        val group = item.associatedModule.associatedGroup
        if(deleteFromDatabase) {
            adapterMap[group]?.remove(checklistItem)
            deleteItemOnDatabase(item)
        }
        else {
            adapterMap[group]?.notifyDataSetChanged()
            Log.d("CUSTOMDEBUG", "${this.simpleClassName} deleted: $item")
            /*
            val indexOfDeletedItem = adapterMap[group]?.getAdapterPosition(checklistItem)
            Log.d("CUSTOMDEBUG", "${this.simpleClassName} deleted: $item; indexOfDeletedItem: $indexOfDeletedItem")
            if (indexOfDeletedItem != null) {
                if (indexOfDeletedItem >= 0) {
                    adapterMap[group]?.remove(checklistItem)
                }
                adapterMap[group]?.notifyDataSetChanged()
            }

             */
        }
    }

    private fun deleteItemOnDatabase(item: ModuleChecklistItem) {
        item.deleteEventually()
    }

    // TODO: implement isInitialized again
    fun getAdapterByGroup(group: Group): GroupAdapter<GroupieViewHolder>? {
        //adapterMap[group]?.clear()
        //if(!isInitialized) {
        Log.d("CUSTOMDEBUG", "$simpleClassName.getAdapaterByGroup() - " +
                "${group.name}(${group.objectId})")
        getAllChecklists(group)
            //isInitialized = true
        //}
        Log.d("CUSTOMDEBUG", "$simpleClassName.getAdapaterByGroup() - " +
                "returning ${adapterMap[group]?.itemCount} now")
        return adapterMap[group]
    }
}