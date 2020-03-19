package de.ntbit.projectearlybird.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.adapter.GroupItem
import kotlinx.android.synthetic.main.fragment_contacts.*


class GroupsFragment : Fragment() {

    private val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_groups, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createTestLayoutt()
        frgmt_contacts_rv_contacts.adapter = adapter
    }

    fun createTestLayoutt(){
        adapter.add(GroupItem())
        adapter.add(GroupItem())
    }
}