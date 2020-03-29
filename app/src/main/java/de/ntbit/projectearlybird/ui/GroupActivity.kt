package de.ntbit.projectearlybird.ui

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
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
        placeToolbar()
        setGroupImage()
    }

    fun dummyLayout(){
        adapter.add(ModuleItem(Module()))
        adapter.add(ModuleItem(Module()))
        adapter.add(ModuleItem(Module()))
        adapter.add(ModuleItem(Module()))

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
