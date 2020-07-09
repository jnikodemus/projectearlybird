package de.ntbit.projectearlybird.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.adapter.item.UserItem
import de.ntbit.projectearlybird.helper.ParcelContract
import de.ntbit.projectearlybird.manager.AdapterManager
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.manager.MessageManager
import de.ntbit.projectearlybird.manager.UserManager
import de.ntbit.projectearlybird.ui.activity.ChatActivity
import kotlinx.android.synthetic.main.fragment_conversations.*

class ConversationsFragment : Fragment() {

    private val mUserManager: UserManager = ManagerFactory.getUserManager()
    private val mMessageManager: MessageManager = ManagerFactory.getMessageManager()
    private val mAdapterManager: AdapterManager = ManagerFactory.getAdapterManager()
    private val adapter = mAdapterManager.getConversationsAdapter()

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
    }

    private fun connectAdapter() {
        frgmt_conversations_rv_latest_messages.adapter = adapter
    }

    private fun setClickListener() {
        adapter.setOnItemClickListener { item, view ->
            val userItem = item as UserItem
            val intent = Intent(view.context, ChatActivity::class.java)
            intent.putExtra(ParcelContract.USER_KEY, userItem.user)
            startActivity(intent)
        }
    }

}