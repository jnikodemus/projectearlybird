package de.ntbit.projectearlybird.model

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.parse.*
import java.io.ByteArrayOutputStream
import java.util.logging.Logger

@ParseClassName("Group")
class Group : ParseObject {

    companion object {
        fun convertBitmapToParseFileByUri(contentResolver: ContentResolver, uri: Uri) : ParseFile{
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            return convertBitmapToParseFile(bitmap)
        }

        fun convertBitmapToParseFile(bitmap: Bitmap) : ParseFile {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val image: ByteArray = stream.toByteArray()
            return ParseFile(image)
        }
    }

    internal constructor() : super()

    internal constructor(name: String,
                         owner: ParseUser,
                         members: MutableCollection<ParseUser>,
                         groupImage: ParseFile) : super() {
        this.name = name
        this.groupImage = groupImage
        this.owner = owner
        
        this.members = ArrayList(members)
        this.members.add(owner)

        this.admins = ArrayList()
        this.admins.add(owner)

        this.modules = ArrayList()

        generateACL()
    }

    var name: String
        get() {
            return this.getString("name")!!
        }
        set(name) {
            this.put("name", name)
        }

    var owner: ParseUser
        get() {
            return this.getParseUser("owner")!!
        }
        set(owner) {
            this.put("owner", owner)
        }

    var groupImage: ParseFile
        get() {
            return this.getParseFile("groupImage")!!
        }
        set(groupImage) {
            //groupImage.save()
            this.put("groupImage", groupImage)
        }

    var croppedImage: ParseFile?
        get() {
            return this.getParseFile("croppedImage")
        }
        set(croppedImage) {
            //croppedImage?.save()
            if (croppedImage != null) {
                this.put("croppedImage", croppedImage)
            }
        }

    var members: ArrayList<ParseUser>
            get() {
                return this.getList<ParseUser>("members") as ArrayList<ParseUser>
            }
            set(members) {
                this.put("members", members)
            }

    var admins: ArrayList<ParseUser>
        get() {
            return this.getList<ParseUser>("admins") as ArrayList<ParseUser>
        }
        set(admins) {
            this.put("admins", admins)
        }

    var parseACL: ParseACL
        get() {
            return super.getACL()!!
        }
        set(parseACL) {
            this.put("ACL",parseACL)
        }

    var modules: ArrayList<Module>
        get() {
            return this.getList<Module>("modules") as ArrayList<Module>
        }
        set(modules) {
            Log.d("CUSTOMDEBUG", "Group - setting modules with size ${modules.size}")
            this.put("modules", modules)
        }

    fun addModule(module: Module) {
        addUnique("modules", module)
        //module.saveEventually()
        //saveEventually()
    }

    fun getSize() : Int {
        return this.members.size
    }

    fun getModuleNames(): String {
        var moduleList = ""
        for(m in modules)
            moduleList += ", " + m.name
        return moduleList
    }

    private fun generateACL() {
        val acl = ParseACL()
        acl.publicReadAccess = false
        acl.publicWriteAccess = false
        for(user in members) {
            acl.setReadAccess(user, true)
            acl.setWriteAccess(user, true)
        }
        this.parseACL = acl
    }

    fun updateACL() {
        generateACL()
    }
}
