package de.ntbit.projectearlybird.model

import com.parse.ParseClassName
import com.parse.ParseObject

@ParseClassName("Test")
class Test internal constructor() : ParseObject() {
    val testVal = 1
}