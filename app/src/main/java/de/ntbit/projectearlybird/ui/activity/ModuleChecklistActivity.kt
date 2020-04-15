package de.ntbit.projectearlybird.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.adapter.item.ChecklistItem
import de.ntbit.projectearlybird.helper.ParcelContract
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.model.Group
import de.ntbit.projectearlybird.model.ModuleChecklist
import kotlinx.android.synthetic.main.activity_module_checklist.*
import kotlinx.android.synthetic.main.toolbar.*
import java.lang.Exception

class ModuleChecklistActivity : AppCompatActivity() {

    private val simpleClassName = this.javaClass.simpleName

    private val mModuleChecklistManager = ManagerFactory.getModuleChecklistManager()

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

    private fun initialize() {
        group = intent.getParcelableExtra(ParcelContract.GROUP_KEY)
        moduleChecklist = intent.getParcelableExtra(ParcelContract.MODULE_KEY)
        adapter = mModuleChecklistManager.getAdapterByGroup(group)
        act_module_checklist_rv_log.adapter = adapter
        placeToolbar()
        setClicklisteners()
        //adapter.addAll(module.items)
    }

    private fun placeToolbar() {
        val toolbar = act_module_checklist_toolbar
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar_tv_root_title.text = moduleChecklist.name
    }

    private fun setClicklisteners() {
        act_module_checklist_fab.setOnClickListener {
            showCreateCategoryDialog()
        }
    }

/*
    private fun addChecklistItem() {
        val dialog = AddItemDialogFragment(adapter)
        dialog.show(this.supportFragmentManager, "DIALOG_MODULE_CHECKLIST_ACTIVITY_ADD_ITEM")
    }

*/

    private fun showCreateCategoryDialog() {
        val context = this
        val builder = AlertDialog.Builder(context)
        val view = layoutInflater.inflate(R.layout.dialog_add_checklist_item, null)

        val categoryEditText = view.findViewById(R.id.dialog_add_checklist_item_et_itemname) as EditText

        builder.setView(view)
        // set up the ok button
        builder.setPositiveButton(android.R.string.ok) { dialog, p1 ->
            val newCategory = categoryEditText.text
            var isValid = true
            if (newCategory.isBlank()) {
                isValid = false
            }
            if (isValid) {
                addItem(de.ntbit.projectearlybird.model.ModuleChecklistItem(
                    categoryEditText.text.toString(), moduleChecklist))
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

    private fun addItem(item: de.ntbit.projectearlybird.model.ModuleChecklistItem) {
        val checklistItem = ChecklistItem(item)
        //items.add(checklistItem)
        adapter.add(checklistItem)
        adapter.notifyDataSetChanged()
        try {
            item.save()
        }
        catch(e: Exception) {
            Log.d("CUSTOMDEBUG", "$simpleClassName - Error: ${e.message}")
        }
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
