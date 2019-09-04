package de.ntbit.projectearlybird.connection

import android.content.Context
import android.util.Log
import de.ntbit.projectearlybird.manager.ParseManager
import com.parse.Parse
import java.util.logging.Logger


class ParseConnection {
    companion object {
        fun initialize(context: Context) : Unit {
            Parse.initialize(
                Parse.Configuration.Builder(context)
                    .applicationId("pYIuK6xeAMNkL2IYpOEWIiAoacyr8jEyTja8LqxV")
                    .clientKey("mpF0Gq4uUR9e7qSgFBefH6UPgJxNdaQyHxEg73tH")
                    .server("https://parseapi.back4app.com")
                    .build()
            )
        }

        fun getParseManager(x: Any, y: Any): Unit {

        }
    }
}