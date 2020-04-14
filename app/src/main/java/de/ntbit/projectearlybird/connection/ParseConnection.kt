package de.ntbit.projectearlybird.connection

import android.content.Context
import android.util.Log
import com.parse.*
import com.parse.Parse.isLocalDatastoreEnabled
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.model.*
import java.util.logging.Logger

/**
 * Class for creating a connection to the parse server
 *
 * @property log for logging
 * @constructor ???
 */
class ParseConnection {

    companion object {
        private val log = Logger.getLogger(this::class.java.simpleName)
        /**
         * Calls every function in this class in the correct order. The subclasses have to be registered before anything else. Also starts the [ManagerFactory]
         */
        fun initialize(context: Context) {
            registerSubclasses()

            initializeBack4App0(context)
            //initializePrivateParse(context)
            //AndroidApiKey: 'AIzaSyDkeFQRd1T-SmaNU1ckRcK43cm8hu8AUi4'

            ParseInstallation.getCurrentInstallation().saveInBackground()
            //installation.put("GCMSenderId", 474988434121)
            //installation.saveInBackground()
            ParsePush.subscribeInBackground("Warning")
            ParsePush.subscribeInBackground("Develop")

            ManagerFactory.initialize()
        }

        private fun initializePrivateParse(context: Context) {
            Parse.initialize(
                Parse.Configuration.Builder(context)
                    .applicationId("APPLICATION_EARLYBIRD_DEV_0")
                    .clientKey("JH64864h3fd4g5k4h354oiu35l435d4jse354wa35g43554zh3e55LKBHbl843zbljhbKHBklbh834lhsjblkhbrlks87bzLKBZZBIzblkwezbrlw")
                    .server("http://217.5.174.224:1337")
                    .enableLocalDataStore()
                    .build()
            )
        }
        /**
         * Initializes the ParseConnection
         *
         * @param context ???
         */
        private fun initializeBack4App0(context: Context) {
            Parse.initialize(
                Parse.Configuration.Builder(context)
                    .applicationId("pYIuK6xeAMNkL2IYpOEWIiAoacyr8jEyTja8LqxV")
                    .clientKey("mpF0Gq4uUR9e7qSgFBefH6UPgJxNdaQyHxEg73tH")
                    .server("https://parseapi.back4app.com")
                    .enableLocalDataStore()
                    .build()
            )
        }

        private fun initializeBack4App1(context: Context) {
            Parse.initialize(
                Parse.Configuration.Builder(context)
                    .applicationId("7J46i6wiq0gQTeF91ArANMUYVjBHcogRrzJ5EICh")
                    .clientKey("V9flekGgzi3v4neeRS7nj2BikZ921YfGvpe6kOyp")
                    .server("https://parseapi.back4app.com")
                    .enableLocalDataStore()
                    .build()
            )
        }
        /**
         * In this function every subclass in our app has to be registered here
         */
        private fun registerSubclasses() {
            ParseUser.registerSubclass(User::class.java)
            ParseObject.registerSubclass(Message::class.java)
            ParseObject.registerSubclass(Group::class.java)
            ParseObject.registerSubclass(Module::class.java)
            ParseObject.registerSubclass(ModuleChecklist::class.java)
        }
    }
}