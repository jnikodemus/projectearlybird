package de.ntbit.projectearlybird.manager

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.parse.ParseQuery
import com.parse.livequery.ParseLiveQueryClient
import com.parse.livequery.SubscriptionHandling
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.adapter.UserItemLatestMessage
import de.ntbit.projectearlybird.model.Message
import de.ntbit.projectearlybird.model.User
import java.net.URI

class AdapterManager {

    private val simpleClassName = this.javaClass.simpleName
    private val parseLiveQueryClient: ParseLiveQueryClient =
        ParseLiveQueryClient.Factory.getClient(URI("wss://projectearlybird.back4app.io/"))

    private val mUserManager = ManagerFactory.getUserManager()
    private val conversationsAdapter = GroupAdapter<GroupieViewHolder>()
    private var isInitialized = false

    private fun readExistingConversations() {
        Log.d("CUSTOMDEBUG", "$simpleClassName - readExistingConversations()")
        getUserQuery().findInBackground {
                convContacts, e ->
            if(e == null) {
                convContacts.remove(mUserManager.getCurrentUser())
                for(contact in convContacts) {
                    conversationsAdapter.add(UserItemLatestMessage(contact))
                    conversationsAdapter.notifyDataSetChanged()
                }
            }
            else {
                Log.d("CUSTOMDEBUG","$simpleClassName - ERROR - ${e.message}")
            }
        }
    }

    private fun listenForNewConversation() {
        Log.d("CUSTOMDEBUG", "$simpleClassName - listenForNewConversation()")

        val parseQuery = ParseQuery.getQuery(Message::class.java)
        parseQuery.whereContains("threadId", mUserManager.getCurrentUser().objectId)
        parseQuery.whereNotEqualTo("senderId", mUserManager.getCurrentUser().objectId)
        val subscriptionHandling: SubscriptionHandling<Message> = parseLiveQueryClient.subscribe(parseQuery)

        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE) { _, message ->
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                Log.d("CUSTOMDEBUG", "$simpleClassName - Processing incoming message... " +
                        "From: ${message.recipient.username} - Body: \"${message.body}\"")
                conversationsAdapter.add(UserItemLatestMessage(message.sender))
                conversationsAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun getUserQuery(): ParseQuery<User> {
        Log.d("CUSTOMDEBUG", "$simpleClassName - getUserQuery()")
        val messageQuery = ParseQuery
            .getQuery(Message::class.java)
            .whereContains("threadId", mUserManager.getCurrentUser().objectId)
        val userQuerySender = ParseQuery
            .getQuery(User::class.java)
            .whereMatchesKeyInQuery("objectId", "senderId", messageQuery)
        val userQueryRecipient = ParseQuery
            .getQuery(User::class.java)
            .whereMatchesKeyInQuery("objectId", "recipientId", messageQuery)

        val queries: ArrayList<ParseQuery<User>> = ArrayList()
        queries.add(userQueryRecipient)
        queries.add(userQuerySender)

        return ParseQuery.or(queries)
    }

    fun getConversationsAdapter(): GroupAdapter<GroupieViewHolder> {
        if(!isInitialized) {
            readExistingConversations()
            listenForNewConversation()
            isInitialized = true
        }
        return conversationsAdapter
    }

    fun processOutgoingMessage(message: Message) {
        // TODO: implement code
        Log.d("CUSTOMDEBUG", "$simpleClassName - Processing outgoing message (STILL TODO)... " +
                "Recipient: ${message.recipient.username} - Body: \"${message.body}\"")
        //conversationsAdapter.add(UserItemLatestMessage(message.recipient))
        //conversationsAdapter.notifyDataSetChanged()
    }
}