package de.ntbit.projectearlybird.manager

//import de.ntbit.projectearlybird.adapter.ConversationsAdapter
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.parse.ParseQuery
import com.parse.livequery.ParseLiveQueryClient
import com.parse.livequery.SubscriptionHandling
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.adapter.item.UserItemLatestMessage
import de.ntbit.projectearlybird.model.Message
import de.ntbit.projectearlybird.model.User
import java.net.URI

/**
 * Provides adapter for the different Activities
 * @property simpleClassName holds the actual simple classname
 * @property parseLiveQueryClient holds an instance of [ParseLiveQueryClient]
 * @property mUserManager holds an instance of [UserManager]
 * @property conversationContacts holds a [HashSet]<[User]> of actual conversation contacts
 * @property conversationsAdapter holds a [GroupAdapter]<[GroupieViewHolder]> with the
 * actual conversations
 * @property isInitialized true if the adapter is initialized
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

    /**
     * Returns [conversationsAdapter], calls [readExistingConversations] and
     * [listenForNewConversation] if [isInitialized] is false. After that [isInitialized] is set
     * to true.
     * @return [GroupAdapter]<[GroupieViewHolder]>
     */
    fun getConversationsAdapter(): GroupAdapter<GroupieViewHolder> {
        if (!isInitialized) {
            readExistingConversations()
            listenForNewConversation()
            isInitialized = true
        }
        return conversationsAdapter
    }

    /**
     * Reads the existing messages of ParseDatabase, transforms them to [UserItemLatestMessage]
     * and adds these to [conversationsAdapter] and adds the corresponding [User]
     * to [conversationContacts]. Afterwards notifies [conversationsAdapter] is notified for the
     * changed dataset.
     */
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

    /**
     * Subscribes to a [ParseQuery] listening for new [Message] and calls [processIncomingMessage]
     */
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

    /**
     * Returns [ParseQuery] which filters all [User]s which sent to current user
     * or received messages by current user.
     * @return [ParseQuery]<[User]>
     */
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

    /**
     * NOT IMPLEMENTED YET
     */
    fun processOutgoingMessage(message: Message) {
        // TODO: implement code
        Log.d("CUSTOMDEBUG", "$simpleClassName - Processing outgoing message (STILL TODO)... " +
                "Recipient: ${message.recipient.username} - Body: \"${message.body}\"")
        //conversationsAdapter.add(UserItemLatestMessage(message.recipient))
        //conversationsAdapter.notifyDataSetChanged()
    }

    /**
     * Adds a new [UserItemLatestMessage] to [conversationsAdapter] or
     * updates the [UserItemLatestMessage] if the conversation is already present.
     */
    private fun processIncomingMessage(message: Message) {
        //Log.d("CUSTOMDEBUG", "$simpleClassName - Processing incoming message... " +
        //            "From: ${message.recipient.username} - Body: \"${message.body}\""
        //)
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