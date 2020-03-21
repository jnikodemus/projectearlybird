package de.ntbit.projectearlybird.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.adapter.UserItem
import de.ntbit.projectearlybird.adapter.UserItemLatestMessage
import de.ntbit.projectearlybird.connection.ParseConnection
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.manager.MessageManager
import de.ntbit.projectearlybird.manager.UserManager
import kotlinx.android.synthetic.main.fragment_contacts.*
import kotlinx.android.synthetic.main.fragment_conversations.*

class ConversationsFragment : Fragment() {

    private val mUserManager: UserManager = ManagerFactory.getUserManager()
    private val mMessageManager: MessageManager = ManagerFactory.getMessageManager()
    private val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_conversations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    private fun initialize() {
        connectAdapter()
        setClickListener()
        val users = mUserManager.getAllUsers()
    }

    private fun connectAdapter() {
        frgmt_conversations_rv_latest_messages.adapter = adapter
        for(user in mUserManager.getAllUsers()) {
            adapter.add(UserItemLatestMessage(user))
        }
    }

    private fun setClickListener() {
        adapter.setOnItemClickListener { item, view ->
            val userItem = item as UserItem
            //userItem.user.pin()
            val intent = Intent(view.context, ChatActivity::class.java)
            intent.putExtra(ContactsFragment.USER_KEY, userItem.user)
            startActivity(intent)
        }
    }
}