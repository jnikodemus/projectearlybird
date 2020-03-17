package de.ntbit.projectearlybird.manager

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.parse.*
import com.parse.livequery.ParseLiveQueryClient
import com.parse.livequery.SubscriptionHandling
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.adapter.ChatFromItem
import de.ntbit.projectearlybird.adapter.ChatSelfItem
import de.ntbit.projectearlybird.model.Message
import java.net.URI
import java.util.logging.Logger


class MessageManager {
    private val log = Logger.getLogger(this::class.java.simpleName)
    private val parseLiveQueryClient: ParseLiveQueryClient =
        ParseLiveQueryClient.Factory.getClient(URI("wss://projectearlybird.back4app.io/"))

    /**
     * Sends a String as Message to [recipient] if [body] isNotEmpty() and [body] isNotBlank()
     */
    fun sendMessage(body: String, recipient: ParseUser) : Message? {
        if(body.isNotBlank() && body.isNotEmpty()) {
            val message = Message(ParseUser.getCurrentUser(), recipient, body)
            message.saveEventually()
            return message
        }
        return null
    }

    @Deprecated("Not used anywhere")
    private fun sendPushNotification(message: String, recipient: ParseUser) {
        val parsePush = ParsePush()
        parsePush.setMessage(message)
        parsePush.setChannel(recipient.objectId)
        parsePush.sendInBackground()
    }

    fun subscribeToPartner(partner: ParseUser, chatLog: RecyclerView) {
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
                adapter.add(ChatFromItem(message, partner))
                adapter.notifyDataSetChanged()
                chatLog.smoothScrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    /**
     * Returns the latest message received from given [user]
     */
    fun getLatestMessage(user: ParseUser): Message {
        user.fetchFromLocalDatastore()
        try {
            val query = ParseQuery.getQuery(Message::class.java)
            //query.fromLocalDatastore()
            query.whereContains("threadId", user.objectId)
            query.limit = 1
            query.orderByDescending("timestamp")
            return query.first
        }
        catch (e: ParseException) {
            Log.d("EXEPTION", e.localizedMessage)
        }
        return Message()
    }

    /**
     * Returns all messages as [Collection]<[Message]> for a given [threadId]
     */
    fun getMessagesByPartner(partner: ParseUser, chatLog: RecyclerView) {
        partner.fetchFromLocalDatastore()
        val adapter: GroupAdapter<GroupieViewHolder> = chatLog.adapter as GroupAdapter<GroupieViewHolder>
        val mutableList: MutableList<Message> = ArrayList()
        val query = ParseQuery.getQuery(Message::class.java)
        query.whereContains("threadId", partner.objectId)
        query.orderByAscending("timestamp")
        query.findInBackground { messages, e ->
            if (e == null) {
                mutableList.addAll(messages)
                for(message in mutableList) {
                    message.pinInBackground()
                    if(message.sender.objectId == partner.objectId)
                        adapter.add(ChatFromItem(message, partner))
                    else adapter.add(ChatSelfItem(message))
                }
                adapter.notifyDataSetChanged()
            }
        }
    }
}