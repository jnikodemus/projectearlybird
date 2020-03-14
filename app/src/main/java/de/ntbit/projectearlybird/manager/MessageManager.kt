package de.ntbit.projectearlybird.manager

import android.util.Log
import com.parse.ParsePush
import com.parse.ParseQuery
import com.parse.ParseUser
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.model.Message
import de.ntbit.projectearlybird.ui.ChatFromItem
import java.util.logging.Logger
import kotlin.collections.ArrayList

class MessageManager {
    private val log = Logger.getLogger(this::class.java.simpleName)

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
        val query = ParseQuery.getQuery(Message::class.java)
        query.orderByAscending("timestamp")
        query.whereContains("threadId", partner.objectId)
        query.findInBackground { messages, e ->
            if (e == null) {
                mutableList.addAll(messages)
                for(message in mutableList) {
                    adapter.add(ChatFromItem(message.body))
                }
                adapter.notifyDataSetChanged()
            }
        }
    }

    fun getAllMessages(adapter: GroupAdapter<GroupieViewHolder>) {
        val mutableList: MutableList<Message> = ArrayList()
        val query = ParseQuery.getQuery(Message::class.java)
        query.orderByAscending("timestamp")
        query.findInBackground { messages, e ->
            if (e == null) {
                mutableList.addAll(messages)
                for(message in mutableList) {
                    adapter.add(ChatFromItem(message.body))
                }
                adapter.notifyDataSetChanged()
            }
        }
    }
}