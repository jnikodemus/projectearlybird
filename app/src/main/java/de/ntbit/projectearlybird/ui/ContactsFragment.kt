package de.ntbit.projectearlybird.ui

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.frgmt_contacts_add, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.frgmt_contacts_add -> {
                val intent = Intent(this.context, AddNewContactActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
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
        frgmt_contacts_fab.setOnClickListener {
            showUserSearchDialog()
        }
    }

    private fun fetchAllParseUser() {
        for (user in mUserManager.getAllUsers())
            adapter.add(UserItem(user))
        adapter.notifyDataSetChanged()
    }

    private fun showUserSearchDialog() {
    }
}