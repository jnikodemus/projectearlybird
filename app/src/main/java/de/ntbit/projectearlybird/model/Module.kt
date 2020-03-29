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

    var name: String
        get() {
            return this.getString("name")!!
        }
        private set(name) {
            this.put("name", name)
        }

    var colorInt: Int
        get() {
            return this.getInt("colorInt")
        }
        private set(colorInt) {
            this.put("colorInt", colorInt)
        }
}