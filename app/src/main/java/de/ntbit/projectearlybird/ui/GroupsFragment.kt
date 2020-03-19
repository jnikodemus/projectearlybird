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
import kotlinx.android.synthetic.main.fragment_groups.*


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
        frgmt_groups_rv_groups.adapter = adapter
        createTestLayout()
    }

    fun createTestLayout(){
        adapter.add(GroupItem())
        adapter.add(GroupItem())
        adapter.add(GroupItem())
        adapter.add(GroupItem())
        adapter.add(GroupItem())
        adapter.add(GroupItem())
        adapter.add(GroupItem())
        adapter.add(GroupItem())
        adapter.add(GroupItem())
        adapter.add(GroupItem())
        adapter.add(GroupItem())
        adapter.add(GroupItem())
        adapter.add(GroupItem())
        adapter.add(GroupItem())
        adapter.add(GroupItem())
        adapter.add(GroupItem())
    }
}