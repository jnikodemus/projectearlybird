package de.ntbit.projectearlybird.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.parse.ParseUser
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.adapter.ContactAdapter
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.manager.UserManager
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.fragment_contacts.*
import kotlinx.android.synthetic.main.new_message_user_row.view.*

class ContactsFragment : Fragment() {

    companion object{
        val USER_KEY = "USER_KEY"
    }

    private val mUserManager: UserManager = ManagerFactory.getUserManager()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contacts, container, false)

        // TODO: Make observable?
        fetchAllParseUser()
        /*
        val recyclerViewContacts = view.findViewById<RecyclerView>(R.id.recyclerViewContacts)
        recyclerViewContacts.layoutManager = LinearLayoutManager(this.context)
        allUsers = mUserManager.getAllUserNames()
        recyclerViewContacts.adapter = ContactAdapter(allUsers, this.context!!)
         */
        return view
    }

    private fun fetchAllParseUser() {
        val adapter = GroupAdapter<GroupieViewHolder>()
        for (user in mUserManager.getAllUsers())
            adapter.add(UserItem(user))
        /* TODO: send selected UserItem/User to intent */
        adapter.setOnItemClickListener { item, view ->
            val userItem = item as UserItem
            val intent = Intent(view.context, ChatActivity::class.java)
            intent.putExtra(USER_KEY, userItem.user)
            startActivity(intent)
        }
        frgmt_contacts_rv_contacts.adapter = adapter
    }
}

class UserItem(val user: ParseUser): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_new_message.text = user.username
        /*Bilder zu den usernames*/
    }

    override fun getLayout(): Int {
        return R.layout.new_message_user_row
    }
}