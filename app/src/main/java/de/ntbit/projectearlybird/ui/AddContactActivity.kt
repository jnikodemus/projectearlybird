package de.ntbit.projectearlybird.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.parse.ParseQuery
import com.parse.ParseUser
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.adapter.UserItem
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.model.User
import kotlinx.android.synthetic.main.activity_add_new_contact.*


class AddContactActivity : AppCompatActivity() {

    private val adapter = GroupAdapter<GroupieViewHolder>()
    private val mUserManager = ManagerFactory.getUserManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_contact)

        add_new_contact_rv.adapter = adapter


        adapter.setOnItemClickListener { item, view ->
            val userItem = item as UserItem
            mUserManager.addNewContact(item.user)
            val intent = Intent(view.context, ChatActivity::class.java)
            intent.putExtra(ContactsFragment.USER_KEY, userItem.user)
            startActivity(intent)
            finish()
        }


        // TODO: Check why adapter callback comes later if input is already cleared
        actAddNewContactEditTextSearch.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                Log.d("CUSTOMDEBUG","afterTextChanged")
                if(!p0.isNullOrBlank() && p0.isNotEmpty())
                    adapter.notifyDataSetChanged()
                else adapter.clear()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("CUSTOMDEBUG","beforeTextChanged")
                Log.d("CUSTOMDEBUG", p0.toString())
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("CUSTOMDEBUG","onTextChanged")
                if(!p0.isNullOrBlank() && p0.isNotEmpty()) {
                    val query = ParseQuery.getQuery(User::class.java)
                    query.whereStartsWith("username", p0.toString())
                    query.findInBackground { users, e ->
                        adapter.clear()
                        if (e == null) {
                            for (user in users)
                                adapter.add(UserItem(user))
                        }
                    }
                }
            }
        })
    }
}
