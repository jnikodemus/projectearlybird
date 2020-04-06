package de.ntbit.projectearlybird.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.adapter.item.UserItem
import de.ntbit.projectearlybird.helper.InputValidator
import de.ntbit.projectearlybird.helper.PixelCalculator
import de.ntbit.projectearlybird.manager.GroupManager
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.manager.UserManager
import de.ntbit.projectearlybird.model.Group
import de.ntbit.projectearlybird.model.User
import kotlinx.android.synthetic.main.activity_create_group.*


class CreateGroupActivity : AppCompatActivity() {

    companion object {
        val GROUP_KEY = "GROUP"
    }

    private val mGroupManager: GroupManager = ManagerFactory.getGroupManager()
    private val mUserManager: UserManager = ManagerFactory.getUserManager()

    private val adapter = GroupAdapter<GroupieViewHolder>()

    private lateinit var createdGroup: Group

    val GALLERY_REQUEST_CODE = 1234
    val IMAGE_GROUP_DEFAULT_URI = "android.resource://de.ntbit.projectearlybird/drawable/default_group_image"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

        Log.d("CUSTOMDEBUG", "${this.javaClass.simpleName} - ")

        initialize()
    }

    private fun initialize() {
        placeToolbar()
        setDefaultImage()
        createInitialGroup()
        fetchAllParseUser()
        act_create_group_rv_contacts.adapter = adapter
        setClickListeners()
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onSupportNavigateUp()
        onBackPressed()
        return true
    }

    private fun placeToolbar() {
        val toolbar = act_create_group_toolbar
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Create a new Group"
    }

    private fun setClickListeners() {

        adapter.setOnItemClickListener { item, view ->
            val userItem = item as UserItem
            toggleView(view)
            processUserClicked(userItem.user)
        }

        act_create_group_check_fab.setOnClickListener {
            // TODO: Check name length
            if(InputValidator.isValidInputNotNullNotEmpty(act_create_group_et_name)) {
                createdGroup.name = act_create_group_et_name.text.toString()
                val intent = Intent(this, GroupActivity::class.java)
                intent.putExtra(GROUP_KEY, createdGroup)

                createdGroup.saveEventually()
                startActivity(intent)
                finish()
            }
        }

        crt_group_iv_avatar.setOnClickListener {
            pickFromGallery()
        }
    }

    private fun createInitialGroup() {
        val initialGroupImage = Group.convertBitmapToParseFileByUri(contentResolver, Uri.parse(IMAGE_GROUP_DEFAULT_URI))
        createdGroup = Group("anonGroup", mUserManager.getCurrentUser(), ArrayList(), initialGroupImage)
        createdGroup.name += createdGroup.objectId
    }

    private fun processUserClicked(user: User) {
        if(createdGroup.members.contains(user))
            createdGroup.members.remove(user)
        else createdGroup.members.add(user)
    }

    private fun toggleView(view: View) {
        view.isActivated = !view.isActivated
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
                        createdGroup.groupImage = Group.convertBitmapToParseFileByUri(this.contentResolver, uri)
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
                            .into(crt_group_iv_avatar)
                        createdGroup.croppedImage = Group.convertBitmapToParseFileByUri(this.contentResolver, it)
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

    private fun fetchAllParseUser() {
        adapter.clear()
        for(contact in mUserManager.getMyContacts()) {
            adapter.add(UserItem(contact))
        }
        adapter.notifyDataSetChanged()
    }

    private fun setDefaultImage() {
        crt_group_iv_avatar.layoutParams.height = PixelCalculator.calculateHeightForFullHD()
        val uri = Uri.parse(IMAGE_GROUP_DEFAULT_URI)
        Picasso.get()
            .load(uri)
            .fit()
            .centerCrop()
            .into(crt_group_iv_avatar)
    }
}

/*
class CheckedContactItem: Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }

    override fun getLayout(): Int {
        return R.layout.row_crt_group_contact
    }

}
*/