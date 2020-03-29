package de.ntbit.projectearlybird.ui

import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.adapter.ModuleItem
import de.ntbit.projectearlybird.helper.PixelCalculator
import de.ntbit.projectearlybird.model.Group
import de.ntbit.projectearlybird.model.Module
import kotlinx.android.synthetic.main.activity_create_group.*
import kotlinx.android.synthetic.main.activity_group.*
import kotlinx.android.synthetic.main.square_group_module.*

class GroupActivity : AppCompatActivity() {

    private lateinit var group: Group
    private val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        initialize()
    }

    private fun initialize() {
        group = intent.getParcelableExtra(CreateGroupActivity.GROUP_KEY)
        act_group_rv_modules.adapter = adapter
        act_group_rv_modules.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        placeToolbar()
        setGroupImage()
        dummyLayout()
    }

    fun dummyLayout(){
        adapter.add(ModuleItem(Module("Checklist")))
        adapter.add(ModuleItem(Module("Chat")))
    }

    private fun placeToolbar() {
        val toolbar = actGroupToolbar
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.title = group.name
    }

    private fun setGroupImage() {
        actGroupIvImage.layoutParams.height = PixelCalculator.calculateHeightForFullHD()
        var uri = group.groupImage.url
        if(group.croppedImage != null)
            uri = group.croppedImage!!.url
        Picasso.get()
            .load(uri)
            .fit()
            .centerCrop()
            .into(actGroupIvImage)
    }
}
