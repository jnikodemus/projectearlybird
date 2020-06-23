package de.ntbit.projectearlybird.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.ntbit.projectearlybird.R
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
        val majorVersion = context?.getString(R.string.app_major_version)
        val subVersion = context?.getString(R.string.app_sub_version)
        val versionTemplate = context?.getString(R.string.app_version_template)
        versionTemplate?.replace("MAJOR", majorVersion.toString())
        versionTemplate?.replace("SUB", subVersion.toString())
        //frgmt_info_tv_version.text = "Version $majorVersion.$subVersion"
    }
}