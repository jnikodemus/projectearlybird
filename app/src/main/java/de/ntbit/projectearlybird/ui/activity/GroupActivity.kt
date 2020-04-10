package de.ntbit.projectearlybird.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.parse.ParseFile
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.adapter.item.ModuleChecklistItem
import de.ntbit.projectearlybird.adapter.item.ModuleItem
import de.ntbit.projectearlybird.helper.PixelCalculator
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.model.Group
import de.ntbit.projectearlybird.model.Module
import de.ntbit.projectearlybird.model.ModuleChecklist
import kotlinx.android.synthetic.main.activity_group.*
import kotlinx.android.synthetic.main.toolbar.*


class GroupActivity : AppCompatActivity() {

    private val mGroupManager = ManagerFactory.getGroupManager()
    private lateinit var group: Group
    private val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)
        try {
            initialize()
        }
        catch(e: Exception) {
            Log.d("CUSTOMDEBUG", "GroupActivity - ${e.message}")
        }
    }

/*

    private var file: ParseFile? = null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (!file!!.isDirty) {
            outState.putParcelable("file", file)
        }
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            file = savedInstanceState.getParcelable<Parcelable>("file") as ParseFile
        }
    }

 */

    override fun onSupportNavigateUp(): Boolean {
        super.onSupportNavigateUp()
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.group_context_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.group_context_menu_leave -> {
                if(mGroupManager.leaveGroup(group)) {
                    finish()
                    return true
                }
                return false
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun initialize() {
        group = mGroupManager.getGroupById(intent.getStringExtra(CreateGroupActivity.GROUP_KEY))
        act_group_rv_modules.adapter = adapter
        act_group_rv_modules.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        placeToolbar()
        setGroupImage()
        dummyLayout()
    }

    private fun placeToolbar() {
        val toolbar = actGroupToolbar
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar_tv_root_title.text = group.name
    }

    fun dummyLayout(){
        //val bla = group.modules // Leere Liste mit Modules
        //Log.d("CUSTOMDEBUG", "GroupActivity - blaList: ${bla.size}; modulesList: ${group.modules.size}")
        //adapter.add(ModuleItem(Module("Checklist")))
        //adapter.add(ModuleItem(Module("Chat")))


        val itemsList = ArrayList<ModuleChecklistItem>()

        val moduleChecklist = ModuleChecklist(itemsList)

        group.addModule(moduleChecklist)

        //Log.d("CUSTOMDEBUG", "GroupActivity - blaList: ${bla.size}; modulesList: ${group.modules.size}")

        try {
            //group.saveEventually()
            Log.d("CUSTOMDEBUG", "GroupActivity - ${group.objectId}")
        }
        catch(e: Exception) {
            Log.d("CUSTOMDEBUG", "GroupActivity - ${e.message}")
        }

        adapter.setOnItemClickListener { item, view ->
            val moduleItem = item as ModuleItem
            val intent: Intent
            when(moduleItem.name) {
                "Checklist" -> {
                    intent = Intent(this, ModuleChecklistActivity::class.java)
                    lateinit var module: Module
                    for(groupModule in group.modules) {
                        if (groupModule.name == "Checklist")
                            module = groupModule as ModuleChecklist
                        intent.putExtra("MODULE", module)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun setGroupImage() {
        actGroupIvImage.layoutParams.height = PixelCalculator.calculateHeightForFullHD()
        var uri = group.groupImage.url
        //if(group.croppedImage != null)
        //    uri = group.croppedImage!!.url
        Picasso.get()
            .load(uri)
            .fit()
            .centerCrop()
            .into(actGroupIvImage)
    }
}
