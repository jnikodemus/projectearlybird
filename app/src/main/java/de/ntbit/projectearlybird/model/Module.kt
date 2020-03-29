package de.ntbit.projectearlybird.model

import android.widget.ImageView

open class Module {
    internal constructor() : super()

    internal constructor(name: String, icon: ImageView) {
        this.name = name
        this.icon = icon

    }

    var name: String
        get() {
            return name
        }
        set(name) {
            this.name = name
        }

    var icon: ImageView
        get() {
            return icon
        }
        set(name) {
            this.icon = icon
        }
}