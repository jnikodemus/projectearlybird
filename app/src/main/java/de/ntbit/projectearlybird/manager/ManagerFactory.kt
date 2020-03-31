package de.ntbit.projectearlybird.manager

import android.util.Log

class ManagerFactory {
    companion object {
        private lateinit var mMessageManager: MessageManager
        private lateinit var mUserManager: UserManager
        private lateinit var mGroupManager: GroupManager
        private lateinit var mAdapterManager: AdapterManager

        fun initialize() {
            Log.d("CUSTOMDEBUG", "ManagerFactory - initialize() executed")
            mMessageManager = MessageManager()
            mUserManager = UserManager()
            mGroupManager = GroupManager()
            mAdapterManager = AdapterManager()
        }

        fun getMessageManager(): MessageManager {
            if(!::mMessageManager.isInitialized) {
                Log.d("CUSTOMDEBUG", "ManagerFactory - initializing MessageManager")
                mMessageManager = MessageManager()
            }
            return mMessageManager
        }

        fun getUserManager(): UserManager {
            if(!::mUserManager.isInitialized) {
                Log.d("CUSTOMDEBUG", "ManagerFactory - initializing UserManager")
                mUserManager = UserManager()
            }
            return mUserManager
        }

        fun getGroupManager(): GroupManager {
            if(!::mGroupManager.isInitialized) {
                Log.d("CUSTOMDEBUG", "ManagerFactory - initializing GroupManager")
                mGroupManager = GroupManager()
            }
            return mGroupManager
        }

        fun getAdapterManager(): AdapterManager {
            if(!::mAdapterManager.isInitialized) {
                Log.d("CUSTOMDEBUG", "ManagerFactory - initializing AdapterManager")
                mAdapterManager = AdapterManager()
            }
            return mAdapterManager
        }

        fun initializeAdapter() {
            getAdapterManager().getConversationsAdapter()
        }
    }
}