package de.ntbit.projectearlybird.helper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.app.NotificationCompat
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.model.Message
import de.ntbit.projectearlybird.ui.activity.ChatActivity
import de.ntbit.projectearlybird.ui.activity.NewMessageActivity

/**
 * Class for creating and manage notifications
 */
class NotificationHelper {
    companion object {
        fun showNotification(message: Message, context: Context) {
            val mNotificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val channel = NotificationChannel("PEB_CHANNEL_ID",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT)
                channel.description = "YOUR_NOTIFICATION_CHANNEL_DESCRIPTION"
                channel.enableVibration(true)
                channel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
                mNotificationManager.createNotificationChannel(channel)
            }
            val mBuilder = NotificationCompat.Builder(context, "PEB_CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_info_black) // notification icon
                .setContentTitle(message.sender.username) // title for notification
                .setContentText(message.body) // message for notification
                .setAutoCancel(true) // clear notification after click
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(longArrayOf(100,200,300,400,500))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra(NewMessageActivity.USER_KEY, message.sender)
            val pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            mBuilder.setContentIntent(pi)
            mNotificationManager.notify(0, mBuilder.build())
        }

        /**
         * Sends a notification to the device of the partner
         *
         * @param contentTitle title of the notification
         * @param contentText contains the actual content of the notification
         * @param intent helps to open the needed activity after clicking on the notification
         */
        fun showNotification(contentTitle: String, contentText: String, intent: Intent) {
            val context = ApplicationContextProvider.getApplicationContext()
            val mNotificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val channel = NotificationChannel("PEB_CHANNEL_ID",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT)
                channel.description = "YOUR_NOTIFICATION_CHANNEL_DESCRIPTION"
                channel.enableVibration(true)
                channel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
                mNotificationManager.createNotificationChannel(channel)
            }
            val mBuilder = NotificationCompat.Builder(context, "PEB_CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_info_black) // notification icon
                .setContentTitle(contentTitle) // title for notification
                .setContentText(contentText) // message for notification
                .setAutoCancel(true) // clear notification after click
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(longArrayOf(100,200,300,400,500))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            val pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            mBuilder.setContentIntent(pi)
            mNotificationManager.notify(0, mBuilder.build())
        }
    }
}