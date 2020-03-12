package de.ntbit.projectearlybird.connection

import android.content.Context
import de.ntbit.projectearlybird.manager.ParseManager
import com.parse.Parse
import java.util.logging.Logger
import com.parse.ParseObject
import de.ntbit.projectearlybird.model.Message


class ParseConnection {

    companion object {
        private val log = Logger.getLogger(this::class.java.simpleName)
        private var mParseManager: ParseManager? = null

        fun initialize(context: Context) : ParseManager? {
            //ParseObject.registerSubclass(UserProfile::class.java)
            ParseObject.registerSubclass(Message::class.java)
            // Enable local storage of Parseobjects
            Parse.enableLocalDatastore(context)
            Parse.initialize(
                Parse.Configuration.Builder(context)
                    .applicationId("7J46i6wiq0gQTeF91ArANMUYVjBHcogRrzJ5EICh")
                    .clientKey("V9flekGgzi3v4neeRS7nj2BikZ921YfGvpe6kOyp")
                    .server("https://parseapi.back4app.com")
                    .build()
            )
            mParseManager = ParseManager()
            return mParseManager
        }

        fun getParseManager() : ParseManager? {
            return mParseManager
        }
    }
}