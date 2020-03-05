package de.ntbit.projectearlybird.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.connection.ParseConnection
import kotlinx.android.synthetic.main.fragment_conversations.*

class ConversationsFragment : Fragment() {



    private val mParseManager = ParseConnection.getParseManager()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_conversations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonSendMessage.setOnClickListener{
            if(editTextMessage.text.isNotBlank() && editTextMessage.text.isNotEmpty())
            mParseManager?.sendMessage(editTextMessage.text.toString())
            editTextMessage.text.clear()
        }
    }
}