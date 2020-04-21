package de.ntbit.projectearlybird.helper

import de.ntbit.projectearlybird.model.Message
import de.ntbit.projectearlybird.model.ModuleChecklistItem
import java.text.SimpleDateFormat
import java.util.*

/**
 * Formats a provided [Date] to [SimpleDateFormat]
 */
class DateFormatter {
    companion object {

        // TODO: Set locale to current systemlocale
        private val userLocale = Locale.GERMAN
        /*
         * Not supported by API < 24:
         * private val defaultLocale = Resources.getSystem().getConfiguration().getLocales().get(0)
         */

        /**
         * Formats timestamp of a provided [Message]
         * @param message provides the timestamp to be formatted
         * @return minimalistic and for human readable timestamp as [String]
         */
        fun formatDate(message: Message): String {
            return formatDate(message.timestamp)
        }

        /**
         * Formats timestamp of a provided [ModuleChecklistItem]
         * @param item provides the timestamp to be formatted
         * @return minimalistic and for human readable timestamp as [String]
         */
        fun formatDate(item: ModuleChecklistItem): String {
            return formatDate(item.timestamp)
        }

        /**
         * Method used by the public methods to format provided timestamp
         * @param timestamp provides the timestamp to be formatted
         * @return minimalistic and for human readable timestamp as [String]
         */
       private fun formatDate(timestamp: Date): String {
            val dayInMillis = 86400000
            val weekInMillis = 604800000
            if(System.currentTimeMillis() - timestamp.time > dayInMillis)
                return SimpleDateFormat("dd.MM.yyyy",userLocale).format(timestamp)
            return SimpleDateFormat("HH:mm",userLocale).format(timestamp)
        }

    }
}