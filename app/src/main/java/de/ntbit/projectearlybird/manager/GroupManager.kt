package de.ntbit.projectearlybird.manager

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.parse.ParseQuery
import com.parse.livequery.ParseLiveQueryClient
import com.parse.livequery.SubscriptionHandling
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.adapter.item.GroupItem
import de.ntbit.projectearlybird.model.Group
import de.ntbit.projectearlybird.model.Message
import java.net.URI
import java.util.logging.Logger

class GroupManager {

    private val parseLiveQueryClient: ParseLiveQueryClient =
        ParseLiveQueryClient.Factory.getClient(URI("wss://projectearlybird.back4app.io/"))
    private val mUserManager = ManagerFactory.getUserManager()
    private lateinit var adapter: GroupAdapter<GroupieViewHolder>

    fun getAdapter() : GroupAdapter<GroupieViewHolder> {
        if(!::adapter.isInitialized) {
            adapter = GroupAdapter()
            readGroups()
            listenForGroups()
        }
        return adapter
    }

    /*STOPPED HERE BECAUSE NO INTERNET*/
    private fun readGroups() {
        Log.d("CUSTOMDEBUG", "Fetching groups from database")
        //val currentUserGroups = ArrayList<Group>()
        val query = ParseQuery.getQuery(Group::class.java)
        //query.whereEqualTo("owner", mUserManager.getCurrentUser())
        query.findInBackground { groups, e ->
            if (e == null) {
                Log.d("CUSTOMDEBUG", "HELOLOOOOOOOOOOOOOOOOOOO")
                for(group in groups)
                    adapter.add(GroupItem(group))
                Log.d("CUSTOMDEBUG", "GOT " + groups.size + " groups")
            }
        }
        //Log.d("CUSTOMDEBUG", "GOT " + currentUserGroups.size + " groups")
        //for (m in currentUserGroups)
            //Log.d("CUSTOMDEBUG", "Found $m")
    }

    private fun listenForGroups() {
        val parseQuery = ParseQuery.getQuery(Group::class.java)
        val subscriptionHandling: SubscriptionHandling<Group> = parseLiveQueryClient.subscribe(parseQuery)

        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE) { _, group ->
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                processNewGroup(group)
            }
        }
    }

    private fun processNewGroup(group: Group) {
        adapter.add(GroupItem(group))
        adapter.notifyItemInserted(adapter.itemCount)
    }

}