package de.ntbit.projectearlybird.manager

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.parse.ParseQuery
import com.parse.livequery.ParseLiveQueryClient
import com.parse.livequery.SubscriptionHandling
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.R.string
import de.ntbit.projectearlybird.adapter.item.GroupItem
import de.ntbit.projectearlybird.helper.ApplicationContextProvider
import de.ntbit.projectearlybird.helper.NotificationHelper
import de.ntbit.projectearlybird.helper.ParcelContract
import de.ntbit.projectearlybird.model.Group
import de.ntbit.projectearlybird.model.User
import de.ntbit.projectearlybird.ui.activity.GroupActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URI

/**
 * Provides an object of [GroupManager] which is used for interacting
 * with objects of the [Group] model.
 * @property parseLiveQueryClient holds instance of [ParseLiveQueryClient]
 * @property mUserManager holds instance of [UserManager]
 * @property adapter holds instance of [GroupAdapter]<[GroupieViewHolder]>
 */

class GroupManager {

    private val simpleClassName = this.javaClass.simpleName

    private val parseLiveQueryClient: ParseLiveQueryClient =
        ParseLiveQueryClient.Factory.getClient(URI("wss://projectearlybird.back4app.io/"))
    private val mUserManager = ManagerFactory.getUserManager()
    private lateinit var adapter: GroupAdapter<GroupieViewHolder>
    private val groupSet = HashSet<Group>()

    /**
     * If the [adapter] is not initialized by calltime, it will be initialized and [readGroups]
     * and [listenForGroups] are called before return.
     * @return [GroupAdapter]<[GroupieViewHolder]>
     */
    fun getAdapter(): GroupAdapter<GroupieViewHolder> {
        if (!::adapter.isInitialized) {
            adapter = GroupAdapter()
            readGroups()
            listenForGroups()
        }
        return adapter
    }

    /**
     * Reads all existing groups in the ParseDatabase, transforms them to [GroupItem] and adds
     * them to [adapter].
     */
    private fun readGroups() {
        //Log.d("CUSTOMDEBUG", "GroupManager - Fetching groups from database")
        //val currentUserGroups = ArrayList<Group>()
        val query = ParseQuery.getQuery(Group::class.java)
        //query.whereEqualTo("owner", mUserManager.getCurrentUser())
        query.findInBackground { groups, e ->
            if (e == null) {
                Log.d("CUSTOMDEBUG", "$simpleClassName - adding ${groups.size} groups.")
                groupSet.addAll(groups)
                for (group in groups)
                    adapter.add(GroupItem(group))
            } else Log.d("CUSTOMDEBUG", "$simpleClassName - ERROR -> ${e.message}")
        }
        //Log.d("CUSTOMDEBUG", "GOT " + currentUserGroups.size + " groups")
        //for (m in currentUserGroups)
        //Log.d("CUSTOMDEBUG", "Found $m")
    }

