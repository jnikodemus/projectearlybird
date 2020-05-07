package de.ntbit.projectearlybird.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.adapter.item.UserItem
import de.ntbit.projectearlybird.helper.ParcelContract
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.model.Group
import de.ntbit.projectearlybird.model.ModuleChecklistItem
import de.ntbit.projectearlybird.model.User
import kotlinx.android.synthetic.main.activity_group_settings.*
import kotlinx.android.synthetic.main.toolbar.*

class GroupSettingsActivity : AppCompatActivity() {

    private val mGroupManager = ManagerFactory.getGroupManager()
    private val mUserManager = ManagerFactory.getUserManager()

    private lateinit var group: Group
    private val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_settings)

        initialize()
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onSupportNavigateUp()
        onBackPressed()
        return true
    }

    private fun initialize() {
        group = intent.getParcelableExtra(ParcelContract.GROUP_KEY)
        placeToolbar()
        placeInformation()
        placeAdapter()
        setClickListeners()
    }

    private fun placeToolbar() {
        val toolbar = act_group_settings_toolbar
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar_tv_root_title.text = group.name
    }

    private fun placeInformation() {
        act_group_settings_tv_num_member.text = group.getSize().toString()
        act_group_settings_tv_num_modules.text = group.getModuleCount().toString()
    }

    private fun placeAdapter() {
        act_group_settings_rv_member.adapter = adapter
        adapter.clear()
        for(member in group.members)
            adapter.add(UserItem(member as User))
        adapter.notifyDataSetChanged()
    }

    private fun setClickListeners() {
        if(mGroupManager.isAdmin(group))
        adapter.setOnItemLongClickListener { item, view ->
            Log.d("CUSTOMDEBUG", "Longclick received.")
            //val userItem = item as UserItem
            true
        }
    }

    private fun showRemoveUserDialog(userToRemove: User) {
        val context = this
        val builder = AlertDialog.Builder(context)
        val view = layoutInflater.inflate(R.layout.dialog_add_checklist_item, null)

        val itemEditText = view.findViewById(R.id.dialog_add_checklist_item_et_itemname) as EditText

        builder.setView(view)
        // set up the ok button
        builder.setPositiveButton(android.R.string.ok) { dialog, p1 ->
            // remove User
            //group.removeUser(userToRemove)
        }
        builder.setNegativeButton(android.R.string.cancel) { dialog, p1 ->
            dialog.cancel()
        }
        builder.show()
    }
}
