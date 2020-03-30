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

    private val parseLiveQueryClient: ParseLiveQueryClient =
        ParseLiveQueryClient.Factory.getClient(URI("wss://projectearlybird.back4app.io/"))

    private val mUserManager = ManagerFactory.getUserManager()
    private val conversationsAdapter = GroupAdapter<GroupieViewHolder>()
    private var isInitialized = false

    private fun readExistingConversations() {
        Log.d("CUSTOMDEBUG", "AdapterManager - readExistingConversations()")
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
                Log.d("CUSTOMDEBUG","ERROR - ${e.message}")
            }
        }
    }

    private fun listenForNewConversation() {
        Log.d("CUSTOMDEBUG", "AdapterManager - listenForNewConversation()")

        /*val subscriptionHandling: SubscriptionHandling<User> = parseLiveQueryClient.subscribe(getUserQuery())

        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE) { _, contact ->
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                Log.d("CUSTOMDEBUG", "AdapterManager - NEW MESSAGE TRIGGERED")
                conversationsAdapter.add(UserItemLatestMessage(contact))
                conversationsAdapter.notifyDataSetChanged()
            }
        }
         */
        val parseQuery = ParseQuery.getQuery(Message::class.java)
        parseQuery.whereContains("threadId", mUserManager.getCurrentUser().objectId)
        parseQuery.whereNotEqualTo("senderId", mUserManager.getCurrentUser().objectId)
        val subscriptionHandling: SubscriptionHandling<Message> = parseLiveQueryClient.subscribe(parseQuery)

        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE) { _, message ->
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                Log.d("CUSTOMDEBUG", "AdapterManager - NEW MESSAGE TRIGGERED")
                conversationsAdapter.add(UserItemLatestMessage(message.sender))
                conversationsAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun getUserQuery(): ParseQuery<User> {
        Log.d("CUSTOMDEBUG", "AdapterManager - getUserQuery()")
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
}