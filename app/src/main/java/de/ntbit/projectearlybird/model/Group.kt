package de.ntbit.projectearlybird.model

import android.graphics.Bitmap
import android.widget.ImageView
import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser
import java.io.ByteArrayOutputStream
import java.util.logging.Logger

@ParseClassName("UserProfile")
class Group : ParseObject {

    private val log: Logger = Logger.getLogger(this::class.java.simpleName)

    internal constructor() : super()

    internal constructor(name: String, owner: ParseUser, members: MutableCollection<ParseUser>, rawLogo: ImageView) : super() {
        this.name = name
        //this.logo = convertRawImageToParseFile(rawLogo)
        this.owner = owner
        this.members = ArrayList(members)
        this.members.add(owner)
        this.admins = ArrayList()
        this.admins.add(owner)
    }

    private fun convertRawImageToParseFile(rawLogo: ImageView): ParseFile? {
        /*
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val image: ByteArray = stream.toByteArray()
        getCurrentUser().put("avatar", ParseFile(image))
        getCurrentUser().saveEventually()
         */
        return null
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

    var logo: ParseFile
        get() {
            return this.getParseFile("logo")!!
        }
        set(logo) {
            this.put("logo", logo)
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
