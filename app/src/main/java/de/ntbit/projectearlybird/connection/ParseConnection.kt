package de.ntbit.projectearlybird.connection

import android.content.Context
import android.util.Log
import com.parse.*
import com.parse.Parse.isLocalDatastoreEnabled
import com.parse.livequery.ParseLiveQueryClient
import com.parse.livequery.SubscriptionHandling
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.model.*
import java.net.URI
import java.util.logging.Logger

/**
 * Class for creating a connection to the parse backend.
 */
class ParseConnection {

    companion object {

        private var parseLiveQueryClient: ParseLiveQueryClient? = null
        private var moduleChecklistItemQuery: ParseQuery<ModuleChecklistItem>? = null
        private var newItemHandling: SubscriptionHandling<ModuleChecklistItem>? = null
        private var updateItemHandling: SubscriptionHandling<ModuleChecklistItem>? = null
        private var deleteItemHandling: SubscriptionHandling<ModuleChecklistItem>? = null

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

            initializeQueryClient()
        }

        fun getModuleChecklistItemQuery(): ParseQuery<ModuleChecklistItem>? {
            return moduleChecklistItemQuery
        }

        fun getModuleChecklistItemUpdateHandling(): SubscriptionHandling<ModuleChecklistItem>? {
            return updateItemHandling
        }

        fun getModuleChecklistItemNewHandling(): SubscriptionHandling<ModuleChecklistItem>? {
            return newItemHandling
        }

        fun getModuleChecklistItemDeleteHandling(): SubscriptionHandling<ModuleChecklistItem>? {
            return deleteItemHandling
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
            ParseObject.registerSubclass(ModuleChecklistItem::class.java)
            ParseObject.registerSubclass(ModuleMurdergame::class.java)
        }

        private fun initializeQueryClient() {
            parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(URI("wss://projectearlybird.back4app.io/"))
            moduleChecklistItemQuery = ParseQuery.getQuery(ModuleChecklistItem::class.java)
            newItemHandling =
                ParseConnection.parseLiveQueryClient?.subscribe(moduleChecklistItemQuery)
            updateItemHandling =
                ParseConnection.parseLiveQueryClient?.subscribe(moduleChecklistItemQuery)
            deleteItemHandling =
                ParseConnection.parseLiveQueryClient?.subscribe(moduleChecklistItemQuery)
        }
    }
}