package de.ntbit.projectearlybird.ui

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.adapter.UserItem
import de.ntbit.projectearlybird.manager.GroupManager
import de.ntbit.projectearlybird.manager.ManagerFactory
import de.ntbit.projectearlybird.manager.UserManager
import kotlinx.android.synthetic.main.activity_create_group.*
import java.security.AccessController.getContext


class CreateGroupActivity : AppCompatActivity() {

    private val mGroupManager: GroupManager = ManagerFactory.getGroupManager()
    private val mUserManager: UserManager = ManagerFactory.getUserManager()
    private val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

        fetchAllParseUser()

        crt_group_rv_contacts.adapter = adapter
/*
        crt_group_iv_avatar.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
      */
 */
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri: Uri = data.data
            CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this)
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)

        }
    }

    private fun fetchAllParseUser() {
        adapter.clear()
        for(contact in mUserManager.getMyContacts()) {
            adapter.add(UserItem(contact))
        }
        adapter.notifyDataSetChanged()
    }
}

class CheckedContactItem: Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        
    }

    override fun getLayout(): Int {
        return R.layout.row_crt_group_contact
    }

}
