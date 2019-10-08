package de.ntbit.projectearlybird.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import de.ntbit.projectearlybird.R
import kotlinx.android.synthetic.main.fragment_conversations.*

class ConversationsFragment : Fragment() {

    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val constraintLayout = inflater.inflate(R.layout.fragment_conversations, container, false)
        val recyclerView = frmtConversationsRecyclerView
        linearLayoutManager = LinearLayoutManager(this.context)
        recyclerView.layoutManager = linearLayoutManager
        Log.d("CUSTOMDEBUG", "" + (recyclerView == null))
        return constraintLayout
    }
}