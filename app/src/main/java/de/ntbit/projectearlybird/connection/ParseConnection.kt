package de.ntbit.projectearlybird.connection

import android.content.Context
import android.util.Log
import com.parse.Parse
import com.parse.Parse.isLocalDatastoreEnabled
import com.parse.ParseInstallation
import com.parse.ParseObject
import com.parse.ParsePush
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.model.Group
import de.ntbit.projectearlybird.model.Message
import java.util.logging.Logger


class ParseConnection {

    companion object {
        private val log = Logger.getLogger(this::class.java.simpleName)

        fun initialize(context: Context) {
            registerSubclasses()

            initializeBack4App(context)
            //initializePrivateParse(context)
            //AndroidApiKey: 'AIzaSyDkeFQRd1T-SmaNU1ckRcK43cm8hu8AUi4'

            Log.d("CUSTOMDEBUG", "Parse LocalDatastore is " + (if(isLocalDatastoreEnabled()) "enabled" else "disabled"))

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

        private fun initializeBack4App(context: Context) {
            Parse.initialize(
                Parse.Configuration.Builder(context)
                    .applicationId("7J46i6wiq0gQTeF91ArANMUYVjBHcogRrzJ5EICh")
                    .clientKey("V9flekGgzi3v4neeRS7nj2BikZ921YfGvpe6kOyp")
                    .server("https://parseapi.back4app.com")
                    .enableLocalDataStore()
                    .build()
            )
        }

        private fun registerSubclasses() {
            ParseObject.registerSubclass(Message::class.java)
            ParseObject.registerSubclass(Group::class.java)
        }
    }
}