package de.ntbit.projectearlybird.helper

import android.app.Application
import android.content.Context
import android.util.Log

/**
 * Class to create a global context
 */
class ApplicationContextProvider: Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d("CUSTOMDEBUG", "ApplicationContextProvider - onCreate()")
        context = applicationContext
    }

    companion object {
        lateinit var context: Context

        fun getApplicationContext() : Context {
            return context
        }
    }
}