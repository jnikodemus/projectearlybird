package de.ntbit.projectearlybird.ui

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import androidx.fragment.app.Fragment
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.adapter.item.GroupItem
import de.ntbit.projectearlybird.helper.ParcelContract
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.ui.activity.GroupCreateActivity
import de.ntbit.projectearlybird.ui.activity.GroupActivity
import kotlinx.android.synthetic.main.fragment_groups.*


class GroupsFragment : Fragment() {

    private val mGroupManager = ManagerFactory.getGroupManager()
    private val adapter = mGroupManager.getAdapter()

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
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        this.activity!!.menuInflater.inflate(R.menu.group_context_menu, menu)
    }

    private fun setClicklistener() {
        frgmt_groups_fab.setOnClickListener {
            //val animator: Animator = ViewAnimationUtils.createCircularReveal(this.view,0,0,0,0)
            val intent = Intent(this.context, GroupCreateActivity::class.java)
            //circularRevealActivity()
            startActivity(intent)
        }
        adapter.setOnItemClickListener { item, view ->
            val groupItem = item as GroupItem
            val intent = Intent(view.context, GroupActivity::class.java)
            intent.putExtra(ParcelContract.GROUP_KEY, groupItem.group)
            intent.putExtra(ParcelContract.GROUP_ADAPTER_POSITION_KEY, adapter.getAdapterPosition(groupItem))
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

}