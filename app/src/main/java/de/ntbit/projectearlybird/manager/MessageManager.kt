package de.ntbit.projectearlybird.manager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.RecyclerView
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.livequery.ParseLiveQueryClient
import com.parse.livequery.SubscriptionHandling
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.adapter.item.ChatFromItem
import de.ntbit.projectearlybird.adapter.item.ChatSelfItem
import de.ntbit.projectearlybird.model.Message
import de.ntbit.projectearlybird.model.User
import de.ntbit.projectearlybird.ui.activity.ChatActivity
import de.ntbit.projectearlybird.ui.activity.NewMessageActivity
import java.net.URI

/**
 * [MessageManager] is used for interacting with [Message] objects.
 *
 * @property simpleClassName holds the simple name of this class
 * @property mUserManager holds an instance of [UserManager]
 * @property mAdapterManager holds an instance of [AdapterManager]
 * @property parseLiveQueryClient holds an instance of [ParseLiveQueryClient]
 */
class MessageManager {

    private val simpleClassName = this.javaClass.simpleName
    private val mUserManager = ManagerFactory.getUserManager()
    private val mAdapterManager = ManagerFactory.getAdapterManager()
    private val parseLiveQueryClient: ParseLiveQueryClient =
        ParseLiveQueryClient.Factory.getClient(URI("wss://projectearlybird.back4app.io/"))

    /**
     * Sends a String as Message to [recipient] if [body] isNotEmpty() and [body] isNotBlank()
     * @param body contains the text from a [Message]
     * @param recipient which receives the [Message]
     * @return [Message]: if the [Message] is not empty nor blank, [Unit] else
     */
    fun sendMessage(body: String, recipient: User) : Message? {
        if(body.isNotBlank() && body.isNotEmpty()) {
            val message = Message(mUserManager.getCurrentUser(), recipient, body)
            message.saveEventually()
            mAdapterManager.processOutgoingMessage(message)
            return message
        }
        return null
    }

    /**
     * Listens for new messages for chat[partner] and adds it to [chatlog] as [ChatFromItem].
     * @param partner which the current [User] subscribed to
     * @param chatLog is a [RecyclerView] which is used for adding the new [Message] to it
     */
    fun subscribeToPartner(partner: User, chatLog: RecyclerView) {
        val adapter: GroupAdapter<GroupieViewHolder> = chatLog.adapter as GroupAdapter<GroupieViewHolder>
        val mutableList: MutableList<Message> = ArrayList()
        val parseQuery = ParseQuery.getQuery(Message::class.java)
        parseQuery.whereContains("threadId", partner.objectId)
        parseQuery.whereEqualTo("senderId", partner.objectId)
        parseQuery.orderByAscending("timestamp")
        val subscriptionHandling: SubscriptionHandling<Message> = parseLiveQueryClient.subscribe(parseQuery)

        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE) { _, message ->
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                mutableList.add(message)
                adapter.add(
                    ChatFromItem(
                        message,
                        partner
                    )
                )
                adapter.notifyDataSetChanged()
                chatLog.smoothScrollToPosition(adapter.itemCount - 1)
                //message.pinInBackground()
                showNotification(message, chatLog.context)
            }
        }
    }

    /**
     * Builds and shows a systemnotification if the current user has received a new [Message].
     */
    private fun showNotification(message: Message, context: Context) {
        val mNotificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
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
     * Getter for the latest [Message]
     * @param user from whom we will get the latest [Message]
     * @return [Message]
     */
    fun getLatestMessage(user: User): Message {
        try {
            val query = ParseQuery.getQuery(Message::class.java)
            //query.fromLocalDatastore()
            query.whereContains("threadId", user.objectId)
            query.limit = 1
            query.orderByDescending("timestamp")
            return query.first
        }
        catch (e: ParseException) {
            Log.d("EXCEPTION", e.localizedMessage)
        }
        return Message()
    }

    /**
     * Fills [chatLog] with all messages for a given chat[partner]
     */
    @Suppress("UNCHECKED_CAST")
    fun getMessagesByPartner(partner: User, chatLog: RecyclerView) {
        val adapter: GroupAdapter<GroupieViewHolder> = chatLog.adapter as GroupAdapter<GroupieViewHolder>
        val mutableList: MutableList<Message> = ArrayList()
        val query = ParseQuery.getQuery(Message::class.java)
        query.whereContains("threadId", partner.objectId)
        query.orderByAscending("timestamp")
        //query.fromLocalDatastore()
        query.findInBackground { messages, e ->
            if (e == null) {
                Log.d("CUSTOMDEBUG", "$simpleClassName - Got ${messages.size} " +
                        "messages in conversation with ${partner.username}.")
                mutableList.addAll(messages)
                //ParseObject.pinAllInBackground(messages)
                for(message in mutableList) {
                    if (message.sender.objectId == partner.objectId)
                        adapter.add(
                            ChatFromItem(
                                message,
                                partner
                            )
                        )
                    else adapter.add(
                        ChatSelfItem(
                            message
                        )
                    )
                }
                adapter.notifyDataSetChanged()
            }
        }
    }
}