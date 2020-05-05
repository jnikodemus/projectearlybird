package de.ntbit.projectearlybird.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.helper.ParcelContract
import de.ntbit.projectearlybird.helper.SwipeToDeleteHelper
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.model.Group
import de.ntbit.projectearlybird.model.ModuleChecklist
import de.ntbit.projectearlybird.model.ModuleChecklistItem
import kotlinx.android.synthetic.main.activity_module_checklist.*
import kotlinx.android.synthetic.main.toolbar.*


class ModuleChecklistActivity : AppCompatActivity() {

    private val simpleClassName = this.javaClass.simpleName

    private val mModuleChecklistManager = ManagerFactory.getModuleChecklistManager()
    private val mUserManager = ManagerFactory.getUserManager()

    private lateinit var adapter: GroupAdapter<GroupieViewHolder>
    private lateinit var group: Group
    private lateinit var moduleChecklist: ModuleChecklist

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_module_checklist)
        initialize()
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onSupportNavigateUp()
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_plus_sign, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.frgmt_contacts_add -> {
                showCreateItemDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initialize() {
        Log.d("CUSTOMDEBUG", "$simpleClassName.initialize()")
        group = intent.getParcelableExtra(ParcelContract.GROUP_KEY)
        moduleChecklist = intent.getParcelableExtra(ParcelContract.MODULE_KEY)
        Log.d("CUSTOMDEBUG", "$simpleClassName.initialize() - Parcel got: ${group.name} and ${moduleChecklist.name}")
        try {
            adapter = mModuleChecklistManager.getAdapterByGroup(group)
        }
        catch (e: Exception) {
            Log.d("CUSTOMDEBUG", "$simpleClassName.initialize() - Catched Exception: ${e.message}")
        }
        Log.d("CUSTOMDEBUG", "$simpleClassName.initialize() - Adaptersize: ${adapter.groupCount}")
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteHelper(adapter))
        itemTouchHelper.attachToRecyclerView(act_module_checklist_rv_log)
        act_module_checklist_rv_log.adapter = adapter
        placeToolbar()
    }

    private fun placeToolbar() {
        val toolbar = act_module_checklist_toolbar
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar_tv_root_title.text = moduleChecklist.name
    }

    private fun setClicklisteners() {
    }

    private fun showCreateItemDialog() {
        val context = this
        val builder = AlertDialog.Builder(context)
        val view = layoutInflater.inflate(R.layout.dialog_add_checklist_item, null)

        val itemEditText = view.findViewById(R.id.dialog_add_checklist_item_et_itemname) as EditText

        builder.setView(view)
        // set up the ok button
        builder.setPositiveButton(android.R.string.ok) { dialog, p1 ->
            val newCategory = itemEditText.text
            var isValid = true
            if (newCategory.isBlank()) {
                isValid = false
            }
            if (isValid) {
                mModuleChecklistManager.addItem(ModuleChecklistItem(
                    itemEditText.text.toString(),
                    mUserManager.getCurrentUser(),
                    moduleChecklist))
            }
            if (isValid) {
                dialog.dismiss()
            }
        }
        builder.setNegativeButton(android.R.string.cancel) { dialog, p1 ->
            dialog.cancel()
        }
        builder.show()
    }
}
/*
class AddItemDialogFragment(adapter: GroupAdapter<GroupieViewHolder>) : DialogFragment() {

    private val simpleClassName = this.javaClass.simpleName

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            builder.setView(inflater.inflate(R.layout.dialog_add_checklist_item, null))
                .setPositiveButton(R.string.app_add
                ) { dialog, id ->
                    val editText = view?.findViewById<EditText>(R.id.dialog_add_checklist_item_et_itemname)
                    Log.d("CUSTOMDEBUG", "$simpleClassName - " +
                            "User added ${editText}")
                }
                .setNegativeButton(R.string.app_cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
 */
