package de.ntbit.projectearlybird.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.ui.activity.InformationThirdPartiesActivity
import de.ntbit.projectearlybird.ui.activity.InformationVersionActivity
import kotlinx.android.synthetic.main.fragment_info.*

class InfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        frgmt_info_cv_third_parties.setOnClickListener{
            val intent = Intent(activity, InformationThirdPartiesActivity::class.java)
            startActivity(intent)
        }

        frgmt_info_cv_version.setOnClickListener{
            val intent = Intent(activity, InformationVersionActivity::class.java)
            startActivity(intent)
        }
    }

}