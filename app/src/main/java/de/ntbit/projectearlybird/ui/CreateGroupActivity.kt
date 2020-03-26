package de.ntbit.projectearlybird.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.parse.ParseFile
import com.parse.ParseUser
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.adapter.UserItem
import de.ntbit.projectearlybird.manager.GroupManager
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.manager.UserManager
import de.ntbit.projectearlybird.model.Group
import kotlinx.android.synthetic.main.activity_create_group.*
import kotlinx.android.synthetic.main.toolbar.*


class CreateGroupActivity : AppCompatActivity() {

    private val mGroupManager: GroupManager = ManagerFactory.getGroupManager()
    private val mUserManager: UserManager = ManagerFactory.getUserManager()

    private val adapter = GroupAdapter<GroupieViewHolder>()

    private val groupMember = HashSet<ParseUser>()

    val GALLERY_REQUEST_CODE = 1234
    val GROUP_MEMBER_KEY = "GROUP_MEMBER_LIST"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

        initialize()

    }

    private fun initialize() {
        placeToolbar()
        fetchAllParseUser()
        crt_group_rv_contacts.adapter = adapter
        setClickListeners()
    }

    private fun placeToolbar() {
        // TODO: Set toolbar.title
        val toolbar = toolbar
        setSupportActionBar(toolbar)
    }

    private fun setClickListeners() {

        adapter.setOnItemClickListener { item, view ->
            val userItem = item as UserItem
            toggleView(view)
            processUserClicked(userItem.user)
        }

        actCreatGroup_check_fab.setOnClickListener {
            //val groupImage = ParseFile(crt_group_iv_avatar)
            //val group: Group = Group(activity_crt_group_et_groupname.text.toString(), mUserManager.getCurrentUser(), groupMember, )
            val intent = Intent(this, GroupActivity::class.java)
            //intent.putExtra(GROUP_MEMBER_KEY, groupMember)
            startActivity(intent)
            finish()
        }

        crt_group_iv_avatar.setOnClickListener {
            pickFromGallery()
        }
    }

    private fun processUserClicked(user: ParseUser) {
        if(groupMember.contains(user))
            groupMember.remove(user)
        else groupMember.add(user)
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
                    }
                }
            }
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if(resultCode == Activity.RESULT_OK){
                    result.uri?.let {
                        Picasso.get().load(it).into(crt_group_iv_avatar)
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