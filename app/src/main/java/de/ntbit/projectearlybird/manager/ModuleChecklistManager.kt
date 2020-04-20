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

    private val checklistItemMap = HashMap<Group, ArrayList<ModuleChecklistItem>>()
    private val adapterMap = HashMap<Group, GroupAdapter<GroupieViewHolder>>()

    private var isInitialized = false
    //private lateinit var adapter: GroupAdapter<GroupieViewHolder>
    //private val checklist = ArrayList<ModuleChecklistItem>()

    init {
        Log.d("CUSTOMDEBUG","$simpleClassName - init()")
        //getAllChecklists()
        //getChecklistItemsFromParse()
        //listenForNewChecklistItem()
    }

    private fun getAllChecklists() {
        val groups = mGroupManager.getGroups()
        Log.d("CUSTOMDEBUG","$simpleClassName - got ${groups.size} groups")
        for(group in groups) {
            Log.d("CUSTOMDEBUG","$simpleClassName - found ${group.name}")
            adapterMap[group] = GroupAdapter()
            checklistItemMap[group] = ArrayList()
        }
        getChecklistItemsFromParse()
    }

    private fun getChecklistItemsFromParse() {
        val query = ParseQuery.getQuery(ModuleChecklistItem::class.java)
        for(pair in adapterMap) {
            val checklist = pair.key.getModuleByName("Checklist") as ModuleChecklist
            query.whereEqualTo("associatedModule", checklist)
            query.findInBackground { items, e ->
                // Add to checklistItemMap
                checklistItemMap[pair.key]!!.addAll(items)
                // Add to adapterMap
                for(item in items)
                    pair.value.add(ChecklistItem(item))
            }
        }
        listenForNewChecklistItem()
        listenForUpdateChecklistItem()
    }

    //fun getChecklist() : Collection<ModuleChecklistItem>{ return checklist }

    private fun listenForNewChecklistItem() {
        Log.d("CUSTOMDEBUG", "$simpleClassName - listening for new ChecklistItems")
        val parseQuery = ParseQuery.getQuery(ModuleChecklistItem::class.java)
        parseQuery.whereNotEqualTo("creatorId", mUserManager.getCurrentUser().objectId)
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

    /*
    private fun listenForDeleteChecklistItem() {
        val parseQuery = ParseQuery.getQuery(ModuleChecklistItem::class.java)
        val subscriptionHandling: SubscriptionHandling<ModuleChecklistItem> =
            parseLiveQueryClient.subscribe(parseQuery)
        subscriptionHandling.handleEvent(SubscriptionHandling.Event.DELETE) {_, item ->
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                Log.d("CUSTOMDEBUG", "$simpleClassName - " +
                        "CurrentUser: ${mUserManager.getCurrentUser().objectId}, " +
                        "got update on item:\n$item")
                processUpdateOnChecklistItem(item)
            }
        }
    }
    */


    private fun processUpdateOnChecklistItem(item: ModuleChecklistItem) {
        val group = item.associatedModule.associatedGroup
        val index = checklistItemMap[group]!!.indexOf(item)
        val oldItem = checklistItemMap[group]!![index]

        checklistItemMap[group]!!.remove(oldItem)
        checklistItemMap[group]!!.add(item)

        //Log.d("CUSTOMDEBUG", "$simpleClassName - $item")
        //Log.d("CUSTOMDEBUG", "$simpleClassName - $oldItem")

        adapterMap[group]!!.notifyDataSetChanged()
    }

    private fun processNewChecklistItem(item: ModuleChecklistItem) {
        val group = item.associatedModule.associatedGroup
        adapterMap[group]!!.add(ChecklistItem(item))
        adapterMap[group]!!.notifyDataSetChanged()
        checklistItemMap[group]!!.add(item)
    }

    fun addItem(item: ModuleChecklistItem) {
        processNewChecklistItem(item)
        saveItemState(item)
    }

    fun saveItemState(item: ModuleChecklistItem) {
        item.saveEventually()
    }

    fun deleteChecklistItem(checklistItem: ChecklistItem) {
        val item = checklistItem.getModuleChecklistItem()
        val group = item.associatedModule.associatedGroup
        val position = adapterMap[group]!!.getAdapterPosition(checklistItem)
        adapterMap[group]!!.remove(checklistItem)
        adapterMap[group]!!.notifyItemRangeChanged(position, adapterMap[group]!!.itemCount -1)
        //adapterMap[group]!!.notifyItemRemoved(position)
        checklistItemMap[group]!!.remove(item)
        deleteItemOnDatabase(item)
        Log.d("CUSTOMDEBUG", "$simpleClassName - Count: ${adapterMap[group]!!.itemCount} " +
                "after deleting on Position $position")
    }

    private fun deleteItemOnDatabase(item: ModuleChecklistItem) {
        item.deleteEventually()
    }

    fun getAdapterByGroup(group: Group): GroupAdapter<GroupieViewHolder> {
        if(!isInitialized) {
            getAllChecklists()
            isInitialized = true
        }
        return adapterMap[group]!!
    }
}