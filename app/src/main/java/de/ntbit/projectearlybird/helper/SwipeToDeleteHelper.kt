package de.ntbit.projectearlybird.helper

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.adapter.item.ChecklistItem
import de.ntbit.projectearlybird.manager.ManagerFactory

class SwipeToDeleteHelper : ItemTouchHelper.SimpleCallback {

    private val mModuleChecklistManager = ManagerFactory.getModuleChecklistManager()

    private val adapter: GroupAdapter<GroupieViewHolder>
    private var icon: Drawable
    private val backgroundColor: ColorDrawable

    internal constructor(adapter: GroupAdapter<GroupieViewHolder>) :
            super(0, ItemTouchHelper.LEFT) {
        this.adapter = adapter
        icon = ContextCompat.getDrawable(ApplicationContextProvider.context, R.drawable.ic_cancel_white_24dp)!!
        backgroundColor = ColorDrawable(Color.RED)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val checklistItemToDelete = this.adapter.getGroup(position) as ChecklistItem
        mModuleChecklistManager.deleteChecklistItem(checklistItemToDelete, true)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(
            c, recyclerView, viewHolder, dX,
            dY, actionState, isCurrentlyActive
        )
        val itemView: View = viewHolder.itemView
        val backgroundCornerOffset = 20

        val iconMargin = (itemView.height - icon.intrinsicHeight) / 2
        val iconTop =
            itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        val iconBottom = iconTop + icon.intrinsicHeight

        /*if (dX > 0) { // Swiping to the right
            val iconLeft = itemView.left + iconMargin + icon.intrinsicWidth
            val iconRight = itemView.left + iconMargin
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            backgroundColor.setBounds(
                itemView.left, itemView.top,
                itemView.left + dX.toInt() + backgroundCornerOffset,
                itemView.bottom
            )
        }
         else */
        if (dX < 0) { // Swiping to the left
            val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
            val iconRight = itemView.right - iconMargin
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            backgroundColor.setBounds(
                itemView.right + dX.toInt() - backgroundCornerOffset,
                itemView.top, itemView.right, itemView.bottom
            )
        }
        else { // view is unSwiped
            backgroundColor.setBounds(0, 0, 0, 0)
        }

        backgroundColor.draw(c)
        icon.draw(c)
    }


}