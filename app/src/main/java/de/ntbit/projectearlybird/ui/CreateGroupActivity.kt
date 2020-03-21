package de.ntbit.projectearlybird.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.manager.GroupManager
import de.ntbit.projectearlybird.manager.ManagerFactory
import kotlinx.android.synthetic.main.activity_create_group.*

class CreateGroupActivity : AppCompatActivity() {

    private val mUserManager: GroupManager = ManagerFactory.getGroupManager()
    private val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

        createTestLayout()

        crt_group_rv_contacts.adapter = adapter
    }

    private fun createTestLayout() {
        adapter.add(CheckedContactItem())
        adapter.add(CheckedContactItem())
        adapter.add(CheckedContactItem())
        adapter.add(CheckedContactItem())
        adapter.add(CheckedContactItem())
    }
}

class CheckedContactItem: Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        
    }

    override fun getLayout(): Int {
        return R.layout.row_crt_group_contact
    }

}
