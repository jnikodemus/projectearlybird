package de.ntbit.projectearlybird.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import de.ntbit.projectearlybird.data.PebContract.UserEntry
import de.ntbit.projectearlybird.model.User

@Deprecated("Use the local Datastore of Parse instead")
class PebDbHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    companion object {
        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "peb.db"
        const val SQL_TEXT_TYPE = " TEXT"
        const val SQL_INTEGER_TYPE = " INTEGER"
        const val SQL_BLOB_TYPE = " BLOB"
        const val SQL_PRIMARY_KEY = " PRIMARY KEY"
        const val SQL_PRIMARY_KEY_AUTOINCREMENT = " PRIMARY KEY AUTOINCREMENT"
        const val SQL_NOT_NULL = " NOT NULL"
        const val SQL_COMMA_SEP = ","

        const val SQL_CREATE_ENTRIES =
            "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                    UserEntry._ID + SQL_TEXT_TYPE + SQL_PRIMARY_KEY + SQL_COMMA_SEP +
                    UserEntry.COLUMN_USER_EMAIL_VERIFIED + SQL_INTEGER_TYPE + SQL_NOT_NULL + " DEFAULT 0" + SQL_COMMA_SEP +
                    // TODO: MAKE ACL NOT NULL
                    UserEntry.COLUMN_USER_ACL + SQL_TEXT_TYPE + SQL_COMMA_SEP +
                    UserEntry.COLUMN_USER_UPDATED_AT + SQL_INTEGER_TYPE + SQL_COMMA_SEP +
                    UserEntry.COLUMN_USER_AUTHDATA + SQL_BLOB_TYPE + SQL_COMMA_SEP +
                    UserEntry.COLUMN_USER_USERNAME + SQL_TEXT_TYPE + SQL_NOT_NULL + SQL_COMMA_SEP +
                    // TODO: MAKE CREATED_AT NOT NULL
                    UserEntry.COLUMN_USER_CREATED_AT + SQL_INTEGER_TYPE + SQL_COMMA_SEP +
                    UserEntry.COLUMN_USER_PASSWORD + SQL_TEXT_TYPE + SQL_COMMA_SEP +
                    UserEntry.COLUMN_USER_EMAIL + SQL_TEXT_TYPE + SQL_NOT_NULL + SQL_COMMA_SEP +
                    UserEntry.COLUMN_USER_FIRSTNAME + SQL_TEXT_TYPE + SQL_COMMA_SEP +
                    UserEntry.COLUMN_USER_LASTNAME + SQL_TEXT_TYPE + SQL_COMMA_SEP +
                    UserEntry.COLUMN_USER_GENDER + SQL_INTEGER_TYPE + SQL_NOT_NULL + SQL_COMMA_SEP +
                    UserEntry.COLUMN_USER_LASTLOGIN + SQL_INTEGER_TYPE + SQL_COMMA_SEP +
                    UserEntry.COLUMN_USER_BIRTHDAY + SQL_INTEGER_TYPE + SQL_COMMA_SEP +
                    UserEntry.COLUMN_USER_AVATAR + SQL_BLOB_TYPE + SQL_COMMA_SEP +
                    UserEntry.COLUMN_USER_IS_ONLINE + SQL_INTEGER_TYPE +
                    ")"

        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME
    }
}