package de.ntbit.projectearlybird.model

abstract class Group internal constructor(var id: String, var name: String) {

    override fun toString(): String {
        return this.name
    }
}