    /**
     * Subscribes to [ParseQuery] of [Group] which calls [processNewGroup]
     * and [NotificationHelper.showNotification] if the current user is added to a group.
     */
    private fun listenForGroups() {
        val parseQueryOwnGroups = ParseQuery.getQuery(Group::class.java)
            .whereContains("owner", mUserManager.getCurrentUser().objectId)
        val parseQuery = ParseQuery.getQuery(Group::class.java)
            //.whereDoesNotMatchKeyInQuery("objectId","objectId", parseQueryOwnGroups)
        val subscriptionHandling: SubscriptionHandling<Group> =
            parseLiveQueryClient.subscribe(parseQuery)

        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE) { _, group ->
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                processNewGroup(group)
                // TODO: remove following if() and use parseQueryOwnGroups instead!
                if(group.owner != mUserManager.getCurrentUser())
                    NotificationHelper.showNotification(
                        group.name,
                        ApplicationContextProvider.getApplicationContext()
                            .getString(string.group_added_to_new)
                            .replace("GROUPNAME", group.name),
                        Intent(
                            ApplicationContextProvider.getApplicationContext(),
                            GroupActivity::class.java
                        )
                            .putExtra(ParcelContract.GROUP_KEY, group)
                    )
            }
        }
        subscriptionHandling.handleEvent(SubscriptionHandling.Event.UPDATE) {_, group ->
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                processNewGroup(group)
                // TODO: remove following if() and use parseQueryOwnGroups instead!
                if(group.owner != mUserManager.getCurrentUser())
                    NotificationHelper.showNotification(
                        group.name,
                        ApplicationContextProvider.getApplicationContext()
                            .getString(string.group_added_to_new)
                            .replace("GROUPNAME", group.name),
                        Intent(
                            ApplicationContextProvider.getApplicationContext(),
                            GroupActivity::class.java
                        )
                            .putExtra(ParcelContract.GROUP_KEY, group)
                    )
            }
        }
    }

    /**
     * Transforms provided [group] to [GroupItem] and adds it to the [adapter] notifying it for
     * a changed dataset afterwards.
     * @param group that was found by the [SubscriptionHandling] will be added to the adapter
     */
    private fun processNewGroup(group: Group) {
        //Log.d("CUSTOMDEBUG", "GroupManager - processing new Group")
        adapter.add(GroupItem(group))
        adapter.notifyDataSetChanged()
        groupSet.add(group)
    }


    /**
     * Leaves the provided [group] by removing the current user from memberlist and adminlist.
     * If the leaving user was the only admin, a member will be added to the adminlist.
     * If the user was owner of the group, the next admin will be the new owner.
     * @return [Boolean]: true if user could leave, false else.
     */
    fun leaveGroup(user: User, group: Group): Boolean {
        return leaveGroup(user, group, -1)
    }

    fun leaveGroup(group: Group, positionToDelete: Int): Boolean {
        return leaveGroup(mUserManager.getCurrentUser(), group, positionToDelete)
    }

    fun leaveGroup(user: User, group: Group, positionToDelete: Int): Boolean {
        val members = group.members
        val admins = group.admins

        Log.d("CUSTOMDEBUG", "$simpleClassName - $positionToDelete; ${adapter.itemCount}")

        if (group.getSize() > 1) {
            members.remove(user)
            if (group.owner == user) {
                group.owner = members[0]
            }
            if (!admins.contains(group.owner))
                admins.add(group.owner)
            admins.remove(user)

            group.members = members
            group.admins = admins
            group.updateACL()
            group.save()
        }
        else {
            group.deleteEventually()
        }

        groupSet.remove(group)
        if(positionToDelete != -1) {
            adapter.removeGroupAtAdapterPosition(positionToDelete)
            adapter.notifyDataSetChanged()
        }

        return true
    }



    /**
     * Adds the provided [user] to the memberlist of the provided [group] if not already member.
     * Returns true if the adding was successful.
     * @return [Boolean]
     */
    fun addUser(user: User, group: Group): Boolean {
        val members = group.members
        return if (!group.members.contains(user)) {
            members.add(user)
            group.members = members
            group.updateACL()
            group.saveEventually()
            true
        } else {
            Log.d(
                "CUSTOMDEBUG",
                "GroupManager - Did not add ${user.username}. Maybe already member?"
            )
            false
        }
    }

    fun save(group: Group) {
        group.saveEventually {
            if (it != null)
                Log.d("CUSTOMDEBUG", "$simpleClassName - Error at save(): ${it.message}")
        }
    }

    fun getGroups(): HashSet<Group> {
        Log.d("CUSTOMDEBUG", "$simpleClassName - returning ${groupSet.size} groups")
        return groupSet
    }

    fun isAdmin(group: Group): Boolean {
        return group.admins.contains(mUserManager.getCurrentUser())
    }

    fun promote(user: User, group: Group) {
        Log.d("CUSTOMDEBUG", "$simpleClassName - promoted ${user.username}")
    }

    fun removeUser(user: User, group: Group): Boolean {
        Log.d("CUSTOMDEBUG", "$simpleClassName - removing ${user.username}")
        return leaveGroup(user, group)
    }

    /**
     * Returns the [Group] for provided [objectId] from pin.
     */
    @Deprecated("Should not be used anymore.")
    fun getGroupById(objectId: String): Group {
        val query = ParseQuery(Group::class.java)
        query.fromPin("group")
        return query.first
    }

}