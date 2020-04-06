package de.ntbit.projectearlybird.manager

import android.util.Log
import com.parse.ParseQuery
import de.ntbit.projectearlybird.model.Group
import java.util.logging.Logger

class GroupManager {
    private val log = Logger.getLogger(this::class.java.simpleName)
    val mUserManager = ManagerFactory.getUserManager()


    }

    /*STOPPED HERE BECAUsE NO INTERNET*/
    fun getGroupsOfCurrentUser(): List<Group>{
        Log.d("CUSTOMDEBUG", "Fetching groups from database")
        val currentUserGroups = ArrayList<Group>()
        val query = ParseQuery.getQuery(Group::class.java)
        //query.whereEqualTo("owner", mUserManager.getCurrentUser())
        query.findInBackground { groups, e ->
            if (e == null) {
                Log.d("CUSTOMDEBUG", "HELOLOOOOOOOOOOOOOOOOOOO")
                currentUserGroups.addAll(groups)
            }
        }
        Log.d("CUSTOMDEBUG", "GOT " + currentUserGroups.size + " groups")
        for (m in currentUserGroups)
            Log.d("CUSTOMDEBUG", "Found $m")
        return currentUserGroups
    }


}