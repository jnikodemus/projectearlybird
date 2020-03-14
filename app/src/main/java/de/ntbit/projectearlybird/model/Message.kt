package de.ntbit.projectearlybird.model

import com.parse.ParseACL
import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseUser
import java.util.Date

@ParseClassName("Message")
class Message : ParseObject {

    internal constructor() : super()

    internal constructor(sender: ParseUser, recipient: ParseUser, body: String) : super() {
        this.sender = sender
        this.recipient = recipient
        this.body = body

        generateTimestamp()
        generateThreadId()
        generateACL()
    }

    var sender: ParseUser
        get() {
            return this.getParseUser("sender")!!
        }
        set(sender) {
            this.put("sender", sender)
        }

    var recipient: ParseUser
        get() {
            return this.getParseUser("recipient")!!
        }
        set(recipient) {
            this.put("recipient", recipient)
        }

    var threadId: String
        get() {
            return this.getString("threadId")!!
        }
        set(threadId) {
            this.put("threadId", threadId)
        }

    var body: String
        get() {
            return this.getString("body")!!
        }
        set(body) {
            this.put("body", body)
        }

    var timestamp: Date
        get() {
            return this.getDate("timestamp")!!
        }
        set(timestamp) {
            this.put("timestamp", timestamp)
        }

    var parseACL: ParseACL
        get() {
            return super.getACL()!!
        }
        set(parseACL) {
            this.put("ACL",parseACL)
        }

    private fun generateTimestamp() {
        this.timestamp = Date(System.currentTimeMillis())
    }

    private fun generateThreadId() {
        /*
         * TODO: Change threadId to something more useful
         */
        this.threadId = sender.objectId + recipient.objectId
    }
    private fun generateACL() {
        val acl = ParseACL()
        acl.setReadAccess(recipient, true)
        acl.setWriteAccess(sender, true)
        this.parseACL = acl
    }
}
