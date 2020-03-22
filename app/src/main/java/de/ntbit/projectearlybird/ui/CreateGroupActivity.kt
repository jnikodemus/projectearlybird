package de.ntbit.projectearlybird.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.adapter.UserItem
import de.ntbit.projectearlybird.manager.GroupManager
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.manager.UserManager
import kotlinx.android.synthetic.main.activity_create_group.*

class CreateGroupActivity : AppCompatActivity() {

    private val mGroupManager: GroupManager = ManagerFactory.getGroupManager()
    private val mUserManager: UserManager = ManagerFactory.getUserManager()
    private val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

        fetchAllParseUser()

        crt_group_rv_contacts.adapter = adapter
    }

    private fun fetchAllParseUser() {
        adapter.clear()
        for(contact in mUserManager.getMyContacts()) {
            adapter.add(UserItem(contact))
        }
        adapter.notifyDataSetChanged()
    }

}

class CheckedContactItem: Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        
    }

    override fun getLayout(): Int {
        return R.layout.row_crt_group_contact
    }

}
