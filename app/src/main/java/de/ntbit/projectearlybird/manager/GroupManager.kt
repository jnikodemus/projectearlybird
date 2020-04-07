package de.ntbit.projectearlybird.manager

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.parse.ParseQuery
import com.parse.livequery.ParseLiveQueryClient
import com.parse.livequery.SubscriptionHandling
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.R.*
import de.ntbit.projectearlybird.adapter.item.GroupItem
import de.ntbit.projectearlybird.helper.ApplicationContextProvider
import de.ntbit.projectearlybird.helper.NotificationHelper
import de.ntbit.projectearlybird.model.Group
import de.ntbit.projectearlybird.model.User
import de.ntbit.projectearlybird.ui.activity.GroupActivity
import java.net.URI

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
        Log.d("CUSTOMDEBUG", "GroupManager - Fetching groups from database")
        //val currentUserGroups = ArrayList<Group>()
        val query = ParseQuery.getQuery(Group::class.java)
        //query.whereEqualTo("owner", mUserManager.getCurrentUser())
        query.findInBackground { groups, e ->
            if (e == null) {
                for(group in groups)
                    adapter.add(GroupItem(group))
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
                NotificationHelper.showNotification(group.name,
                    ApplicationContextProvider.getApplicationContext()
                        .getString(string.group_added_to_new)
                        .replace("GROUPNAME",group.name),
                    Intent(ApplicationContextProvider.getApplicationContext(), GroupActivity::class.java)
                        .putExtra("GROUP",group))
            }
        }
    }

    private fun processNewGroup(group: Group) {
        Log.d("CUSTOMDEBUG", "GroupManager - processing new Group")
        adapter.add(GroupItem(group))
        adapter.notifyDataSetChanged()
    }


    // TODO: Check admin/owner leaving; implement size < 2
    fun leaveGroup(group: Group): Boolean {
        val currentUser = mUserManager.getCurrentUser()
        val members = group.members
        val admins = group.admins
        val posToDelete = adapter.getAdapterPosition(GroupItem(group))

        if(group.getSize() > 1) {
            members.remove(currentUser)
            admins.remove(currentUser)
            if(group.owner == currentUser) {
                group.owner = members[0]
            }
            if(!admins.contains(group.owner))
                admins.add(group.owner)

            group.members = members
            group.admins = admins
            group.updateACL()
            group.save()

            // TODO: search for right Group in adapter
            adapter.removeGroupAtAdapterPosition(posToDelete)
            adapter.notifyItemRemoved(posToDelete)

            return true
        }
        return false
    }

    fun addUser(user: User, group: Group): Boolean {
        val members = group.members
        if(!group.members.contains(user)) {
            members.add(user)
            group.members = members
            group.updateACL()
            return true
        }
        else {
            Log.d("CUSTOMDEBUG", "GroupManager - Did not add ${user.username}. Maybe already member?")
            return false
        }
    }

}