package de.ntbit.projectearlybird.manager

import java.util.logging.Logger

class ManagerFactory {
    companion object {
        private val log = Logger.getLogger(this::class.java.simpleName)
        private var mMessageManager: MessageManager? = null
        private var mUserManager: UserManager? = null

        fun initialize() {
            mMessageManager = MessageManager()
            mUserManager = UserManager()
        }

        fun getMessageManager(): MessageManager {
            if(mMessageManager == null)
                mMessageManager = MessageManager()
            return mMessageManager!!
        }

        fun getUserManager(): UserManager {
            if(mUserManager == null)
                mUserManager = UserManager()
            return mUserManager!!
        }
    }
}