package de.ntbit.projectearlybird.ui

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.adapter.GroupItem
import de.ntbit.projectearlybird.manager.ManagerFactory
import kotlinx.android.synthetic.main.activity_create_group.*
import kotlinx.android.synthetic.main.fragment_groups.*


class GroupsFragment : Fragment() {

    private val adapter = GroupAdapter<GroupieViewHolder>()
    private val mGroupManager = ManagerFactory.getGroupManager()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_groups, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClicklistener()
        frgmt_groups_rv_groups.adapter = adapter


        /*ALSO STOPPED HERE ... NO INTERNET*/
        for(group in mGroupManager.getGroupsOfCurrentUser()){
            adapter.add(GroupItem(group))
        }
    }

    private fun setClicklistener() {
        frgmt_groups_fab.setOnClickListener {
            //val animator: Animator = ViewAnimationUtils.createCircularReveal(this.view,0,0,0,0)
            val intent = Intent(this.context, CreateGroupActivity::class.java)
            //circularRevealActivity()
            startActivity(intent)
        }
    }

    private fun circularRevealActivity() {
        val view = this.view!!
        val fabHeight = 43
        val currentDips = calcDips(fabHeight)
        val x: Int = view.right - currentDips
        val y: Int = view.bottom - currentDips

        val finalRad : Float = view.width.coerceAtLeast((view.height * 1.25).toInt()).toFloat()

        val animator: Animator = ViewAnimationUtils.createCircularReveal(view, x, y, 0f, finalRad)

        animator.duration = 500
        view.visibility = View.VISIBLE
        animator.start()
    }

    private fun calcDips(dps: Int) : Int {
        val resources = this.resources
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dps.toFloat(), resources.displayMetrics).toInt()
    }

    fun createTestLayout(){
    }
}