package de.ntbit.projectearlybird.model

import com.parse.ParseClassName
import com.parse.ParseObject
import java.util.logging.Logger

@ParseClassName("UserProfile")
abstract class Group : ParseObject {

    private val log: Logger = Logger.getLogger(this::class.java.simpleName)
/*
    private var members: MutableList<UserProfile>
        get() = getList<UserProfile>()
        private set(userFk) {
            addUnique("userFk", userFk)
        }
        
 */

    constructor()
}
