package de.ntbit.projectearlybird.data

import android.provider.BaseColumns

class PebContract {

    object UserEntry : BaseColumns {
        const val TABLE_NAME = "user"
        const val _ID = BaseColumns._ID                         // String interpeted as Text
        const val COLUMN_USER_EMAIL_VERIFIED = "emailVerified"  // Boolean interpeted as Integer
        const val COLUMN_USER_ACL = "acl"                       // ACL interpeted as Text
        const val COLUMN_USER_UPDATED_AT = "updatedAt"          // Date interpeted as Integer
        const val COLUMN_USER_AUTHDATA = "authData"             // Object interpeted as Blob
        const val COLUMN_USER_USERNAME = "username"             // String interpeted as Text
        const val COLUMN_USER_CREATED_AT = "createdAt"         // Date interpeted as Integer
        const val COLUMN_USER_PASSWORD = "password"             // String interpeted as Text
        const val COLUMN_USER_EMAIL = "email"                   // String interpeted as Text
        const val COLUMN_USER_FIRSTNAME = "firstName"          // String interpeted as Text
        const val COLUMN_USER_LASTNAME = "lastName"            // String interpeted as Text
        const val COLUMN_USER_GENDER = "gender"                 // Number interpeted as Integer
        const val COLUMN_USER_LASTLOGIN = "lastLogin"          // Date interpeted as Integer
        const val COLUMN_USER_BIRTHDAY = "birthday"             // Date interpeted as Integer
        const val COLUMN_USER_AVATAR = "avatar"                 // File interpeted as Blob
        const val COLUMN_USER_IS_ONLINE = "isOnline"            // Boolean interpeted as Int


        const val GENDER_MALE = 0
        const val GENDER_FEMALE = 1
        const val GENDER_UNKNOWN = 2

        const val IS_OFFLINE = 0
        const val IS_ONLINE = 1
    }
}