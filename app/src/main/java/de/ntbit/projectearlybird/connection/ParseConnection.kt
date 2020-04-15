package de.ntbit.projectearlybird.connection

import android.content.Context
import android.util.Log
import com.parse.*
import com.parse.Parse.isLocalDatastoreEnabled
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.model.*
import java.util.logging.Logger

/**
 * Class for creating a connection to the parse backend.
 */
class ParseConnection {

    companion object {
        /**
         * Calls [registerSubclasses], [initializeBack4App0],
         * [ParseInstallation.getCurrentInstallation], [ParseInstallation.saveInBackground],
         * [ParsePush.subscribeInBackground] (for the channels "Warning" and "Develop") and
         * [ManagerFactory.initialize].
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

        /**
         * Initializes Parseconnection to a private hosting.
         */
        private fun initializePrivateParse(context: Context) {
            Parse.initialize(
                Parse.Configuration.Builder(context)
                    .applicationId("APPLICATION_EARLYBIRD_DEV_0")
                    .clientKey("JH64864h3fd4g5k4h354oiu35l435d4jse354wa35g43554z" +
                            "h3e55LKBHbl843zbljhbKHBklbh834lhsjblkhbrlks87bzLKBZZBIzblkwezbrlw")
                    .server("http://217.5.174.224:1337")
                    .enableLocalDataStore()
                    .build()
            )
        }
        /**
         * Initializes the Parseconnection to Back4App hosting.
         *
         * @param context is needed to initialize [Parse.Configuration.Builder]
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

        /**
         * Initializes the Parseconnection to Back4App hosting.
         *
         * @param context is needed to initialize [Parse.Configuration.Builder]
         */
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
         * Registers all ParseSubclasses that are used.
         */
        private fun registerSubclasses() {
            ParseUser.registerSubclass(User::class.java)
            ParseObject.registerSubclass(Message::class.java)
            ParseObject.registerSubclass(Group::class.java)
            ParseObject.registerSubclass(Module::class.java)
            ParseObject.registerSubclass(ModuleChecklist::class.java)
            ParseObject.registerSubclass(ModuleMurdergame::class.java)
        }
    }
}