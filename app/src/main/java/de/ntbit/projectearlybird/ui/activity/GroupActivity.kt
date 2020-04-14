package de.ntbit.projectearlybird.ui.activity

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.parse.ParseFile
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.adapter.item.ModuleChecklistItem
import de.ntbit.projectearlybird.adapter.item.ModuleItem
import de.ntbit.projectearlybird.helper.ParcelContract
import de.ntbit.projectearlybird.helper.PixelCalculator
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.model.Group
import de.ntbit.projectearlybird.model.Module
import de.ntbit.projectearlybird.model.ModuleChecklist
import kotlinx.android.synthetic.main.activity_group.*
import kotlinx.android.synthetic.main.toolbar.*


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
                if(mGroupManager.leaveGroup(group)) {
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
            Log.d("CUSTOMDEBUG", "$simpleClassName - ${m.name}, ${m.description}")
            adapter.add(ModuleItem(Module(m)))
        }
    }

    private fun setClicklistener() {
        adapter.setOnItemClickListener { item, view ->
            val moduleItem = item as ModuleItem
            Log.d("CUSTOMDEBUG", "$simpleClassName - ${moduleItem.name}")

            /*
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
             */
        }
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

        group.saveEventually {
            if(it == null)
                Log.d("CUSTOMDEBUG", "$simpleClassName - success on group.saveEventually")

            else Log.d("CUSTOMDEBUG", "$simpleClassName - ${it.message}")

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
                .setItems(R.array.group_modules,
                    DialogInterface.OnClickListener { dialog, which ->
                        Log.d("CUSTOMDEBUG", "$simpleClassName - User clicked $which")
                        when(which) {
                            0 -> {
                                val module = ModuleChecklist(ArrayList<ModuleChecklistItem>())
                                module.saveEventually()
                                group.addModule(module)
                                moduleAdapter.add(ModuleItem(Module(module)))
                                moduleAdapter.notifyDataSetChanged()
                                //group.addModule(ModuleChecklist())
                                //moduleAdapter.add(ModuleItem(ModuleChecklist()))
                                //moduleAdapter.notifyDataSetChanged()
                            }
                            else -> Log.d("CUSTOMDEBUG", "$simpleClassName - " +
                                    "$which not implemented yet.")
                        }
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}