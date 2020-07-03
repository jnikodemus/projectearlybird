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

    private val parseLiveQueryClient: ParseLiveQueryClient =
        ParseLiveQueryClient.Factory.getClient(URI("wss://projectearlybird.back4app.io/"))

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
        val query = ParseQuery.getQuery(ModuleChecklistItem::class.java)
        val checklist = group.getModuleByName("Checklist")
        if(checklist != null) {
            checklist as ModuleChecklist
            query.whereEqualTo("associatedModule", checklist)
            query.findInBackground { items, _ ->
                // Add to checklistItemMap
//                checklistItemMap[group]?.addAll(items)
                Log.d("CUSTOMDEBUG", "$simpleClassName - added ${items.size}")
                // Add to adapterMap
                for (item in items) {
                    adapterMap[group]?.add(ChecklistItem(item))
                    Log.d("CUSTOMDEBUG", "$simpleClassName - added $item")
                }
            }
        }
        listenForNewChecklistItem()
        listenForUpdateChecklistItem()
        listenForDeleteChecklistItem()
    }

    /*
     * groupAdapter: all indizes in adapterMap
     * key: group
     * value: GroupAdapter<GroupieViewHolder>
     */
    @Deprecated("Use getChecklistItemsFromParse(Group)")
    private fun getChecklistItemsFromParse() {
        val query = ParseQuery.getQuery(ModuleChecklistItem::class.java)
        for(groupAdapter in adapterMap) {
            val checklist = groupAdapter.key.getModuleByName("Checklist")
            if(checklist != null) {
                checklist as ModuleChecklist
                query.whereEqualTo("associatedModule", checklist)
                query.findInBackground { items, _ ->
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

        listenForNewChecklistItem()
        listenForUpdateChecklistItem()
    }

    //fun getChecklist() : Collection<ModuleChecklistItem>{ return checklist }

    private fun listenForNewChecklistItem() {
        val parseQuery = ParseQuery.getQuery(ModuleChecklistItem::class.java)
        //parseQuery.whereNotEqualTo("creatorId", mUserManager.getCurrentUser().objectId)
        val subscriptionHandling: SubscriptionHandling<ModuleChecklistItem> =
            parseLiveQueryClient.subscribe(parseQuery)

        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE) { _, item ->
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                Log.d(
                    "CUSTOMDEBUG", "$simpleClassName - " +
                            "CurrentUser: ${mUserManager.getCurrentUser().objectId}, " +
                            "got a new item:\n$item"
                )
                processNewChecklistItem(item)
            }
        }
    }

    private fun listenForUpdateChecklistItem() {
        val parseQuery = ParseQuery.getQuery(ModuleChecklistItem::class.java)
        val subscriptionHandling: SubscriptionHandling<ModuleChecklistItem> =
            parseLiveQueryClient.subscribe(parseQuery)
        subscriptionHandling.handleEvent(SubscriptionHandling.Event.UPDATE) {_, item ->
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
        val parseQuery = ParseQuery.getQuery(ModuleChecklistItem::class.java)
        val subscriptionHandling: SubscriptionHandling<ModuleChecklistItem> =
            parseLiveQueryClient.subscribe(parseQuery)
        subscriptionHandling.handleEvent(SubscriptionHandling.Event.DELETE) {_, item ->
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
        /*
        val position = adapterMap[group]?.getAdapterPosition(checklistItem)
        // TODO: check why Codes crashes on next line
        adapterMap[group]?.remove(checklistItem)
        if (position != null) {
            //adapterMap[group]?.notifyItemRangeChanged(position, adapterMap[group]!!.itemCount -1)
            adapterMap[group]?.notifyDataSetChanged()
        }
        //adapterMap[group]!!.notifyItemRemoved(position)
        checklistItemMap[group]?.remove(item)
         */
        if(deleteFromDatabase) {
            adapterMap[group]?.remove(checklistItem)
            deleteItemOnDatabase(item)
        }
        else {
            adapterMap[group]?.notifyDataSetChanged()
        }
    }

    private fun deleteItemOnDatabase(item: ModuleChecklistItem) {
        item.deleteEventually()
    }

    // TODO: implement isInitialized again
    fun getAdapterByGroup(group: Group): GroupAdapter<GroupieViewHolder>? {
        //adapterMap[group]?.clear()
        //if(!isInitialized) {
        Log.d("CUSTOMDEBUG", "$simpleClassName.getAdapaterByGroup() - ${group.name}(${group.objectId})")
        getAllChecklists(group)
            //isInitialized = true
        //}
        Log.d("CUSTOMDEBUG", "$simpleClassName.getAdapaterByGroup() - returning ${adapterMap[group]?.itemCount} now")
        return adapterMap[group]
    }
}