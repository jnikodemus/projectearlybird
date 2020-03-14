package de.ntbit.projectearlybird.connection

import android.content.Context
import com.parse.Parse
import java.util.logging.Logger
import com.parse.ParseObject
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.model.Message


class ParseConnection {

    companion object {
        private val log = Logger.getLogger(this::class.java.simpleName)

        fun initialize(context: Context) {
            //ParseObject.registerSubclass(Message::class.java)
            // Enable local storage of Parseobjects
            Parse.enableLocalDatastore(context)
            Parse.initialize(
                Parse.Configuration.Builder(context)
                    .applicationId("7J46i6wiq0gQTeF91ArANMUYVjBHcogRrzJ5EICh")
                    .clientKey("V9flekGgzi3v4neeRS7nj2BikZ921YfGvpe6kOyp")
                    .server("https://parseapi.back4app.com")
                    .build()
            )
            ManagerFactory.initialize()
        }
    }
}