package de.ntbit.projectearlybird.manager

import android.content.Context
import android.database.Cursor
import de.ntbit.projectearlybird.data.PebContract.UserEntry
import de.ntbit.projectearlybird.data.PebDbHelper
import de.ntbit.projectearlybird.model.UserProfile


internal class LocalUserManager {
    val userProfiles: ArrayList<UserProfile>

    fun getCurrentUser(context: Context?): UserProfile {
        val mDbHelper = PebDbHelper(context)
        val db = mDbHelper.readableDatabase

        val projection =
            arrayOf(UserEntry.COLUMN_USER_EMAIL, UserEntry.COLUMN_USER_USERNAME)
        val selection = UserEntry._ID + "=?"

        val c: Cursor = db.query(UserEntry.TABLE_NAME, projection, selection, null, null, null, null)

        var userProfile = UserProfile()

        println(c.count)
        println(c)

        return UserProfile()

    }

    init {
        userProfiles = ArrayList()
    }
}
