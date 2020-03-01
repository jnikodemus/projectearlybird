package de.ntbit.projectearlybird.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.adapter.ContactAdapter
import de.ntbit.projectearlybird.connection.ParseConnection
import de.ntbit.projectearlybird.manager.ParseManager
import kotlinx.android.synthetic.main.fragment_contacts.*

class ContactsFragment : Fragment() {

    private val parseManager: ParseManager? = ParseConnection.getParseManager()
    lateinit var allUsers: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contacts, container, false)

        // TODO: Make observable?
        val recyclerViewContacts = view.findViewById<RecyclerView>(R.id.recyclerViewContacts)
        recyclerViewContacts.layoutManager = LinearLayoutManager(this.context)
        if (parseManager != null) {
            allUsers = parseManager.getAllUserNames()
            recyclerViewContacts.adapter = ContactAdapter(allUsers, this.context!!)
        }

        return view
    }
}