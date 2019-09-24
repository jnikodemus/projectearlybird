package de.ntbit.projectearlybird.model

import com.parse.ParseClassName
import com.parse.ParseObject
import java.util.*

@ParseClassName("Message")
class Message : ParseObject() {

    var sender: UserProfile
        get() = getParseObject("sender") as UserProfile
        set(sender) {
            put("sender", sender)
        }
    var receiver: UserProfile
        get() = getParseObject("receiver") as UserProfile
        set(receiver) {
            put("receiver", receiver)
        }
    var content: String
        get() = getString("content")!!
        set(content) {
            put("content", content)
        }
    var timeStamp: Date
        get() = getDate("timeStamp")!!
        set(timeStamp) {
            put("timeStamp", timeStamp)
        }
}