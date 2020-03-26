package de.ntbit.projectearlybird.manager

import java.util.logging.Logger

class ManagerFactory {
    companion object {
        private val log = Logger.getLogger(this::class.java.simpleName)
        private lateinit var mMessageManager: MessageManager
        private lateinit var mUserManager: UserManager
        private lateinit var mGroupManager: GroupManager

        fun initialize() {
            mMessageManager = MessageManager()
            mUserManager = UserManager()
        }

        fun getMessageManager(): MessageManager {
            if(!::mMessageManager.isInitialized)
                mMessageManager = MessageManager()
            return mMessageManager
        }

        fun getUserManager(): UserManager {
            if(!::mUserManager.isInitialized)
                mUserManager = UserManager()
            return mUserManager
        }

        fun getGroupManager(): GroupManager {
            if(!::mGroupManager.isInitialized)
                mGroupManager = GroupManager()
            return mGroupManager
        }
    }
}