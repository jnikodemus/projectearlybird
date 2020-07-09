package de.ntbit.projectearlybird.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.adapter.item.UserItem
import de.ntbit.projectearlybird.helper.ParcelContract
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.model.Group
import de.ntbit.projectearlybird.model.User
import kotlinx.android.synthetic.main.activity_group_settings.*
import kotlinx.android.synthetic.main.dialog_user_options.*
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
            val userItem = item as UserItem
            showUserOptionsDialog(userItem)
            true
        }
    }

    private fun showUserOptionsDialog(userItem: UserItem) {
        val user = userItem.user
        val context = this
        val builder = AlertDialog.Builder(context)
        val view = layoutInflater.inflate(R.layout.dialog_user_options, null)
        val dialogTitle = view.findViewById(R.id.dialog_user_options_tv_title) as TextView
        dialogTitle.text = user.username

        val radioButtonPromote = view.findViewById(R.id.dialog_user_options_rb_promote) as RadioButton
        //val radioButtonRemove = view.findViewById(R.id.dialog_user_options_rb_remove) as RadioButton

        builder.setView(view)
        // set up the ok button
        builder.setPositiveButton(android.R.string.ok) { dialog, p1 ->
            // check radioButton
            if(radioButtonPromote.isChecked) {
                mGroupManager.promote(user, group)
            }
            else {
                if(mGroupManager.removeUser(user, group)) {
                    placeInformation()
                    adapter.remove(userItem)
                    adapter.notifyDataSetChanged()
                }
            }
        }
        builder.setNegativeButton(android.R.string.cancel) { dialog, p1 ->
            dialog.cancel()
        }
        builder.show()
    }
}
