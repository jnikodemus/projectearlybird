package de.ntbit.projectearlybird.ui.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.adapter.item.ModuleItem
import de.ntbit.projectearlybird.helper.ParcelContract
import de.ntbit.projectearlybird.helper.PixelCalculator
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.model.Group
import de.ntbit.projectearlybird.model.Module
import de.ntbit.projectearlybird.model.ModuleChecklist
import kotlinx.android.synthetic.main.activity_group.*
import kotlinx.android.synthetic.main.toolbar.*
import java.lang.Exception


class GroupActivity : AppCompatActivity() {

    private val simpleClassName = this.javaClass.simpleName

    private val mGroupManager = ManagerFactory.getGroupManager()
    private val mModuleManager = ManagerFactory.getModuleManager()
    private lateinit var group: Group
    private val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        initialize()
    }

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
                if(mGroupManager.leaveGroup(group, intent.getIntExtra(ParcelContract.GROUP_ADAPTER_POSITION_KEY, -1))) {
                    finish()
                    return true
                }
                return false
            }
            R.id.group_context_menu_add_module -> {
                Log.d("CUSTOMDEBUG", "$simpleClassName - AddModule clicked.")
                val dialog = AddModuleDialogFragment(group, adapter)
                dialog.show(this.supportFragmentManager, "DIALOG_GROUP_ACTIVITY_ADD_MODULE")
                for(module in mModuleManager.getModules())
                    Log.d("CUSTOMDEBUG", "$simpleClassName - ${module.name}, ${module.description}")
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun initialize() {
        group = intent.getParcelableExtra(ParcelContract.GROUP_KEY)
        saveGroup()
        connectAdapter()
        placeToolbar()
        setGroupImage()
        loadModules()
        setClicklistener()
    }

    private fun saveGroup() {
        mGroupManager.save(group)
    }

    private fun connectAdapter() {
        act_group_rv_modules.adapter = adapter
        act_group_rv_modules.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
    }

    private fun placeToolbar() {
        val toolbar = actGroupToolbar
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar_tv_root_title.text = group.name
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

    private fun loadModules() {
        for(m in group.modules) {
            m.fetchIfNeeded<Module>()
            Log.d("CUSTOMDEBUG", "$simpleClassName.loadModules() - ${m.name}, ${m.description}")
            adapter.add(ModuleItem(Module(m)))
        }
    }

    private fun setClicklistener() {
        adapter.setOnItemClickListener { item, view ->
            val moduleItem = item as ModuleItem
            Log.d("CUSTOMDEBUG", "$simpleClassName.OnClickListener() - ${moduleItem.name}")

            val intent: Intent
            when(moduleItem.name) {
                "Checklist" -> {
                    intent = Intent(this, ModuleChecklistActivity::class.java)
                    lateinit var module: Module
                    Log.d("CUSTOMDEBUG", "$simpleClassName.switch(Checklist) - before cast")
                    for(groupModule in group.modules) {
                        if (groupModule.name == "Checklist") {
                            Log.d("CUSTOMDEBUG", "$simpleClassName.OnClickListener() - trying cast to ModuleChecklist")
                            module = groupModule as ModuleChecklist
                        }
                        intent.putExtra(ParcelContract.GROUP_KEY, group)
                        intent.putExtra(ParcelContract.MODULE_KEY, module)
                        startActivity(intent)
                    }
                }
            }
        }
    }

}

class AddModuleDialogFragment(otherGroup: Group, adapter: GroupAdapter<GroupieViewHolder>) : DialogFragment() {

    private val simpleClassName = this.javaClass.simpleName

    private val moduleAdapter = adapter
    private val group = otherGroup
    private val mModuleManager = ManagerFactory.getModuleManager()
    val array = arrayOf(mModuleManager.getModules())

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Pick a module")
                .setItems(R.array.group_modules
                ) { dialog, which ->
                    Log.d("CUSTOMDEBUG", "$simpleClassName - User clicked $which")
                    when(which) {
                        0 -> {
                            Log.d(
                                "CUSTOMDEBUG",
                                "$simpleClassName - associatedGroup: " + group.name
                            )
                            val module = ModuleChecklist(ArrayList(), group)
                            if (!group.modules.contains(module)) {
                                module.saveEventually()
                                group.addModule(module)
                                group.saveEventually()
                                moduleAdapter.add(ModuleItem(Module(module)))
                                moduleAdapter.notifyDataSetChanged()
                            }
                        }
                        else -> Log.d("CUSTOMDEBUG", "$simpleClassName - " +
                                "$which not implemented yet.")
                    }
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}