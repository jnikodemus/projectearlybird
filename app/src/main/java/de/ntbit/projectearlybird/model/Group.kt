package de.ntbit.projectearlybird.model

import java.util.logging.Logger

abstract class Group internal constructor(var id: String, var name: String) {

    private val log = Logger.getLogger(this::class.java.simpleName)

    override fun toString(): String {
        return this.name
    }
}
