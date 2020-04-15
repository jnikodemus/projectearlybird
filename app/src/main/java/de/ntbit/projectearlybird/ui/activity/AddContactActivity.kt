package de.ntbit.projectearlybird.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.parse.ParseQuery
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.R.*
import de.ntbit.projectearlybird.adapter.item.UserItem
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.model.User
import de.ntbit.projectearlybird.ui.fragment.ContactsFragment
import kotlinx.android.synthetic.main.activity_add_contact.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

/**
 * Activity for adding a new contact to the [User]
 *
 * @property adapter contains the contact [User] for the recyclerview
 * @property mUserManager private global [UserManager]
 */
class AddContactActivity : AppCompatActivity() {

    private val adapter = GroupAdapter<GroupieViewHolder>()
    private val mUserManager = ManagerFactory.getUserManager()

    /**
     * Calls [initialize]
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_add_contact)

        initialize()
    }

    /**
     * Initializes the Activity by calling [placeToolbar], [connectAdapter],
     * [setClickListeners] and [initializeSearchFunction]
     */
    private fun initialize() {
        placeToolbar()
        connectAdapter()
        setClickListeners()
        initializeSearchFunction()
    }

    /**
     * Sets the "toolbar-backbutton" setting its behavior to [onBackPressed].
     */
    override fun onSupportNavigateUp(): Boolean {
        super.onSupportNavigateUp()
        onBackPressed()
        return true
    }

    /**
     * Activates the toolbar for the activity, shows the backbutton
     * and fills it with information like titletext.
     */
    private fun placeToolbar() {
        val toolbar = act_add_contact_toolbar
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar_tv_root_title.text = getString(string.add_contact)
    }

    /**
     * Connects the [GroupAdapter] to the recyclerview.adapter.
     */
    private fun connectAdapter() {
        add_new_contact_rv.adapter = adapter
    }

    /**
     * Sets an onItemClickListener on every item in the [adapter]
     */
    private fun setClickListeners() {
        adapter.setOnItemClickListener { item, view ->
            val userItem = item as UserItem
            mUserManager.addContact(item.user)
            val intent = Intent(view.context, ChatActivity::class.java)
            intent.putExtra(ContactsFragment.USER_KEY, userItem.user)
            startActivity(intent)
            finish()
        }
    }

    /**
     * Controls the search bar behavior
     */
    private fun initializeSearchFunction() {
        // TODO: Check why adapter callback comes later if input is already cleared
        actAddNewContactEditTextSearch.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                //Log.d("CUSTOMDEBUG","afterTextChanged")
                if(!p0.isNullOrBlank() && p0.isNotEmpty())
                    adapter.notifyDataSetChanged()
                else adapter.clear()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
               // Log.d("CUSTOMDEBUG","beforeTextChanged")
                //Log.d("CUSTOMDEBUG", p0.toString())
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!p0.isNullOrBlank() && p0.isNotEmpty()) {
                    val query = ParseQuery.getQuery(User::class.java)
                    query.whereStartsWith("username", p0.toString().toLowerCase(Locale.ROOT))
                    query.findInBackground { users, e ->
                        adapter.clear()
                        if (e == null) {
                            users.remove(mUserManager.getCurrentUser())
                            for (user in users)
                                adapter.add(UserItem(user))
                        }
                    }
                }
            }
        })
    }
}
