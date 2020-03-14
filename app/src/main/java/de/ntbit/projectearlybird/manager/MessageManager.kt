package de.ntbit.projectearlybird.manager

import android.util.Log
import com.parse.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.model.Message
import de.ntbit.projectearlybird.ui.ChatFromItem
import java.util.*
import java.util.logging.Logger
import kotlin.collections.ArrayList

class MessageManager {
    private val log = Logger.getLogger(this::class.java.simpleName)

    //val mutableList: MutableList<Message> = ArrayList()

    /**
     * Sends a String as Message to [recipientUser] if [message] isNotEmpty() and [message] isNotBlank()
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
     * Returns all messages as [Collection]<[Message]> for a given [threadId]
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

    fun getAllMessages(adapter: GroupAdapter<GroupieViewHolder>) {
        val mutableList: MutableList<Message> = ArrayList()
        val query = ParseQuery.getQuery(Message::class.java)
        query.orderByAscending("timestamp")
        query.findInBackground { messages, e ->
            if (e == null) {
                mutableList.addAll(messages)
                for(message in mutableList)
                    adapter.add(ChatFromItem(message.getString("body")))
                adapter.notifyDataSetChanged()
            }
        }
    }
}