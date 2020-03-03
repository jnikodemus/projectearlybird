package de.ntbit.projectearlybird.data

import android.provider.BaseColumns

class PebContract {

    object UserEntry : BaseColumns {
        const val TABLE_NAME = "user"
        const val _ID = BaseColumns._ID
        const val COLUMN_USER_USERNAME = "username"
        const val COLUMN_USER_PASSWORD = "password"
        const val COLUMN_USER_EMAIL = "email"
        const val COLUMN_USER_FIRSTNAME = "first_name"
        const val COLUMN_USER_LASTNAME = "last_name"
        const val COLUMN_USER_BIRTHDAY = "birthday"
        const val COLUMN_USER_GENDER = "gender"
        const val COLUMN_USER_LASTLOGIN = "last_login"
        const val COLUMN_USER_CREATED_AT = "created_at"
        const val COLUMN_USER_PARSE_USER = "parse_user"
        const val COLUMN_USER_AVATAR = "avatar"

        const val GENDER_MALE = 0
        const val GENDER_FEMALE = 1
        const val GENDER_UNKNOWN = 2
    }
}