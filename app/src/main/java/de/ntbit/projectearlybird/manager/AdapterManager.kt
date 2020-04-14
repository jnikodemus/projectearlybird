package de.ntbit.projectearlybird.manager

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.parse.ParseQuery
import com.parse.livequery.ParseLiveQueryClient
import com.parse.livequery.SubscriptionHandling
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
//import de.ntbit.projectearlybird.adapter.ConversationsAdapter
import de.ntbit.projectearlybird.adapter.item.UserItemLatestMessage
import de.ntbit.projectearlybird.model.Message
import de.ntbit.projectearlybird.model.User
import java.net.URI
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

/**
 * Manager for controlling adapters from other views and to also get them in other activities
 * @property simpleClassName contains the classname
 * @property parseLiveQueryClient connection to the back4app livequery event
 * @property mUserManager global [UserManager]
 * @property conversationContacts collection for all conversation from the current [User]
 * @property conversationsAdapter ???
 * @property isInitialized ???
 */
class AdapterManager {

    //val theRealConversationsAdapter = ConversationsAdapter()

    private val simpleClassName = this.javaClass.simpleName
    private val parseLiveQueryClient: ParseLiveQueryClient =
        ParseLiveQueryClient.Factory.getClient(URI("wss://projectearlybird.back4app.io/"))

    private val mUserManager = ManagerFactory.getUserManager()

    //private val conversationObjects = HashMap<User, UserItemLatestMessage>()
    private val conversationContacts = HashSet<User>()
    private val conversationsAdapter = GroupAdapter<GroupieViewHolder>()

    private var isInitialized = false

    fun getConversationsAdapter(): GroupAdapter<GroupieViewHolder> {
        if (!isInitialized) {
            readExistingConversations()
            listenForNewConversation()
            isInitialized = true
        }
        return conversationsAdapter
    }

    private fun readExistingConversations() {
        Log.d("CUSTOMDEBUG", "$simpleClassName - readExistingConversations()")
        getUserQuery().orderByDescending("username").findInBackground {
                convContacts, e ->
            if(e == null) {
                convContacts.remove(mUserManager.getCurrentUser())
                for(contact in convContacts) {
                    val latestContact =
                        UserItemLatestMessage(
                            contact
                        )
                    conversationsAdapter.add(0, latestContact)
                    conversationContacts.add(contact)
                    //conversationObjects.put(latestContact.user,latestContact)
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
                processIncomingMessage(message)
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

    fun processOutgoingMessage(message: Message) {
        // TODO: implement code
        Log.d("CUSTOMDEBUG", "$simpleClassName - Processing outgoing message (STILL TODO)... " +
                "Recipient: ${message.recipient.username} - Body: \"${message.body}\"")
        //conversationsAdapter.add(UserItemLatestMessage(message.recipient))
        //conversationsAdapter.notifyDataSetChanged()
    }

    private fun processIncomingMessage(message: Message) {
        Log.d(
            "CUSTOMDEBUG", "$simpleClassName - Processing incoming message... " +
                    "From: ${message.recipient.username} - Body: \"${message.body}\""
        )

        val latestContact =
            UserItemLatestMessage(message.sender)
        if (!conversationContacts.contains(latestContact.user))
            conversationsAdapter.add(0, latestContact)
        conversationsAdapter.notifyDataSetChanged()
        // TODO: Implement notifyItemChanged()
        /*
        conversationsAdapter.notifyDataSetChanged()
        if(conversationObjects.containsKey(latestContact.user)) {
            val fromPos =
                conversationsAdapter.getAdapterPosition(conversationObjects[latestContact.user]!!)
            conversationsAdapter.notifyItemMoved(fromPos, 0)
            conversationObjects.remove(latestContact.user)
            conversationObjects.put(latestContact.user, latestContact)
            Log.d("CUSTOMDEBUG", "$simpleClassName - if")
        }
        else {
            conversationsAdapter.add(0,latestContact)
            conversationObjects.put(latestContact.user, latestContact)
            conversationsAdapter.notifyItemInserted(0)
            Log.d("CUSTOMDEBUG", "$simpleClassName - else")
        }
        // TODO: Check notifyItemRangeChanged()
    }
     */
    }
}