package de.ntbit.projectearlybird.manager

import java.util.logging.Logger

class ManagerFactory {
    companion object {
        private val log = Logger.getLogger(this::class.java.simpleName)
        private lateinit var mMessageManager: MessageManager
        private lateinit var mUserManager: UserManager

        fun initialize() {
            mMessageManager = MessageManager()
            mUserManager = UserManager()
        }

        fun getMessageManager(): MessageManager {
            return mMessageManager
        }

        fun getUserManager(): UserManager {
            return mUserManager
        }
    }
}