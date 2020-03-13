package de.ntbit.projectearlybird.manager

import com.parse.ParseACL
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import de.ntbit.projectearlybird.model.Message
import java.util.*
import java.util.logging.Logger
import kotlin.collections.ArrayList

class MessageManager {
    private val log = Logger.getLogger(this::class.java.simpleName)
    private val mUserManager: UserManager = ManagerFactory.getUserManager()

    /**
     * Sends a String as Message to [recipient] if [message] isNotEmpty() and [message] isNotBlank()
     */
    fun sendMessage(message: String, recipient: ParseUser) {
        if(message.isNotBlank() && message.isNotEmpty()) {
            /* TODO: Change to
             *  val entity = Message()
             *  and message.saveEventually()
             *  if Parse has fixed it
             * TODO: Change threadId to something more useful
             */
            val entity = ParseObject.create("Message")
            val sender = mUserManager.getCurrentUser().objectId
            val recipient = recipient.objectId
            val threadId = sender + recipient
            val now = System.currentTimeMillis()
            val acl = ParseACL()
            acl.setReadAccess(recipient, true)
            acl.setWriteAccess(sender, true)

            entity.put("sender", sender)
            entity.put("recipient", recipient)
            entity.put("threadId", threadId)
            entity.put("body", message)
            entity.put("timestamp", now)
            entity.put("ACL", acl)

            entity.saveEventually{}
        }
    }

    fun getMessagesByThreadId(threadId: String) : Collection<Message> {
        val query = ParseQuery.getQuery(Message::class.java)
        val allMessages = ArrayList<Message>()
        query.orderByDescending("timestamp")
        query.findInBackground { messages, e ->
            if(e == null){
                allMessages.addAll(messages)
            }
        }
        return allMessages
    }
}