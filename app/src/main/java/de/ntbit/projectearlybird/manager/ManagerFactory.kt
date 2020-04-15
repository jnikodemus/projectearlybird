package de.ntbit.projectearlybird.manager

import android.util.Log
import de.ntbit.projectearlybird.manager.ManagerFactory.Companion.mAdapterManager
import de.ntbit.projectearlybird.manager.ManagerFactory.Companion.mGroupManager
import de.ntbit.projectearlybird.manager.ManagerFactory.Companion.mMessageManager
import de.ntbit.projectearlybird.manager.ManagerFactory.Companion.mUserManager

/**
 * Provides the different Managers for interacting with modelobjects.
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
        private lateinit var mModuleManager: ModuleManager
        private lateinit var mModuleChecklistManager: ModuleChecklistManager

        /**
         * Initializes all Managers.
         */
        fun initialize() {
            Log.d("CUSTOMDEBUG", "ManagerFactory - initialize() executed")
            mMessageManager = MessageManager()
            mUserManager = UserManager()
            mGroupManager = GroupManager()
            mAdapterManager = AdapterManager()
            mModuleManager = ModuleManager()
            mModuleChecklistManager = ModuleChecklistManager()
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
         * Returns the instance of [ModuleManager].
         * @return [ModuleManager]
         */
        fun getModuleManager(): ModuleManager {
            if(!::mModuleManager.isInitialized) {
                Log.d("CUSTOMDEBUG", "ManagerFactory - initializing ModuleManager")
                mModuleManager = ModuleManager()
            }
            return mModuleManager
        }

        /**
         * Returns the instance of [ModuleChecklistManager].
         * @return [ModuleChecklistManager]
         */
        fun getModuleChecklistManager(): ModuleChecklistManager {
            if(!::mModuleChecklistManager.isInitialized) {
                Log.d("CUSTOMDEBUG", "ManagerFactory - initializing ModuleChecklistManager")
                mModuleChecklistManager = ModuleChecklistManager()
            }
            return mModuleChecklistManager
        }

        /**
         * Calls [AdapterManager.getConversationsAdapter]
         */
        fun initializeAdapter() {
            getAdapterManager().getConversationsAdapter()
        }
    }
}