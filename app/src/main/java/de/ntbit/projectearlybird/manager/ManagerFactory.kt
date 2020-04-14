package de.ntbit.projectearlybird.manager

import android.util.Log

/**
 * Factory for creating multiple Manager
 *
 * @property mMessageManager Manager for managing and controlling [Message]
 * @property mUserManager Manager for managing and controlling [User]
 * @property mGroupManager Manager for managing and controlling [Group]
 * @property mAdapterManager Manager for managing and controlling adapter
 */
class ManagerFactory {
    companion object {
        private lateinit var mMessageManager: MessageManager
        private lateinit var mUserManager: UserManager
        private lateinit var mGroupManager: GroupManager
        private lateinit var mAdapterManager: AdapterManager

        /**
         * Creates all Manager
         */
        fun initialize() {
            Log.d("CUSTOMDEBUG", "ManagerFactory - initialize() executed")
            mMessageManager = MessageManager()
            mUserManager = UserManager()
            mGroupManager = GroupManager()
            mAdapterManager = AdapterManager()
        }

        /**
         * Getter for the [MessageManager]
         *
         * @return [MessageManager] that can be used globally
         */
        fun getMessageManager(): MessageManager {
            if(!::mMessageManager.isInitialized) {
                Log.d("CUSTOMDEBUG", "ManagerFactory - initializing MessageManager")
                mMessageManager = MessageManager()
            }
            return mMessageManager
        }

        /**
         * Getter for the [UserManager]
         *
         * @return [UserManager] that can be used globally
         */
        fun getUserManager(): UserManager {
            if(!::mUserManager.isInitialized) {
                Log.d("CUSTOMDEBUG", "ManagerFactory - initializing UserManager")
                mUserManager = UserManager()
            }
            return mUserManager
        }

        /**
         * Getter for the [GroupManager]
         *
         * @return [GroupManager] that can be used globally
         */
        fun getGroupManager(): GroupManager {
            if(!::mGroupManager.isInitialized) {
                Log.d("CUSTOMDEBUG", "ManagerFactory - initializing GroupManager")
                mGroupManager = GroupManager()
            }
            return mGroupManager
        }

        /**
         * Getter for the [AdapterManager]
         *
         * @return [AdapterManager] that can be used globally
         */
        fun getAdapterManager(): AdapterManager {
            if(!::mAdapterManager.isInitialized) {
                Log.d("CUSTOMDEBUG", "ManagerFactory - initializing AdapterManager")
                mAdapterManager = AdapterManager()
            }
            return mAdapterManager
        }

        /**
         * initializes the adapter for the [ConversationsActivity]
         */
        fun initializeAdapter() {
            getAdapterManager().getConversationsAdapter()
        }
    }
}