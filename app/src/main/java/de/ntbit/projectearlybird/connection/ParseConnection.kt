package de.ntbit.projectearlybird.connection

import android.content.Context
import de.ntbit.projectearlybird.manager.ParseManager
import com.parse.Parse
import java.util.logging.Logger
import com.parse.ParseObject
import de.ntbit.projectearlybird.model.Message
import de.ntbit.projectearlybird.model.UserProfile


class ParseConnection {

    companion object {
        private val log = Logger.getLogger(this::class.java.simpleName)
        private var mParseManager: ParseManager? = null

        fun initialize(context: Context) : ParseManager? {
            //ParseObject.registerSubclass(UserProfile::class.java)
            ParseObject.registerSubclass(Message::class.java)
            Parse.initialize(
                Parse.Configuration.Builder(context)
                    .applicationId("pYIuK6xeAMNkL2IYpOEWIiAoacyr8jEyTja8LqxV")
                    .clientKey("mpF0Gq4uUR9e7qSgFBefH6UPgJxNdaQyHxEg73tH")
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