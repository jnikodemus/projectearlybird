package de.ntbit.projectearlybird.manager

import android.util.Log
import de.ntbit.projectearlybird.manager.ManagerFactory.Companion.mAdapterManager
import de.ntbit.projectearlybird.manager.ManagerFactory.Companion.mGroupManager
import de.ntbit.projectearlybird.manager.ManagerFactory.Companion.mMessageManager
import de.ntbit.projectearlybird.manager.ManagerFactory.Companion.mUserManager

/**
 * Provides the different Managers for interacting with modelobjects.
 * @property mMessageManager holds an instance of [MessageManager]
 * @property mUserManager holds an instance of [UserManager]
 * @property mGroupManager holds an instance of [GroupManager]
 * @property mAdapterManager holds an instance of [AdapterManager]
 */
class ManagerFactory {
    companion object {
        private lateinit var mMessageManager: MessageManager
        private lateinit var mUserManager: UserManager
        private lateinit var mGroupManager: GroupManager
        private lateinit var mAdapterManager: AdapterManager

        /**
         * Initializes all Managers.
         */
        fun initialize() {
            Log.d("CUSTOMDEBUG", "ManagerFactory - initialize() executed")
            mMessageManager = MessageManager()
            mUserManager = UserManager()
            mGroupManager = GroupManager()
            mAdapterManager = AdapterManager()
        }

        /**
         * Returns the instance of [MessageManager].
         * @return [MessageManager]
         */
        fun getMessageManager(): MessageManager {
            if(!::mMessageManager.isInitialized) {
                Log.d("CUSTOMDEBUG", "ManagerFactory - initializing MessageManager")
                mMessageManager = MessageManager()
            }
            return mMessageManager
        }

        /**
         * Returns the instance of [UserManager].
         * @return [UserManager]
         */
        fun getUserManager(): UserManager {
            if(!::mUserManager.isInitialized) {
                Log.d("CUSTOMDEBUG", "ManagerFactory - initializing UserManager")
                mUserManager = UserManager()
            }
            return mUserManager
        }

        /**
         * Returns the instance of [GroupManager].
         * @return [GroupManager]
         */
        fun getGroupManager(): GroupManager {
            if(!::mGroupManager.isInitialized) {
                Log.d("CUSTOMDEBUG", "ManagerFactory - initializing GroupManager")
                mGroupManager = GroupManager()
            }
            return mGroupManager
        }

        /**
         * Returns the instance of [AdapterManager].
         * @return [AdapterManager]
         */
        fun getAdapterManager(): AdapterManager {
            if(!::mAdapterManager.isInitialized) {
                Log.d("CUSTOMDEBUG", "ManagerFactory - initializing AdapterManager")
                mAdapterManager = AdapterManager()
            }
            return mAdapterManager
        }

        /**
         * Calls [AdapterManager.getConversationsAdapter]
         */
        fun initializeAdapter() {
            getAdapterManager().getConversationsAdapter()
        }
    }
}