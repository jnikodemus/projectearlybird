package de.ntbit.projectearlybird.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import de.ntbit.projectearlybird.R

class ModuleChecklistActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_module_checklist)
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onSupportNavigateUp()
        onBackPressed()
        return true
    }
}
