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

    /**
     * Creates a new [GroupAdatper] and maps it to the local [adapterMap][group].
     * Afterwards calls [getChecklistItemsFromParse](group).
     */
    private fun getAllChecklists(group: Group) {
        Log.d("CUSTOMDEBUG", "$simpleClassName.getAllChecklists() - adapterMap: ${adapterMap.size}")
//        Log.d("CUSTOMDEBUG", "$simpleClassName.getAllChecklists() - checklistItemMap: ${checklistItemMap.size}")
        adapterMap[group] = GroupAdapter()
//        checklistItemMap[group] = ArrayList()
        //Log.d("CUSTOMDEBUG", "$simpleClassName.getAllChecklists() - adapterMapContains ${group.name}: ${adapterMap.containsKey(group)}")
        //Log.d("CUSTOMDEBUG", "$simpleClassName.getAllChecklists() - checklistItemMapContains ${group.name}: ${checklistItemMap.contains(group)}")
        getChecklistItemsFromParse(group)
    }

    /**
     * Reads all [ChecklistItem]s from Parse and adds them to the Adapter
     * of the local [adapterMap][group].
     */
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
                adapterMap[group]?.clear()
                for (item in items) {
                    adapterMap[group]?.add(ChecklistItem(item))
                    Log.d("CUSTOMDEBUG", "$simpleClassName - added $item")
                }
            }
        }
        //listenForNewChecklistItem()
        //listenForUpdateChecklistItem()
        //listenForDeleteChecklistItem()
    }

    //fun getChecklist() : Collection<ModuleChecklistItem>{ return checklist }

    /**
     * Creates a [SubscriptionHanding] and subscribes to the [ParseQuery]
     * of ModuleChecklistItem::class.java which will listen
     * for events of[SubscriptionHandling.Event.CREATE].
     * Everytime a new item comes in, the method [getChecklistItemsFromParse] will be called.
     */
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
                //processNewChecklistItem(item)
                getChecklistItemsFromParse(item.associatedModule.associatedGroup)
            }
        }
    }

    /**
     * Creates a [SubscriptionHanding] and subscribes to the [ParseQuery]
     * of ModuleChecklistItem::class.java which will listen
     * for events of[SubscriptionHandling.Event.UPDATE].
     * Everytime an item update comes in, the method [processUpdateOnChecklistItem] will be called.
     */
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

    /**
     * Creates a [SubscriptionHanding] and subscribes to the [ParseQuery]
     * of ModuleChecklistItem::class.java which will listen
     * for events of[SubscriptionHandling.Event.DELETE].
     * Everytime an item is deleted, the method [deleteChecklistItem] will be called.
     */
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

    /**
     * Reloads the adapter of the associated group of [item] by calling [notifyDataSetChanged].
     */
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

    /**
     * Calls [saveItemState] and adds the [item] to the adapter.
     */
    fun addItem(item: ModuleChecklistItem) {
        //processNewChecklistItem(item)
        saveItemState(item)
        adapterMap[item.associatedModule.associatedGroup]?.add(ChecklistItem(item))
    }

    /**
     * Calls [ParseObject.saveEventually].
     */
    fun saveItemState(item: ModuleChecklistItem) {
        item.saveEventually()
    }

    /**
     * Removes provided [checklistItem] from adapter and tries to delete
     * it from database if [deleteFromDatabase].
     */
    fun deleteChecklistItem(checklistItem: ChecklistItem, deleteFromDatabase: Boolean) {
        val item = checklistItem.getModuleChecklistItem()
        val group = item.associatedModule.associatedGroup
        if(deleteFromDatabase) {
            adapterMap[group]?.remove(checklistItem)
            deleteItemOnDatabase(item)
        }
        else {
            val indexOfDeletedItem = adapterMap[group]?.getAdapterPosition(checklistItem)
            if (indexOfDeletedItem != null) {
                if (indexOfDeletedItem >= 0) {
                    adapterMap[group]?.remove(checklistItem)
                }
            }
        }
    }

    /**
     * Calls [item].[deleteEventually].
     */
    private fun deleteItemOnDatabase(item: ModuleChecklistItem) {
        item.deleteEventually()
    }

    // TODO: implement isInitialized again
    /**
     * Builds, initializes, subscribes to events
     * and returns the [GroupAdapter]<[GroupieViewHolder]> of [group].
     */
    fun getAdapterByGroup(group: Group): GroupAdapter<GroupieViewHolder>? {
        //adapterMap[group]?.clear()
        //if(!isInitialized) {
        Log.d("CUSTOMDEBUG", "$simpleClassName.getAdapaterByGroup() - ${group.name}(${group.objectId})")
        getAllChecklists(group)
        listenForNewChecklistItem()
        listenForUpdateChecklistItem()
        listenForDeleteChecklistItem()
        //isInitialized = true
        //}
        Log.d("CUSTOMDEBUG", "$simpleClassName.getAdapaterByGroup() - returning ${adapterMap[group]?.itemCount} now")
        return adapterMap[group]
    }
}