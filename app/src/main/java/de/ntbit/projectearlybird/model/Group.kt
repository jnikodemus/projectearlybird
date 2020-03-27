package de.ntbit.projectearlybird.model

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser
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

    private val log: Logger = Logger.getLogger(this::class.java.simpleName)

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
            groupImage.save()
            this.put("groupImage", groupImage)
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

    fun getGroupSize() : Int {
        return this.members.size
    }
}
