package de.ntbit.projectearlybird.model

import android.graphics.Color
import com.parse.ParseClassName
import com.parse.ParseObject

@ParseClassName("Module")
open class Module : ParseObject {

    internal constructor() : super()

    internal constructor(name: String) {
        this.name = name
    }

    internal constructor(other: Module) {
        this.name = other.name
        this.description = other.description
        this.colorInt = other.colorInt
    }

    var name: String
        get() {
            return this.getString("name")!!
        }
        protected set(name) {
            this.put("name", name)
        }

    var colorInt: Int
        get() {
            return this.getInt("colorInt")
        }
        set(colorInt) {
            this.put("colorInt", colorInt)
        }

    var description: String
        get() {
            return this.getString("description")!!
        }
        protected set(description) {
            this.put("description", description)
        }
}