package de.ntbit.projectearlybird.manager

import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.RecyclerView
import com.parse.ParsePush
import com.parse.ParseQuery
import com.parse.ParseUser
import com.parse.livequery.ParseLiveQueryClient
import com.parse.livequery.SubscriptionHandling
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.model.Message
import de.ntbit.projectearlybird.ui.ChatFromItem
import de.ntbit.projectearlybird.ui.ChatSelfItem
import java.net.URI
import java.util.logging.Logger


class MessageManager {
    private val log = Logger.getLogger(this::class.java.simpleName)
    private val parseLiveQueryClient: ParseLiveQueryClient =
        ParseLiveQueryClient.Factory.getClient(URI("wss://projectearlybird.back4app.io/"))

    /**
     * Sends a String as Message to [recipient] if [body] isNotEmpty() and [body] isNotBlank()
     */
    fun sendMessage(body: String, recipient: ParseUser) {
        if(body.isNotBlank() && body.isNotEmpty()) {
            val message = Message(ParseUser.getCurrentUser(), recipient, body)
            message.saveEventually()
        }
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
                adapter.add(ChatFromItem(message.body, partner))
                adapter.notifyDataSetChanged()
            }
        }
    }

    /**
     * Returns all messages as [Collection]<[Message]> for a given [threadId]
     */
    fun getMessagesByPartner(partner: ParseUser, chatLog: RecyclerView) {
        val adapter: GroupAdapter<GroupieViewHolder> = chatLog.adapter as GroupAdapter<GroupieViewHolder>
        val mutableList: MutableList<Message> = ArrayList()
        val query = ParseQuery.getQuery(Message::class.java)
        query.whereContains("threadId", partner.objectId)
        query.orderByAscending("timestamp")
        query.findInBackground { messages, e ->
            if (e == null) {
                mutableList.addAll(messages)
                for(message in mutableList) {
                    if(message.sender.objectId == partner.objectId)
                        adapter.add(ChatFromItem(message.body, partner))
                    else adapter.add(ChatSelfItem(message.body))
                }
                adapter.notifyDataSetChanged()
            }
        }
    }
}