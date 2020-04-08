package de.ntbit.projectearlybird.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.adapter.item.ModuleChecklistItem
import de.ntbit.projectearlybird.model.ModuleChecklist
import kotlinx.android.synthetic.main.activity_module_checklist.*

class ModuleChecklistActivity : AppCompatActivity() {

    private val adapter = GroupAdapter<GroupieViewHolder>()
    private val items = ArrayList<ModuleChecklistItem>()
    private val module = intent.getParcelableExtra<ModuleChecklist>("MODULE")

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
        act_module_checklist_rv_log.adapter = adapter
        buildTestLayout()
        adapter.addAll(module.items)
    }

    private fun buildTestLayout() {
        items.add(ModuleChecklistItem("Bier"))
        items.add(ModuleChecklistItem("Klopapier"))
        items.add(ModuleChecklistItem("Nudeln"))
        items.add(ModuleChecklistItem("Mehl"))
        items.add(ModuleChecklistItem("Linsen"))
        items.add(ModuleChecklistItem("Eis"))
    }
}
