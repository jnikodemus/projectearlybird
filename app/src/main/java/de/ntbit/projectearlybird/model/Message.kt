package de.ntbit.projectearlybird.model

import com.parse.ParseACL
import com.parse.ParseClassName
import com.parse.ParseObject
import java.util.Date

@ParseClassName("Message")
class Message : ParseObject {
    private lateinit var sender: String
    private lateinit var recipient: String
    private lateinit var threadId: String
    private lateinit var body: String
    private lateinit var timestamp: Date
    private lateinit var parseACL: ParseACL

    internal constructor() : super() {}
    internal constructor(sender: String, recipient: String, threadId: String, body: String, timestamp: Date) : super() {
        this.sender = sender
        this.recipient = recipient
        this.threadId = threadId
        this.body = body
        this.timestamp = timestamp
        this.parseACL = ParseACL()
        this.parseACL.setReadAccess(recipient, true)
        this.parseACL.setWriteAccess(sender, true)
    }

    fun setSender(sender: String) {
        put("sender", sender)
    }

    fun setRecipient(recipient: String){
        put("recipient", recipient)
    }

    fun setThreadId(threadId: String){
        put("threadId", threadId)
    }

    fun setBody(body: String){
        put("body", body)
    }

    fun setTimestamp(timestamp: Date){
        put("timestamp", timestamp)
    }

    fun getSender(): String {
        return sender
    }

    fun getRecipient(): String {
        return recipient
    }

    fun getThreadId(): String {
        return threadId
    }

    fun getBody(): String {
        return body
    }

    fun getTimestamp(): Date {
        return timestamp
    }

    fun print() {
        println("Sender: $sender Recipient: $recipient Thread: $threadId " +
                "Timestamp: $timestamp ACL: $parseACL Body: $body")
    }

}
