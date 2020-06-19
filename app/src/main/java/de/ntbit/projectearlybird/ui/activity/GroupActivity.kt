package de.ntbit.projectearlybird.ui.activity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
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


class GroupActivity : AppCompatActivity() {

    private val simpleClassName = this.javaClass.simpleName

    private val mGroupManager = ManagerFactory.getGroupManager()
    private val mModuleManager = ManagerFactory.getModuleManager()
    private lateinit var group: Group
    private val adapter = GroupAdapter<GroupieViewHolder>()
    val GALLERY_REQUEST_CODE = 1234

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
            R.id.group_context_menu_add_user -> {
                displayContacts()
                return false
            }

            R.id.group_context_menu_change_image -> {
                pickFromGallery()
                return true
            }

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
        act_group_iv_image.layoutParams.height = PixelCalculator.calculateHeightForFullHD()
        var uri = group.groupImage.url
        //if(group.croppedImage != null)
        //    uri = group.croppedImage!!.url
        Picasso.get()
            .load(uri)
            .fit()
            .centerCrop()
            .into(act_group_iv_image)
    }

    private fun loadModules() {
        for(m in group.modules) {
            Log.d("CUSTOMDEBUG", "$simpleClassName.loadModules() - Barrier0: " +
                    "isDirty:${m.isDirty} " +
                    "isDataAvailable:${m.isDataAvailable} - " +
                    "${group.objectId} ${m.objectId}")
            adapter.add(ModuleItem(Module(m.fetchIfNeeded<Module>())))
        }
    }

    private fun setClicklistener() {
        toolbar_tv_root_title.setOnClickListener {
            val intent = Intent(this, GroupSettingsActivity::class.java)
            intent.putExtra(ParcelContract.GROUP_KEY, group)
            startActivity(intent)
        }

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

    private fun pickFromGallery(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            GALLERY_REQUEST_CODE -> {
                if(resultCode == Activity.RESULT_OK){
                    data?.data?.let { uri ->
                        launchImageCrop(uri)
                        group.groupImage = Group.convertBitmapToParseFileByUri(this.contentResolver, uri)
                    }
                }
            }
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if(resultCode == Activity.RESULT_OK){
                    result.uri?.let {
                        Picasso.get()
                            .load(it)
                            .fit()
                            .centerCrop()
                            .into(act_group_iv_image)
                        group.croppedImage = Group.convertBitmapToParseFileByUri(this.contentResolver, it)
                        mGroupManager.getAdapter().notifyDataSetChanged()
                    }
                }
            }
            CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE -> {
                Log.d("DEBUG", "Error ${CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE} occured.")
            }
        }
    }


    private fun launchImageCrop(uri: Uri) {
        CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON)
            .setAspectRatio(1920, 1080)
            .start(this)
    }

    private fun displayContacts() {

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