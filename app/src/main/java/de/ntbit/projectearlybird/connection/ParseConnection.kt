package de.ntbit.projectearlybird.connection

import android.content.Context
import de.ntbit.projectearlybird.manager.ParseManager
import com.parse.Parse
import java.util.logging.Logger


class ParseConnection {

    companion object {
        private var logger: Logger = Logger.getLogger(this::class.toString())
        private var parseManager: ParseManager? = null

        fun initialize(context: Context) : ParseManager? {
            Parse.initialize(
                Parse.Configuration.Builder(context)
                    .applicationId("pYIuK6xeAMNkL2IYpOEWIiAoacyr8jEyTja8LqxV")
                    .clientKey("mpF0Gq4uUR9e7qSgFBefH6UPgJxNdaQyHxEg73tH")
                    .server("https://parseapi.back4app.com")
                    .build()
            )
            parseManager = ParseManager()
            println("ParseManager is null: " + (parseManager == null))
            return parseManager
        }

        fun getParseManager() : ParseManager? {
            return parseManager
        }
    }
}