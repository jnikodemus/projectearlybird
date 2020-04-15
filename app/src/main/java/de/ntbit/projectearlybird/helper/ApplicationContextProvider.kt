package de.ntbit.projectearlybird.helper

import android.app.Application
import android.content.Context
import android.util.Log

/**
 * Provides an [Context] for non Activities
 */
class ApplicationContextProvider: Application() {

    /**
     * Binds the return of [getApplicationContext] to the variable [context]
     */
    override fun onCreate() {
        super.onCreate()
        Log.d("CUSTOMDEBUG", "ApplicationContextProvider - onCreate()")
        context = applicationContext
    }

    companion object {
        lateinit var context: Context

        /**
         * Returns the in [onCreate] instantiated [context]
         */
        fun getApplicationContext() : Context {
            return context
        }
    }
}