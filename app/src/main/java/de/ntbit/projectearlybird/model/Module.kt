package de.ntbit.projectearlybird.model

import android.graphics.Color
import com.parse.ParseClassName
import com.parse.ParseObject

/**
 * Model corresponding to table "Module" in Parse Database extends [ParseObject]
 *
 * @property name of the module
 * @property description small information about the module
 * @property colorInt background color for the ui
 */
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

    /**
     * Checks this equals [other] by using the [name].
     *
     * @return true if [other] is [Module] and names are the same, false else.
     */
    override fun equals(other: Any?): Boolean {
        if(other is Module) {
            return other.name == this.name
        }
        return false
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return "$name ($description)"
    }
}