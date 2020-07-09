package de.ntbit.projectearlybird.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.adapter.item.UserItem
import de.ntbit.projectearlybird.helper.ParcelContract
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.manager.UserManager
import de.ntbit.projectearlybird.ui.activity.AddContactActivity
import de.ntbit.projectearlybird.ui.activity.ChatActivity
import de.ntbit.projectearlybird.ui.activity.HomeActivity
import kotlinx.android.synthetic.main.fragment_contacts.*
import kotlinx.android.synthetic.main.toolbar.*

class ContactsFragment : Fragment() {

    private val mUserManager: UserManager = ManagerFactory.getUserManager()
    private val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contacts, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        fetchAllParseUser()
    }

    private fun initialize() {
        connectAdapter()
        fetchAllParseUser()
        setClickListener()
    }

    private fun connectAdapter() {
        frgmt_contacts_rv_contacts.adapter = adapter
    }

    private fun setClickListener() {
        adapter.setOnItemClickListener { item, view ->
            val userItem = item as UserItem
            //userItem.user.pin()
            val intent = Intent(view.context, ChatActivity::class.java)
            intent.putExtra(ParcelContract.USER_KEY, userItem.user)
            startActivity(intent)
        }
        frgmt_contacts_fab.setOnClickListener {
            showUserSearchActivity()
        }
    }

    private fun fetchAllParseUser() {
        adapter.clear()
        for(contact in mUserManager.getMyContacts()) {
            adapter.add(UserItem(contact))
        }
        adapter.notifyDataSetChanged()
    }

    private fun showUserSearchActivity() {
        val intent = Intent(this.context, AddContactActivity::class.java)
        startActivity(intent)
    }
}