package de.ntbit.projectearlybird.manager

import android.util.Log
import com.parse.*
import de.ntbit.projectearlybird.model.Message
import java.util.*
import java.util.logging.Logger
import kotlin.collections.ArrayList

class MessageManager {
    private val log = Logger.getLogger(this::class.java.simpleName)

    /**
     * Sends a String as Message to [recipient] if [message] isNotEmpty() and [message] isNotBlank()
     */
    fun sendMessage(message: String, recipientUser: ParseUser) {
        if(message.isNotBlank() && message.isNotEmpty()) {
            /* TODO: Change to
             *  val entity = Message() and message.saveEventually()
             *  if Parse has fixed it
             * TODO: Change threadId to something more useful
             */
            val entity = ParseObject.create("Message")
            val sender = ParseUser.getCurrentUser().objectId
            val recipient = recipientUser.objectId
            val threadId = sender + recipient
            val now = Date(System.currentTimeMillis())
            val acl = ParseACL()
            acl.setReadAccess(recipient, true)
            acl.setWriteAccess(sender, true)

            entity.put("sender", sender)
            entity.put("recipient", recipient)
            entity.put("threadId", threadId)
            entity.put("body", message)
            entity.put("timestamp", now)
            entity.put("ACL", acl)

            entity.saveEventually {}

            //val mess = Message(sender,recipient,threadId,message,now)
            //mess.print()
            //mess.saveEventually{ e -> println("CUSTOMDEBUG: MESSAGE_Exception $e") }
            val parsePush = ParsePush()
            parsePush.setMessage(message)
            parsePush.setChannel(recipient)
            parsePush.sendInBackground()
        }
    }

    /**
     * Returns all Messages as [Collection]<[Message]> for a given [threadId]
     */
    fun getMessagesByThreadId(threadId: String) : Collection<Message> {
        val query = ParseQuery.getQuery(Message::class.java)
        val allMessages = ArrayList<Message>()
        query.orderByDescending("timestamp")
        query.findInBackground { messages, e ->
            if(e == null){
                allMessages.addAll(messages)
                Log.d("CUSTOM", "got " + allMessages.size)
            }
        }
        return allMessages
    }

    fun getAllMessages() : MutableList<Message> {
        val mutableList: MutableList<Message> = ArrayList()
        val query = ParseQuery.getQuery(Message::class.java)
        query.orderByDescending("timestamp")
        query.findInBackground { messages, e ->
            if(e == null){
                mutableList.addAll(messages)
                Log.d("CUSTOM", "got " + mutableList.size)
            }
        }

        return mutableList
    }
}