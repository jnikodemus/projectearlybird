package de.ntbit.projectearlybird.model

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.parse.*
import com.parse.coroutines.read.parse_object.fetch
import java.io.ByteArrayOutputStream
import java.util.logging.Logger

/**
 * Model corresponding to table "Group" in Parse Database extends [ParseObject]
 *
 * @property name of the group
 * @property groupImage banner of the group
 * @property members contains all [User] that belong to the group
 * @property admins contains all admins that belong to the group
 * @property modules contains all [Module] that belong the group
 */
@ParseClassName("Group")
class Group : ParseObject {

    companion object {

        /**
         * Static method to convert a [Bitmap] to [ParseFile] expecting [contentResolver] and [uri].
         * Calls [convertBitmapToParseFile] for compression and at long last convert.
         * @return [ParseFile]
         */
        fun convertBitmapToParseFileByUri(contentResolver: ContentResolver, uri: Uri) : ParseFile{
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            return convertBitmapToParseFile(bitmap)
        }

        /**
         * Static method to convert a [Bitmap] to [ParseFile] expecting only the [Bitmap].
         * The provided Bitmap is compressed to [Bitmap.CompressFormat.PNG] in 100% quality.
         * @return [ParseFile]
         */
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
            // TODO: CHECK save()
            groupImage.save()
            this.put("groupImage", groupImage)
        }

    var croppedImage: ParseFile?
        get() {
            return this.getParseFile("croppedImage")
        }
        set(croppedImage) {
            // TODO: CHECK save()
            croppedImage?.save()
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

    /**
     * Adds provided [module] to [Group.modules] if it doesn't already exist.
     */
    fun addModule(module: Module) {
        addUnique("modules", module)
        module.acl = parseACL
        module.save()
    }

    /**
     * Returns number of members in actual [Group]
     * @return [Int]
     */
    fun getSize() : Int {
        return this.members.size
    }

    /**
     * Writes all module names to a [String] and returns it.
     * @return [String]
     */
    fun getModuleNames(): String {
        var moduleList = ""
        for(m in modules) {
            m.fetchIfNeeded<Module>()
            moduleList += m.name + " "
        }
        return moduleList
    }

    /**
     * Creates a new [ParseACL] setting public read and write access to false.
     * Afterwards read and write access for every user in [Group.members] is set to true
     * and resulting acl is written to [Group.parseACL]
     */
    private fun generateACL() {
        val acl = ParseACL()
        acl.publicReadAccess = false
        acl.publicWriteAccess = false
        for(user in members) {
            acl.setReadAccess(user, true)
            acl.setWriteAccess(user, true)
        }
        this.parseACL = acl
        //updateModuleACL()
    }

    private fun updateModuleACL() {
        for(module in modules)
            module.acl = this.parseACL
    }

    /**
     * Instantly calls private method [generateACL].
     */
    fun updateACL() {
        generateACL()
    }

    fun getModuleByName(moduleName: String): Module? {
        for(module in modules) {
            if (module.name == moduleName) return module
        }
        return null
    }
}
