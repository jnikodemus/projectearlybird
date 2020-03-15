package de.ntbit.projectearlybird.manager

import android.R
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import com.parse.ParseObject
import com.parse.ParsePush
import com.parse.ParseQuery
import com.parse.ParseUser
import com.parse.livequery.ParseLiveQueryClient
import com.parse.livequery.SubscriptionHandling
import com.parse.livequery.SubscriptionHandling.HandleEventCallback
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.model.Message
import de.ntbit.projectearlybird.ui.ChatFromItem
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

            sendPushNotification(body, recipient)
        }
    }

    private fun sendPushNotification(message: String, recipient: ParseUser) {
        val parsePush = ParsePush()
        parsePush.setMessage(message)
        parsePush.setChannel(recipient.objectId)
        parsePush.sendInBackground()
    }

    /**
     * Returns all messages as [Collection]<[Message]> for a given [threadId]
     */
    fun getMessagesByPartner(partner: ParseUser, adapter: GroupAdapter<GroupieViewHolder>) {
        val mutableList: MutableList<Message> = ArrayList()
        val parseQuery = ParseQuery.getQuery(Message::class.java)
        parseQuery.whereContains("threadId", partner.objectId)
        parseQuery.orderByAscending("timestamp")
        val subscriptionHandling: SubscriptionHandling<Message> = parseLiveQueryClient.subscribe(parseQuery)

        subscriptionHandling.handleEvent(
            SubscriptionHandling.Event.CREATE,
            HandleEventCallback<Message> { query, message ->
                val handler = Handler(Looper.getMainLooper())
                handler.post(Runnable {
                    mutableList.add(message)
                    adapter.add(ChatFromItem(message.body, partner))
                    adapter.notifyDataSetChanged()
                })
            }
        )
/*
        query.findInBackground { messages, e ->
            if (e == null) {
                mutableList.addAll(messages)
                for(message in mutableList) {
                    adapter.add(ChatFromItem(message.body, partner))
                }
                adapter.notifyDataSetChanged()
            }
        } */
    }
}