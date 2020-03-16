package de.ntbit.projectearlybird.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.adapter.UserItem
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.manager.UserManager
import kotlinx.android.synthetic.main.fragment_contacts.*

class ContactsFragment : Fragment() {

    companion object{
        val USER_KEY = "USER_KEY"
    }

    private val mUserManager: UserManager = ManagerFactory.getUserManager()
    private val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contacts, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    private fun initialize() {
        connectAdapter()
        setClickListener()
        fetchAllParseUser()
    }

    private fun connectAdapter() {
        frgmt_contacts_rv_contacts.adapter = adapter
    }

    private fun setClickListener() {
        adapter.setOnItemClickListener { item, view ->
            val userItem = item as UserItem
            userItem.user.pin()
            val intent = Intent(view.context, ChatActivity::class.java)
            intent.putExtra(USER_KEY, userItem.user)
            startActivity(intent)
        }
    }

    private fun fetchAllParseUser() {
        for (user in mUserManager.getAllUsers())
            adapter.add(UserItem(user))
        adapter.notifyDataSetChanged()
    }
}