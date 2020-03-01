package de.ntbit.projectearlybird.ui

import android.content.Context
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

class ContactsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("CUSTOMDEBUG", "WOLOLO")
        val contacts: ArrayList<String> = ArrayList()
        contacts.add("martin")
        contacts.add("thomas")
        contacts.add("jonas")
        contacts.add("remus")
        contacts.add("manni")
        
        val view = inflater.inflate(R.layout.fragment_contacts, container, false)

        val recyclerViewContacts = view.findViewById<RecyclerView>(R.id.recyclerViewContacts)
        recyclerViewContacts.layoutManager = LinearLayoutManager(this.context)
        recyclerViewContacts.adapter = ContactAdapter(contacts, this.context!!)

        return view
    }
/*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

 */
}