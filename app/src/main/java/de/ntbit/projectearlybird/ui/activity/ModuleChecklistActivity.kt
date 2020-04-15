package de.ntbit.projectearlybird.ui.activity

import android.os.Bundle
import android.app.Dialog
import android.content.DialogInterface
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.adapter.item.ModuleChecklistItem
import de.ntbit.projectearlybird.helper.ParcelContract
import de.ntbit.projectearlybird.model.Group
import de.ntbit.projectearlybird.model.Module
import kotlinx.android.synthetic.main.activity_module_checklist.*
import kotlinx.android.synthetic.main.dialog_add_checklist_item.*
import kotlinx.android.synthetic.main.toolbar.*

class ModuleChecklistActivity : AppCompatActivity() {

    private val simpleClassName = this.javaClass.simpleName
    private val adapter = GroupAdapter<GroupieViewHolder>()
    private val items = ArrayList<ModuleChecklistItem>()
    private lateinit var group: Group
    private lateinit var module: Module

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
        module = intent.getParcelableExtra(ParcelContract.MODULE_KEY)
        act_module_checklist_rv_log.adapter = adapter
        placeToolbar()
        setClicklisteners()
        //buildTestLayout()
        //adapter.addAll(module.items)
    }

    private fun placeToolbar() {
        val toolbar = act_module_checklist_toolbar
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar_tv_root_title.text = "${module.name}"
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

    private fun buildTestLayout() {
        items.add(ModuleChecklistItem("Bier"))
        items.add(ModuleChecklistItem("Klopapier"))
        items.add(ModuleChecklistItem("Nudeln"))
        items.add(ModuleChecklistItem("Mehl"))
        items.add(ModuleChecklistItem("Linsen"))
        items.add(ModuleChecklistItem("Eis"))
    }

    fun showCreateCategoryDialog() {
        val context = this
        val builder = AlertDialog.Builder(context)
        builder.setTitle("New Category")
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
                Log.d("CUSTOMDEBUG", "$simpleClassName - " +
                        "User added ${categoryEditText.text}")
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
