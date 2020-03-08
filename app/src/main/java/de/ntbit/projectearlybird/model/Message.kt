package de.ntbit.projectearlybird.model

import com.parse.ParseACL
import com.parse.ParseClassName
import com.parse.ParseObject
import java.util.Date

@ParseClassName("Message")
class Message : ParseObject {
    private var sender: String? = null
    private var recipient: String? = null
    private var threadId: String? = null
    private var body: String? = null
    private var timestamp: Date? = null
    private lateinit var parseACL: ParseACL

    internal constructor() : super() {}
    internal constructor(
        sender: String?,
        recipient: String?,
        threadId: String?,
        body: String?,
        timestamp: Date?
    ) {
        this.sender = sender
        this.recipient = recipient
        this.threadId = threadId
        this.body = body
        this.timestamp = timestamp
        this.parseACL = ParseACL()
        this.parseACL.setReadAccess(recipient, true)
        this.parseACL.setWriteAccess(sender, true)
    }

    fun print() {
        println("Sender: $sender Recipient: $recipient Thread: $threadId " +
                "Timestamp: $timestamp ACL: $parseACL Body: $body")
    }
}
