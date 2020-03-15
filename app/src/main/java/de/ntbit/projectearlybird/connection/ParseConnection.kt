package de.ntbit.projectearlybird.connection

import android.content.Context
import com.parse.Parse
import com.parse.ParseInstallation
import java.util.logging.Logger
import com.parse.ParseObject
import com.parse.ParsePush
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.model.Message
import de.ntbit.projectearlybird.model.User


class ParseConnection {

    companion object {
        private val log = Logger.getLogger(this::class.java.simpleName)

        fun initialize(context: Context) {
            ParseObject.registerSubclass(Message::class.java)
            ParseObject.registerSubclass(User::class.java)
            // Enable local storage of Parseobjects
            //Parse.enableLocalDatastore(context)
            Parse.initialize(
                Parse.Configuration.Builder(context)
                    .applicationId("7J46i6wiq0gQTeF91ArANMUYVjBHcogRrzJ5EICh")
                    .clientKey("V9flekGgzi3v4neeRS7nj2BikZ921YfGvpe6kOyp")
                    .server("https://parseapi.back4app.com")
                    .build()
            )
            //AndroidApiKey: 'AIzaSyDkeFQRd1T-SmaNU1ckRcK43cm8hu8AUi4'

            ParseInstallation.getCurrentInstallation().saveInBackground()
            //installation.put("GCMSenderId", 474988434121)
            //installation.saveInBackground()
            ParsePush.subscribeInBackground("Warning")
            ParsePush.subscribeInBackground("Develop")

            ManagerFactory.initialize()
        }
    }
}