package de.ntbit.projectearlybird.model

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser
import java.util.logging.Logger

@ParseClassName("UserProfile")
class Group : ParseObject {

    private val log: Logger = Logger.getLogger(this::class.java.simpleName)

    internal constructor() : super()

    internal constructor(name: String, members: MutableCollection<ParseUser>, logo: ParseFile) : super() {
        this.name = name
        this.logo = logo
        this.members = ArrayList(members)
        this.members.addAll(members)
    }

    var name: String
        get() {
            return this.getString("name")!!
        }
        set(name) {
            this.put("name", name)
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

    fun getGroupSize() : Int {
        return this.members.size
    }
}
